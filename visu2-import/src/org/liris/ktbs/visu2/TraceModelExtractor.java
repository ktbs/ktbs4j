package org.liris.ktbs.visu2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.Mode;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultimap;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * A class that allow to infer a trace model from a set of traces
 * serialized in a RDF model.
 * 
 * @author Damien Cram
 *
 */
public class TraceModelExtractor {

	private ResourceRepository repository;
	private String baseURI;

	private Comparator<String[]> itemsetComparatorArray = new Comparator<String[]>() {
		@Override
		public int compare(String[] o1, String[] o2) {
			return itemsetComparator.compare(Arrays.asList(o1), Arrays.asList(o2));
		}
	};
	
	/**
	 * Create a new extractor that will operate in a given 
	 * base of a given repository.
	 * 
	 * @param repository
	 * @param baseURI
	 */
	public TraceModelExtractor(ResourceRepository repository, String baseURI) {
		super();
		this.repository = repository;
		this.baseURI = baseURI;
	}

	private Comparator<List<String>> itemsetComparator = new Comparator<List<String>>() {
		public int compare(List<String> l1,  List<String> l2) {
			if(l1.size() == 0 && l2.size() == 0)
				return 0;
			int comp = l1.get(0).compareTo(l2.get(0));
			if(comp!= 0)
				return comp;
			return compare(l1.subList(1, l1.size()), l2.subList(1, l2.size()));
		}
	};


	/**
	 * Extract a trace model from a set of traces serialized in a RDF model
	 * and add it to the base this extractor operates on.
	 * 
	 * @param modelURI the uri of the new model to create
	 * @param model the RDF statements describing the traces as a Jena model
	 * @return the extracted model
	 */
	public TraceModel inferTraceModel(String modelURI, Model model) {

		StmtIterator it = model.listStatements(
				null, 
				model.getProperty(KtbsConstants.P_HAS_TRACE), 
				(RDFNode)null);

		Multiset<String> obselTypes = HashMultiset.create();
		Multiset<String> traces = HashMultiset.create();

		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			traces.add(statement.getObject().asResource().getURI());
			Resource rdfType = statement.getSubject().getPropertyResourceValue(RDF.type);
			obselTypes.add(rdfType.getURI());
		}

		/*
		 * For each obel type, map all its attribute to it in a multimap
		 */
		Multimap<String, String> typeAttributes = generateTypeAttributes(model,obselTypes.elementSet());
		List<Collection<String>> attributeGroups = generateAttributeGroups(typeAttributes);

		typeAttributes = generateTypeAttributes(model,obselTypes.elementSet());
		TraceModel traceModel = generateTraceModel(
				repository,
				baseURI,
				modelURI,
				attributeGroups,
				typeAttributes
		);

		return traceModel;
	}

	private  Multimap<String, String> generateTypeAttributes(Model model,
			Collection<String> obselTypes) {
		Multimap<String, String> typeAttributes = TreeMultimap.create();
		for(String obselType:obselTypes) {
			StmtIterator obselIt = model.listStatements(null, RDF.type, model.getResource(obselType));

			while (obselIt.hasNext()) {
				Statement statement = (Statement) obselIt.next();
				Resource obsel = statement.getSubject();
				StmtIterator obselPropIt = obsel.listProperties();
				while (obselPropIt.hasNext()) {
					Statement propStat = (Statement) obselPropIt.next();

					typeAttributes.put(
							obselType.replaceAll("http://localhost:8001/visu/", "").replaceAll(KtbsConstants.NAMESPACE, "ktbs:").replaceAll(RDF.getURI(), "rdf:"), 
							propStat.getPredicate().getURI().replaceAll("http://localhost:8001/visu/", "").replaceAll(KtbsConstants.NAMESPACE, "ktbs:").replaceAll(RDF.getURI(), "rdf:")
					);
				}
			}
		}
		return typeAttributes;
	}



	private  List<Collection<String>> generateAttributeGroups(
			Multimap<String, String> typeAttributes) {
		List<Collection<String>> attributeGroups = new LinkedList<Collection<String>>();


		int nbTypes = typeAttributes.keySet().size();
		for(int freq=nbTypes;freq>0;freq--) {
			/*
			 * Get the biggest set of attributes that are frequent of size nbTypes.
			 */
			Collection<String[]> biggestSets = getBiggestSet(freq, typeAttributes);

			if(biggestSets == null || biggestSets.size() == 0) {

			} else {

				for(String[] attGroup:biggestSets) {
					attributeGroups.add(Arrays.asList(attGroup));
					// remove these attributes from existing obsel types
					Collection<String> allObselTypes = new HashSet<String>();
					allObselTypes.addAll(typeAttributes.keySet());
					for(String obselType:allObselTypes) {
						for(String attribute:attGroup) 
							typeAttributes.remove(obselType, attribute);
					}
				}
			}
		}
		return attributeGroups;
	}

	private  TraceModel generateTraceModel(ResourceRepository repository, String baseURI, String modelURI, List<Collection<String>> attributeGroups, Multimap<String, String> typeAttributes) {

		if(!repository.exists(baseURI))
			repository.createBase(baseURI);

		TraceModel model = repository.createTraceModel(
				repository.getResource(baseURI, Base.class), 
				modelURI);
		Set<String> remainingObselTypes = new HashSet<String>(typeAttributes.keySet());

		// Create a copy of the multimap that will be emptied during the process
		Multimap<String, String> remainingTypeAttributes = TreeMultimap.create();
		remainingTypeAttributes.putAll(typeAttributes);


		int anonymousObselTypeId = 1;
		for(Collection<String> attributeGroup:attributeGroups) {

			// the set of obsel type names that are impacted by the current attribute group
			Set<String> affectedObselTypes = new HashSet<String>();

			// Remove all attributes of current group from the multimap
			for(String obselType:remainingObselTypes) {
				for(String att:attributeGroup) {
					if(remainingTypeAttributes.containsEntry(obselType, att)) {
						remainingTypeAttributes.remove(obselType, att);
						affectedObselTypes.add(obselType);
					}
				}
			}

			// Identify obsel types that have no more attributes in the multimap
			Collection<String> emptyObselTypes = new HashSet<String>(); 
			Iterator<String> it = remainingObselTypes.iterator();
			while (it.hasNext()) {
				String obselType = (String) it.next();
				if(remainingTypeAttributes.get(obselType).size()==0) {
					// the obsel type has no more attribute
					it.remove();
					emptyObselTypes.add(obselType);
				}
			}

			// create an obsel type with the attribute of the current group
			ObselType constructedObselType;
			if(emptyObselTypes.size() == 0) {
				// Create an anonymous class
				constructedObselType = model.newObselType("ObselType_" + anonymousObselTypeId++);
			} else {
				// Create an obsel type whose name is bases on empty obsel types
				boolean first = true;
				String obselTypeName = "";
				for(String emptyObselType:emptyObselTypes) {
					obselTypeName+=(first?"":"And")+emptyObselType;
					first = false;
				}

				constructedObselType = model.newObselType(obselTypeName);
			}

			// Create the attribute types and add them to the class
			for(String attribute:attributeGroup) {
				model.newAttributeType(attribute, constructedObselType);
			}

			// Find the super obsel type of that obsel type
			ObselType superObselType = null;
			Collection<ObselType> obselTypes = KtbsUtils.toLinkedList(model.listObselTypes());
			for(ObselType obsType:obselTypes) {
				if(obsType.equals(constructedObselType))
					continue;

				/*
				 *  Does this type of obsel contains all attributes that the constructedObselType is supposed to have ?
				 */

				// the collection of attributes that must be defined 
				// on the super obsel type
				Set<String> requiredAttributes = new TreeSet<String>();
				for(String affectedObselType:affectedObselTypes) {
					Collection<String> requiredForAffectedObselType = new HashSet<String>();
					requiredForAffectedObselType.addAll(typeAttributes.get(affectedObselType));
					requiredForAffectedObselType.removeAll(remainingTypeAttributes.get(affectedObselType));
					requiredAttributes.addAll(requiredForAffectedObselType);
				}
				requiredAttributes.removeAll(attributeGroup);

				// the collection of all attributes of this obsType
				Set<String> obsTypesAttributes = new TreeSet<String>();
				for(AttributeType attType:KtbsUtils.toIterable(obsType.listAttributes(Mode.INFERRED))) 
					obsTypesAttributes.add(attType.getURI().replaceAll("http://localhost:8001/visu2/unionModel/", ""));

				// check that the collections are equal
				if(requiredAttributes.containsAll(obsTypesAttributes) 
						&& obsTypesAttributes.containsAll(requiredAttributes)) 
					/*
					 *  this is the super obsel type.
					 *  attribute collections are the same
					 */
					superObselType = obsType;
			}

			if(superObselType != null)
				constructedObselType.setSuperObselType(superObselType);
			else {
				/*
				 * find the already existing ObselType that has the bigger number of required attributes
				 */
				int maxNbOfRequiredAttributes = 0;
				Collection<String> requiredAttributesNotInSuperTypes = new LinkedList<String>();
				for(ObselType obsType:obselTypes) {
					if(obsType.equals(constructedObselType))
						continue;

					// the collection of attributes that must be defined 
					// on the super obsel type
					Set<String> requiredAttributes = new TreeSet<String>();
					for(String affectedObselType:affectedObselTypes) {
						Collection<String> requiredForAffectedObselType = new HashSet<String>();
						requiredForAffectedObselType.addAll(typeAttributes.get(affectedObselType));
						requiredForAffectedObselType.removeAll(remainingTypeAttributes.get(affectedObselType));
						requiredAttributes.addAll(requiredForAffectedObselType);
					}
					requiredAttributes.removeAll(attributeGroup);

					// the collection of all attributes of this obsType
					Set<String> obsTypesAttributes = new TreeSet<String>();
					for(AttributeType attType:KtbsUtils.toIterable(obsType.listAttributes(Mode.INFERRED))) 
						obsTypesAttributes.add(attType.getURI().replaceAll("http://localhost:8001/visu2/unionModel/", ""));

					if(!requiredAttributes.containsAll(obsTypesAttributes))
						continue;

					int requiredAttributesSize = requiredAttributes.size();
					requiredAttributes.removeAll(obsTypesAttributes);
					int nbOfRequiredAttributes = requiredAttributesSize - requiredAttributes.size();
					if(nbOfRequiredAttributes>maxNbOfRequiredAttributes) {
						maxNbOfRequiredAttributes = nbOfRequiredAttributes;
						superObselType = obsType;
						requiredAttributesNotInSuperTypes = requiredAttributes;
					}
				}

				if(superObselType != null) {
					constructedObselType.setSuperObselType(superObselType);
					for(String att:requiredAttributesNotInSuperTypes) {
						AttributeType attType = model.getAttributeType("http://localhost:8001/visu2/unionModel/" + att);
						attType.addDomain(constructedObselType);
					}
				}
			}

		}
		return model;
	}


	private  Collection<String[]> getBiggestSet(int freq,
			Multimap<String, String> typeAttributes) {

		Collection<String[]> candidates = new LinkedList<String[]>();
		for(String attribute:new HashSet<String>(typeAttributes.values())) {
			candidates.add(new String[]{attribute});
		}

		int size = 1;

		Collection<String[]> oldFrequents = new HashSet<String[]>();

		boolean foundFrequents = true;

		while(foundFrequents) {

			List<String[]> frequents = new LinkedList<String[]>();

			for(String[] itemset:candidates) {
				int f = getFrequency(typeAttributes, itemset);
				if(f>=freq)
					frequents.add(itemset);
			}

			if(frequents.size() == 0)
				foundFrequents = false;
			else {
				size+=1;
				candidates = generateCandidates(frequents, size);
				oldFrequents = frequents;
			}
		} 

		if(oldFrequents.size() == 0) 
			return null;
		return oldFrequents;
	}

	private  Collection<String[]> generateCandidates(
			List<String[]> frequents, int size) {


		Collections.sort(frequents, itemsetComparatorArray);

		if(frequents.size() <= 1)
			return new LinkedList<String[]>();


			Collection<List<String[]>> blocks = getBlocks(frequents, size-2);

			Collection<String[]> candidates = new LinkedList<String[]>();
			for(List<String[]> block:blocks) 
				candidates.addAll(generateCandidatesInBlock(block, size));

			return candidates;
	}

	private  Collection<String[]> generateCandidatesInBlock(
			List<String[]> block, int newSise) {

		Collection<String[]> c = new LinkedList<String[]>();

		for(int i = 0; i<block.size(); i++) {
			String[] prefix = getPrefix(block.get(i));
			String a = block.get(i)[newSise-2];
			for(int j = i+1; j<block.size(); j++) {
				String b= block.get(j)[newSise-2];
				String[] itemset = Arrays.copyOf(prefix, newSise);
				itemset[newSise-2] = a;
				itemset[newSise-1] = b;
				c.add(itemset);
			}
		}
		return c;
	}

	private  Collection<List<String[]>> getBlocks(List<String[]> frequents, int blockSize) {
		Collection<List<String[]>> blocks = new LinkedList<List<String[]>>() ;

		String[] currentItemSet = frequents.get(0);

		List<String[]> currentBlock = new LinkedList<String[]>();
		currentBlock.add(currentItemSet);

		for(int k=1; k<frequents.size(); k++) {
			if(Arrays.equals(getPrefix(currentItemSet), getPrefix(frequents.get(k)))) {
				currentBlock.add(frequents.get(k));
			} else {
				if(currentBlock.size()>1)
					blocks.add(currentBlock);
				currentBlock = new LinkedList<String[]>();
				currentBlock.add(frequents.get(k));
			}
		}
		if(currentBlock.size()>1)
			blocks.add(currentBlock);

		return blocks;
	} 


	private  String[] getPrefix(String[] itemset) {
		return Arrays.copyOf(itemset, itemset.length-1);
	}


	private  int getFrequency(Multimap<String, String> attByObselTypes, String... attTypes) {
		int cnt = 0;
		Collection<String> c = Arrays.asList(attTypes);
		for(String t:attByObselTypes.keySet()) {
			cnt+= attByObselTypes.get(t).containsAll(c)?
					1:0;
		}
		return cnt;
	}


}
