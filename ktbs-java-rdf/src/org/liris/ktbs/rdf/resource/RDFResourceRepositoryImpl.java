package org.liris.ktbs.rdf.resource;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsResourceNotFoundException;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.ResourceLoadException;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.JenaUtils;
import org.liris.ktbs.utils.KtbsUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class RDFResourceRepositoryImpl implements ResourceRepository {

//	private KtbsJenaResourceFactory factory;

	private Map<String, KtbsJenaResource> resources;

	public RDFResourceRepositoryImpl() {
		resources = new HashMap<String, KtbsJenaResource>();
//		factory = new KtbsJenaResourceFactory(this);
	}

	@Override
	public boolean exists(String uri) {
		return resources.containsKey(uri);
	}

//	@Override
//	public <T extends KtbsResource> T getResource(String uri, Class<T> clazz) {
//		if(!exists(uri))
//			resources.put(uri, (KtbsJenaResource) factory.createResource(uri, clazz));
//		return clazz.cast(resources.get(uri));
//	}

	@Override
	public KtbsResource loadResource(
			InputStream stream, 
			String lang) throws ResourceLoadException {

		KtbsJenaResource resource = null;

		Model model = ModelFactory.createDefaultModel();
		model.read(stream, "", lang);


		// try to find the type of the resource contained
		StmtIterator it = model.listStatements(null, RDF.type, (RDFNode)null);
		Multiset<String> types = HashMultiset.create();
		Multimap<String, String> resourceURIsByType = HashMultimap.create();
		while(it.hasNext()) {
			Statement stmt = it.next();
			String typeURI = stmt.getObject().asResource().getURI();
			types.add(typeURI);
			resourceURIsByType.put(typeURI, stmt.getSubject().getURI());
		}

		if(types.size()==0)
			throw new ResourceLoadException(
					"Impossible to find the type of resource contained in the stream.", 
					JenaUtils.toTurtleString(model));
		else if(types.elementSet().contains(KtbsConstants.KTBS_ROOT)) {
			checkAtLeastNumberOfElements(model, types, KtbsConstants.KTBS_ROOT, 1);
			String rootURI = resourceURIsByType.get(KtbsConstants.KTBS_ROOT).iterator().next();
			Model filteredModel = filterModel(model, new ResourceSelector(rootURI));
			resource = new KtbsJenaRoot(rootURI, filteredModel, this);
			putResource(resource);
		} else if(types.contains(KtbsConstants.BASE)) {
			checkAtLeastNumberOfElements(model, types, KtbsConstants.BASE, 1);
			String baseURI = resourceURIsByType.get(KtbsConstants.BASE).iterator().next();
			Model filteredModel = filterModel(model, new ResourceSelector(baseURI));
			resource = new KtbsJenaBase(baseURI, filteredModel, this);
			
			putResource(resource);
		} else if(types.contains(KtbsConstants.STORED_TRACE)) {
			checkAtLeastNumberOfElements(model, types, KtbsConstants.STORED_TRACE, 1);
			String traceURI = resourceURIsByType.get(KtbsConstants.STORED_TRACE).iterator().next();
			Model filteredModel = filterModel(model, new ResourceSelector(traceURI));
			resource = new KtbsJenaStoredTrace(traceURI, filteredModel, this);
			putResource(resource);
		} else if(types.contains(KtbsConstants.COMPUTED_TRACE)) {
			checkAtLeastNumberOfElements(model, types, KtbsConstants.COMPUTED_TRACE, 1);
			String computedTraceURI = resourceURIsByType.get(KtbsConstants.COMPUTED_TRACE).iterator().next();
			Model filteredModel = filterModel(model, new ResourceSelector(computedTraceURI));
			resource = new KtbsJenaComputedTrace(computedTraceURI, filteredModel, this);
			putResource(resource);
		} else if(types.contains(KtbsConstants.METHOD)) {
			checkAtLeastNumberOfElements(model, types, KtbsConstants.METHOD, 1);
			String computedTraceURI = resourceURIsByType.get(KtbsConstants.METHOD).iterator().next();
			Model filteredModel = filterModel(model, new ResourceSelector(computedTraceURI));
			resource = new KtbsJenaMethod(computedTraceURI, filteredModel, this);
			putResource(resource);
		} else if(types.contains(KtbsConstants.TRACE_MODEL) || containsOnly(types, KtbsConstants.ATTRIBUTE_TYPE, KtbsConstants.OBSEL_TYPE, KtbsConstants.RELATION_TYPE)) {
			checkAtLeastNumberOfElements(model, types, KtbsConstants.TRACE_MODEL, 1);

			String traceModelURI = null;
			// get the trace model URI
			Iterator<String> iterator = resourceURIsByType.get(KtbsConstants.TRACE_MODEL).iterator();
			if(iterator.hasNext())
				traceModelURI = iterator.next();
			else {
				iterator = resourceURIsByType.get(KtbsConstants.OBSEL_TYPE).iterator();
				if(iterator.hasNext())
					traceModelURI = KtbsUtils.resolveParentURI(iterator.next());
				else{
					iterator = resourceURIsByType.get(KtbsConstants.ATTRIBUTE_TYPE).iterator();
					if(iterator.hasNext())
						traceModelURI = KtbsUtils.resolveParentURI(iterator.next());
					else{
						iterator = resourceURIsByType.get(KtbsConstants.RELATION_TYPE).iterator();
						if(iterator.hasNext())
							traceModelURI = KtbsUtils.resolveParentURI(iterator.next());
					}
				}
			}
			if(traceModelURI == null) 
				throw new ResourceLoadException(
						"Could not find nor any trace model URI nor any resource in the trace model.", 
						JenaUtils.toTurtleString(model));

			Model filteredModel = filterModel(model, new ResourceSelector(traceModelURI));
			resource = new KtbsJenaTraceModel(traceModelURI, filteredModel, this);

			resources.put(resource.getURI(), resource);
			// put model and its children manually
			for(String attTypeURI:resourceURIsByType.get(KtbsConstants.ATTRIBUTE_TYPE)) {
				KtbsJenaAttributeType att = new KtbsJenaAttributeType(attTypeURI, filteredModel, this);
				resources.put(attTypeURI, att);
			}
			for(String obselTypeURI:resourceURIsByType.get(KtbsConstants.OBSEL_TYPE)) {
				KtbsJenaObselType obselType = new KtbsJenaObselType(obselTypeURI, filteredModel, this);
				resources.put(obselTypeURI, obselType);
			}
			for(String relTypeURI:resourceURIsByType.get(KtbsConstants.RELATION_TYPE)) {
				KtbsJenaRelationType rel = new KtbsJenaRelationType(relTypeURI, filteredModel, this);
				resources.put(relTypeURI, rel);
			}
			
		} else {
			// is this an obsel ?
			it = model.listStatements(
					null, 
					model.getProperty(KtbsConstants.P_HAS_TRACE), 
					(RDFNode)null);			
			if(!it.hasNext()) 
				// this is not an obsel
				throw new ResourceLoadException(
						"Impossible to find the type of resource contained in the stream.", 
						JenaUtils.toTurtleString(model));

			// checks if they are all obsels in the same trace
			Collection<String> obselURIs = new HashSet<String>();
			boolean sameTrace = true;
			String traceURI = null;

			while (it.hasNext()) {
				Statement statement = (Statement) it.next();
				String objectURI = statement.getObject().asResource().getURI();
				if(traceURI != null && !traceURI.equals(objectURI)) {
					// two different traces
					sameTrace = false;
					break;
				} else {
					traceURI = objectURI;
					obselURIs.add(statement.getResource().getURI());
				}
			}

			if(!sameTrace) 
				throw new ResourceLoadException(
						"Loading failed. Invalid state : there is more than one trace in the stream.", 
						JenaUtils.toTurtleString(model));
			if(traceURI == null) 
				throw new ResourceLoadException(
						"Impossible to find the type of resource contained in the stream.", 
						JenaUtils.toTurtleString(model));

			Model filteredModel = filterModel(model, new ResourceSelector(obselURIs));

			//add the obsels to the repository
			KtbsJenaTrace trace = (KtbsJenaTrace) resources.get(traceURI);

			// merge to the existing model
			trace.rdfModel.union(filteredModel);
			for(String obselURI:obselURIs) 
				resources.put(obselURI, new KtbsJenaObsel(obselURI, trace.rdfModel, this));

			resource = trace;
		}

		return resource;
	}

	/*
	 * Find if all elements in a set are equal to any of the parameters
	 */
	private boolean containsOnly(Collection<String> types, String... parameters) {
		Collection<String> c = Arrays.asList(parameters);
		Collection<String> c2 = new LinkedList<String>(types);
		c2.removeAll(c);
		return c2.size()==0;
	}

	private void checkAtLeastNumberOfElements(Model model, Multiset<String> multiset, String typeURI, int expectedNumber) throws MultipleResourcesInStreamException {
		int actualNumber = multiset.count(typeURI);
		if(actualNumber > expectedNumber) {
			throw new MultipleResourcesInStreamException(
					JenaUtils.toTurtleString(model), 
					typeURI, 
					expectedNumber, 
					actualNumber);
		}
	}

	/**
	 * Creates a new model with only the statement selected by a selector.
	 * 
	 * @param model
	 * @param selector
	 * @return
	 */
	private Model filterModel(Model model, Selector selector) {
		Model filteredModel = ModelFactory.createDefaultModel();
		filteredModel.add(model);

		StmtIterator it = model.listStatements(selector);
		while (it.hasNext()) 
			it.removeNext();

		return filteredModel;
	}

	private class ResourceSelector extends SimpleSelector {

		private Collection<String> selectedURIS;

		public ResourceSelector(String selectedUIR) {
			super();
			this.selectedURIS = new HashSet<String>();
			this.selectedURIS.add(selectedUIR);
		}

		public ResourceSelector(Collection<String> selectedURIs) {
			super();
			this.selectedURIS = selectedURIs;
		}

		@Override
		public boolean selects(Statement s) {
			String uri = s.getSubject().getURI();
			if(uri != null && selectedURIS.contains(uri))
				return true;
			else if(s.getObject().isResource() && selectedURIS.contains(s.getObject().asResource().getURI())) 
				return true;
			else if(s.getObject().isLiteral() && selectedURIS.contains(s.getObject().asLiteral().getValue())) 
				return true;
			return false;
		}
	}

	//	private void addResourceToParent(KtbsResource resource) {
	//		String parentURI = KtbsUtils.resolveParentURI(resource.getURI());
	//		if(exists(parentURI)) {
	//			// the resource will be added to the map by the parent resource
	//			if (resource instanceof Base) 
	//				((KtbsRoot)getResource(parentURI)).addBase((Base)resource);
	//			else if (resource instanceof StoredTrace) 
	//				((Base)getResource(parentURI)).addStoredTrace((StoredTrace)resource);
	//			else if (resource instanceof ComputedTrace) 
	//				((Base)getResource(parentURI)).addComputedTrace((ComputedTrace)resource);
	//			else if (resource instanceof Method) 
	//				((Base)getResource(parentURI)).addMethod((Method)resource);
	//			else if (resource instanceof TraceModel)
	//				((Base)getResource(parentURI)).addTraceModel((TraceModel)resource);
	//		} 
	//	}


	//	private  void putResourceAndChildren(KtbsJenaResource resource) {
	//		resources.put(resource.getURI(), resource);
	//		if(TraceModel.class.isAssignableFrom(resource.getClass())) {
	//			TraceModel tm = (TraceModel) resource;
	//			//add all the attribute types
	//			for(AttributeType type : KtbsUtils.toIterable(tm.listAttributeTypes()))
	//				resources.put(type.getURI(), (KtbsJenaResource) type);
	//
	//			//add all the relation types
	//			for(RelationType type : KtbsUtils.toIterable(tm.listRelationTypes()))
	//				resources.put(type.getURI(), (KtbsJenaResource) type);
	//
	//			//add all the obsel types
	//			for(ObselType type : KtbsUtils.toIterable(tm.listObselTypes()))
	//				resources.put(type.getURI(), (KtbsJenaResource) type);
	//		} else if (Trace.class.isAssignableFrom(resource.getClass())) {
	//			Trace trace = (Trace) resource;
	//			//	add all obsels
	//			for(Obsel obsel: KtbsUtils.toIterable(trace.listObsels()))
	//				resources.put(obsel.getURI(), (KtbsJenaResource) obsel);
	//		}
	//	}

//	@Override
//	public <T extends KtbsResource> T getResourceAlreadyInModel(String uri,
//			Class<T> clazz, Model rdfModel) {
//		if(!exists(uri))
//			resources.put(uri, (KtbsJenaResource) factory.createResource(uri, clazz, rdfModel));
//
//		return clazz.cast(resources.get(uri));
//	}

	//	private void addNewResource(KtbsJenaResource resource, Class<?> clazz, boolean autoUpdate) {
	//		putResourceAndChildren((KtbsJenaResource) resource);
	//
	//		if(autoUpdate) {
	//
	//			/*
	//			 * In case the resource is of type : Base, TraceModel, StoredTrace, 
	//			 * ComputedTrace or Method
	//			 * 
	//			 * Create the parent connection
	//			 * 
	//			 */
	//			addResourceToParent(resource);
	//
	//			/*
	//			 * In case the resource is of type Obsel, AttributeType, RelationType, or ObselType
	//			 * 
	//			 * Merge the Jena model and set the same model to the child
	//			 * 
	//			 */
	//			KtbsJenaResource parentResource = null;
	//			if(Obsel.class.isAssignableFrom(clazz)) {
	//				// Adds the obsel to the existing model
	//				Object[] propertyValues = ((Obsel)resource).getPropertyValues(KtbsConstants.P_HAS_TRACE);
	//				if(propertyValues != null && propertyValues.length != 1)
	//					throw new IllegalStateException("No trace defined for the obsel \""+uri+"\".");
	//				String traceURI = (String) propertyValues[0];
	//				parentResource = (KtbsJenaResource)getAfterCheck(traceURI, Trace.class);
	//			} else if(AttributeType.class.isAssignableFrom(clazz)
	//					|| RelationType.class.isAssignableFrom(clazz)
	//					|| ObselType.class.isAssignableFrom(clazz)) {
	//				String traceModelURI = KtbsUtils.resolveParentURI(uri);
	//				parentResource = (KtbsJenaResource)getAfterCheck(traceModelURI, TraceModel.class);
	//			}
	//
	//			putResourceAndChildren((KtbsJenaResource) resource);
	//		}
	//
	//		if(parentResource != null) {
	//			/*
	//			 * Case 1 : resource --> Obsel and parentResource --> Trace
	//			 * Case 2 : resource --> (AttributeType or ObselType or RelationType) and parentResource --> TraceModel
	//			 */
	//			// merge Jena models and override the child's model
	//			parentResource.incorporateResource((KtbsJenaResource) resource);
	//		}
	//	}

	//	@Override
	private <T extends KtbsResource> T putResource(T resource) {
		Class<? extends KtbsResource> cls = resource.getClass();
		if(isLeafType(cls)) 
			throw new UnsupportedOperationException("Cannot put an obsel, an obsel type, " +
					"an attribute type, or a relation type. This operation should be performed " +
			"from the Trace or the Trace Model.");
		else if(isStandaloneResource(cls) || Method.class.isAssignableFrom(cls)){
			resources.put(resource.getURI(), (KtbsJenaResource) resource);
		} else {
			/*
			 * Trace, StoredTrace, ComputedTrace.
			 */

			// remove the resource and all its children
			removeResource(resource);

			// adds the resource and all its children
			resources.put(resource.getURI(), (KtbsJenaResource) resource);
			for(KtbsResource child:getResourceChildren(resource))
				resources.put(child.getURI(), (KtbsJenaResource) child);
		}
		//		addNewResource((KtbsJenaResource) resource, cls, true);
		return resource;
	}


	//	@Override
	private  <T extends KtbsResource> void removeResource(T resource) {
		if(exists(resource.getURI())) {
			if(isLeafType(resource.getClass()))
				throw new UnsupportedOperationException("Cannot remove an obsel, an obsel type, " +
						"an attribute type, or a relation type. This operation should be performed " +
				"from the Trace or the Trace Model.");
			else if(isTypeWithChildren(resource.getClass())) {
				for(KtbsResource child:getResourceChildren(resource)) {
					resources.remove(child.getURI());
				}
				resources.remove(resource.getURI());
			} else {
				// Resource is Base, or Root, or Method
				resources.remove(resource.getURI());
			}
		}
	}

	private static Collection<KtbsResource> getResourceChildren(KtbsResource resource) {
		Collection<KtbsResource> c = new LinkedList<KtbsResource>();
		if(! isTypeWithChildren(resource.getClass()))
			return c;
		else {
			if(Trace.class.isAssignableFrom(resource.getClass())) {
				addResourcesToCollection(((Trace)resource).listObsels(), c);
			} else if(TraceModel.class.isAssignableFrom(resource.getClass())) {
				addResourcesToCollection(((TraceModel)resource).listObselTypes(), c);
				addResourcesToCollection(((TraceModel)resource).listAttributeTypes(), c);
				addResourcesToCollection(((TraceModel)resource).listRelationTypes(), c);
			}
		}
		return c;

	}

	private static void addResourcesToCollection(Iterator<? extends KtbsResource> it,
			Collection<KtbsResource> c) {
		while (it.hasNext()) {
			try {
				c.add(it.next());
			} catch(KtbsResourceNotFoundException e) {
				// do nothing
			}
		}
	}

	/*
	 * A standalone resource is a resource that has no reference 
	 * to any parent resource.
	 */
	private static boolean isStandaloneResource(Class<? extends KtbsResource> cls) {
		return Base.class.isAssignableFrom(cls)
		|| KtbsRoot.class.isAssignableFrom(cls);
	}

	private static boolean isTypeWithChildren(Class<? extends KtbsResource> cls) {
		return Trace.class.isAssignableFrom(cls)
		|| TraceModel.class.isAssignableFrom(cls);
	}


	//	private static boolean isTraceModelElementType(Class<? extends KtbsResource> cls) {
	//		return AttributeType.class.isAssignableFrom(cls)
	//		|| RelationType.class.isAssignableFrom(cls)
	//		|| ObselType.class.isAssignableFrom(cls);
	//	}

	private static boolean isLeafType(Class<? extends KtbsResource> cls) {
		return Obsel.class.isAssignableFrom(cls)
		|| AttributeType.class.isAssignableFrom(cls)
		|| RelationType.class.isAssignableFrom(cls)
		|| ObselType.class.isAssignableFrom(cls);
	}

//	@Override
//	public void addResourceAsPartOfExistingModel(KtbsResource resource, Model rdfModel) {
//		if (resource instanceof KtbsJenaResource) {
//			KtbsJenaResource kjResource = (KtbsJenaResource) resource;
//			rdfModel.add(kjResource.getJenaModel());
//			resources.put(kjResource.getURI(), kjResource);
//		} else
//			throw new UnsupportedOperationException("Must be a KTBS jena obsel");
//	}

	@Override
	public  <T extends KtbsResource> boolean existsOfType(String uri, Class<T> class1) {
		KtbsResource r = resources.get(uri);
		return (r!=null) && class1.isAssignableFrom(r.getClass());
	}

	@Override
	public <T extends KtbsResource> T getResource(String uri, Class<T> clazz) {
		checkExistency(uri);
		KtbsJenaResource r = resources.get(uri);
		if(!clazz.isAssignableFrom(r.getClass()))
			throw new KtbsResourceNotFoundException("The resource \""+uri+"\" is not of type " + clazz + ", but actually "+r.getClass()+"\".");
		return clazz.cast(r);
	}

	@Override
	public KtbsResource getResource(String uri) {
		return getResource(uri, KtbsResource.class);
	}
//
//	@Override
//	public KtbsResource getResource(String uri) {
//		return getResource(uri);
//	}

	@Override
	public Base createBase(String baseURI) {
		Model rdfModel = ModelFactory.createDefaultModel();
		rdfModel.getResource(baseURI).addProperty(
				RDF.type, 
				rdfModel.getResource(KtbsConstants.BASE));
		KtbsJenaBase base = new KtbsJenaBase(baseURI, rdfModel, this);
		putResource(base);
		return base;
	}

	@Override
	public TraceModel createTraceModel(Base base, String modelURI) {
		checkExistency(base);
		Model tmRdfModel = createBaseChild(base, modelURI, KtbsConstants.TRACE_MODEL);

		KtbsJenaTraceModel tm = new KtbsJenaTraceModel(modelURI, tmRdfModel, this);
		putResource(tm);
		return tm;
	}

	@Override
	public Method createMethod(Base base, String methodURI, String inherits) {
		checkExistency(base);

		Model methodRdfModel = createBaseChild(base, methodURI, KtbsConstants.METHOD);

		KtbsJenaMethod method = new KtbsJenaMethod(methodURI, methodRdfModel, this);
		method.setInherits(inherits);

		putResource(method);

		return method;
	}

	@Override
	public StoredTrace createStoredTrace(Base base, String traceURI, TraceModel tm) {
		checkExistency(tm, base);

		Model stRdfModel = createBaseChild(base, traceURI, KtbsConstants.STORED_TRACE);

		stRdfModel.getResource(traceURI).addProperty(
				stRdfModel.getProperty(KtbsConstants.P_HAS_MODEL),
				stRdfModel.getResource(tm.getURI())
		);

		KtbsJenaStoredTrace st = new KtbsJenaStoredTrace(traceURI, stRdfModel, this);
		putResource(st);
		return st;
	}

	@Override
	public ComputedTrace createComputedTrace(Base base, String traceURI, TraceModel tm, Method m, Collection<Trace> sources) {
		checkExistency(m, tm, base);
		for(Trace trace:sources) 
			checkExistency(trace);

		Model computedTraceRdfModel = createBaseChild(base, traceURI, KtbsConstants.COMPUTED_TRACE);

		computedTraceRdfModel.getResource(traceURI).addProperty(
				computedTraceRdfModel.getProperty(KtbsConstants.P_HAS_MODEL),
				computedTraceRdfModel.getResource(tm.getURI())
		);

		computedTraceRdfModel.getResource(traceURI).addProperty(
				computedTraceRdfModel.getProperty(KtbsConstants.P_HAS_METHOD),
				computedTraceRdfModel.getResource(m.getURI())
		);

		for(Trace trace:sources) {
			computedTraceRdfModel.getResource(traceURI).addProperty(
					computedTraceRdfModel.getProperty(KtbsConstants.P_HAS_SOURCE),
					computedTraceRdfModel.getResource(trace.getURI())
			);
		}

		KtbsJenaComputedTrace ct = new KtbsJenaComputedTrace (traceURI, computedTraceRdfModel, this);
		putResource(ct);
		return ct;
	}

	private Model createBaseChild(Base base, String childURI, String childRdfType) {
		checkExistency(base);
		Model baseRdfModel = ((KtbsJenaBase)base).rdfModel;
		Model childRdfModel = ModelFactory.createDefaultModel();
		
		baseRdfModel.getResource(childURI).addProperty(
				RDF.type, 
				baseRdfModel.getResource(childRdfType));

		childRdfModel.getResource(childURI).addProperty(
				RDF.type, 
				childRdfModel.getResource(childRdfType));

		createParentConnection(baseRdfModel, childRdfModel, base.getURI(), childURI);
		return childRdfModel;
	}

	private void createParentConnection(Model baseRdfModel, Model childRdfModel, String baseUri, String childURI) {
		baseRdfModel.getResource(baseUri).addProperty(
				baseRdfModel.getProperty(KtbsConstants.P_OWNS),
				baseRdfModel.getResource(childURI)
		);

		childRdfModel.getResource(baseUri).addProperty(
				childRdfModel.getProperty(KtbsConstants.P_OWNS),
				childRdfModel.getResource(childURI)
		);
	}

	@Override
	public Obsel createObsel(StoredTrace trace, String obselURI,
			ObselType type, Map<AttributeType, Object> attributes) {
		checkExistency(trace, type);
		AttributeType[] attributeTypes = (attributes==null)?new AttributeType[0]:attributes.keySet().toArray(new AttributeType[attributes.size()]);
		checkExistency(attributeTypes);

		Model traceRdfModel = ((KtbsJenaStoredTrace)trace).rdfModel;

		KtbsJenaObsel obsel = new KtbsJenaObsel(obselURI, traceRdfModel, this);

		traceRdfModel.getResource(obselURI).addProperty(
				traceRdfModel.getProperty(KtbsConstants.P_HAS_TRACE), 
				traceRdfModel.getResource(trace.getURI()));
		traceRdfModel.getResource(obselURI).addProperty(
				RDF.type, 
				traceRdfModel.getResource(type.getURI()));
		if(attributes!=null) {
			for(AttributeType att:attributes.keySet())
				obsel.addAttribute(att, attributes.get(att));
		}
		return obsel;
	}

	@Override
	public ObselType createObselType(TraceModel traceModel, String localName) {
		checkExistency(traceModel);

		Model rdfModel = ((KtbsJenaTraceModel)traceModel).rdfModel;

		KtbsJenaObselType obselType = new KtbsJenaObselType(traceModel.getURI()+localName, rdfModel, this);

		rdfModel.getResource(obselType.getURI()).addProperty(
				RDF.type, 
				rdfModel.getResource(KtbsConstants.OBSEL_TYPE));
		
		resources.put(obselType.getURI(), obselType);

		return obselType;
	}

	@Override
	public RelationType createRelationType(TraceModel traceModel, String localName,
			ObselType domain, ObselType range) {
		checkExistency(traceModel, range, domain);

		Model rdfModel = ((KtbsJenaTraceModel)traceModel).rdfModel;

		KtbsJenaRelationType relType = new KtbsJenaRelationType(traceModel.getURI()+localName, rdfModel, this);

		rdfModel.getResource(relType.getURI()).addProperty(
				RDF.type, 
				rdfModel.getResource(KtbsConstants.RELATION_TYPE));
		relType.setDomain(domain);
		relType.setRange(range);

		resources.put(relType.getURI(), relType);
		return relType;

	}

	@Override
	public AttributeType createAttributeType(TraceModel traceModel, String localName,
			ObselType domain) {
		checkExistency(traceModel, domain);

		Model rdfModel = ((KtbsJenaTraceModel)traceModel).rdfModel;

		KtbsJenaAttributeType attType = new KtbsJenaAttributeType(traceModel.getURI()+localName, rdfModel, this);

		rdfModel.getResource(attType.getURI()).addProperty(
				RDF.type, 
				rdfModel.getResource(KtbsConstants.ATTRIBUTE_TYPE));
		attType.setDomain(domain);

		resources.put(attType.getURI(), attType);
		return attType;
	}


	@Override
	public void removeObsel(StoredTrace trace, Obsel obsel) {
		if(!exists(trace.getURI()))
			throw new KtbsResourceNotFoundException(trace.getURI());
		if(!exists(obsel.getURI()))
			throw new KtbsResourceNotFoundException(obsel.getURI());

		KtbsJenaStoredTrace jenaTrace = (KtbsJenaStoredTrace)trace;
		Model rdfModel = jenaTrace.rdfModel;
		rdfModel.removeAll(rdfModel.getResource(obsel.getURI()), null, null);
		rdfModel.removeAll( null, null,rdfModel.getResource(obsel.getURI()));
		this.resources.remove(obsel.getURI());
	}

	@Override
	public void checkExistency(KtbsResource... resources) {
		for(KtbsResource resource:resources)
			checkExistency(resource.getURI());
	}

	@Override
	public void checkExistency(String... uris) {
		for(String uri:uris) {
			if(!exists(uri))
				throw new KtbsResourceNotFoundException(uri);
		}
	}
}
