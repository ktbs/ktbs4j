package org.liris.ktbs.rdf.resource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.MultipleResourcesInStreamException;
import org.liris.ktbs.core.ResourceLoadException;
import org.liris.ktbs.core.ResourceNotFoundException;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.Root;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.rdf.JenaUtils;
import org.liris.ktbs.rdf.MinimalRdfResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class RdfResourceRepository implements ResourceRepository {

	private static final Log log = LogFactory.getLog(RdfResourceRepository.class);

	private Map<String, RdfKtbsResource> resources;
	private MinimalRdfResourceFactory minimalFac = MinimalRdfResourceFactory.getInstance();


	public RdfResourceRepository() {
		resources = new HashMap<String, RdfKtbsResource>();
	}

	@Override
	public boolean exists(String uri) {
		return resources.containsKey(uri);
	}

	@Override
	public void loadResource(
			InputStream stream, 
			String lang) throws ResourceLoadException {

		loadResourceFromReader(new InputStreamReader(stream), lang);

	}

	private void loadResourceFromReader(Reader reader, String lang)
	throws ResourceLoadException, MultipleResourcesInStreamException {
		boolean resourceFound = false;

		Model model = readModel(reader, lang);


		// try to find the type of the resource contained
		Multiset<String> types = HashMultiset.create();
		StmtIterator it = model.listStatements(null, RDF.type, (RDFNode)null);
		Multimap<String, String> resourceURIsByType = HashMultimap.create();
		while(it.hasNext()) {
			Statement stmt = it.next();
			String typeURI = stmt.getObject().asResource().getURI();
			types.add(typeURI);
			resourceURIsByType.put(typeURI, stmt.getSubject().getURI());
		}


		/*
		 * TODO KTBS bug : quand on fait un GET sur une trace, on n'a pas le rdf:type de la trace
		 * et on ne peut donc pas savoir qu'il s'agit d'une trace.
		 *
		 * 
		 */

		if(types.size()==0)
			throw new ResourceLoadException(
					"Impossible to find the type of resource contained in the stream.", 
					JenaUtils.toTurtleString(model));

		if(types.contains(KtbsConstants.METHOD)) {
			log.info("A method has been found in the stream.");
			//			checkAtLeastNumberOfElements(model, types, KtbsConstants.METHOD, 1);
			resourceFound |= putResourcesInModel(
					RdfMethod.class, 
					model, 
					resourceURIsByType.get(KtbsConstants.METHOD));

		} 

		if(types.elementSet().contains(KtbsConstants.ROOT)) {
			log.info("A root has been found in the stream.");
			resourceFound |= putResourcesInModel(
					RdfRoot.class, 
					model, 
					resourceURIsByType.get(KtbsConstants.ROOT));
		} 

		if(types.contains(KtbsConstants.BASE)) {
			log.info("A base has been found in the stream.");
			resourceFound |= putResourcesInModel(
					RdfBase.class, 
					model, 
					resourceURIsByType.get(KtbsConstants.BASE));
		} 

		if(types.contains(KtbsConstants.TRACE_MODEL) || containsOnly(types, KtbsConstants.ATTRIBUTE_TYPE, KtbsConstants.OBSEL_TYPE, KtbsConstants.RELATION_TYPE)) {
			resourceFound = loadTraceModel(model, resourceURIsByType, null);
		} 

		if(types.contains(KtbsConstants.COMPUTED_TRACE)) {
			log.info("A computed trace has been found in the stream.");

			resourceFound |= putResourcesInModel(
					RdfComputedTrace.class, 
					model, 
					resourceURIsByType.get(KtbsConstants.COMPUTED_TRACE));
		}

		if(types.contains(KtbsConstants.STORED_TRACE)) {
			log.info("A stored trace has been found in the stream.");


			resourceFound |= putResourcesInModel(
					RdfStoredTrace.class, 
					model, 
					resourceURIsByType.get(KtbsConstants.STORED_TRACE));
		}


		// are there obsels ?
		it = model.listStatements(
				null, 
				model.getProperty(KtbsConstants.P_HAS_TRACE), 
				(RDFNode)null);

		if(it.hasNext()){
			// checks if they are all obsels in the same trace
			log.info("Obsels are found in the stream.");

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
					obselURIs.add(statement.getSubject().getURI());
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

			Model filteredModel =  JenaUtils.filterModel(model, new ResourceSelector(obselURIs));

			//add the obsels to the repository
			RdfTrace trace = (RdfTrace) resources.get(traceURI);
			if(trace == null) {
				/*
				 * the trace does not exists
				 */
				String message = "There are obsels in the stream that belongs to an unknown trace.";
				log.warn(message);
				throw new ResourceLoadException(
						message, 
						JenaUtils.toTurtleString(model));

			} else {

				/*
				 *  merge to the existing model
				 *  The add method behaves as the union, but does not create 
				 *  a new model.
				 */
				trace.rdfModel.add(filteredModel);

				for(String obselURI:obselURIs) 
					resources.put(obselURI, new RdfObsel(obselURI, trace.rdfModel, this));

				resourceFound = true;
			}
		}

		if(!resourceFound)
			throw new ResourceLoadException("No resource could be found in the stream.", JenaUtils.toTurtleString(model));
	}

	private Model readModel(Reader reader, String lang) {
		Model model = ModelFactory.createDefaultModel();
		model.read(reader, "", lang);
		return model;
	}

	private boolean loadTraceModel(Model model, 
			Multimap<String, String> resourceURIsByType, String tmURI)
	throws ResourceLoadException {

		boolean resourceFound;
		log.info("Loading a trace model.");

		String traceModelURI = null;
		if(tmURI!=null)
			traceModelURI = tmURI;
		else {
			// try to find the trace model URI
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
		}

		if(traceModelURI == null) {
			throw new ResourceLoadException(
					"Impossible to find out the uri of the trace model to load in repository (empty trace model).", 
					JenaUtils.toTurtleString(model));
		}
		
		Model filteredModel = JenaUtils.filterModel(model, new TraceModelElementSelector(traceModelURI));

		RdfTraceModel resource = new RdfTraceModel(traceModelURI, filteredModel, this);
		resources.put(resource.getURI(), resource);
		resourceFound = true;

		// put model and its children manually
		for(String attTypeURI:resourceURIsByType.get(KtbsConstants.ATTRIBUTE_TYPE)) {
			RdfAttributeType att = new RdfAttributeType(attTypeURI, filteredModel, this);
			resources.put(attTypeURI, att);
		}
		for(String obselTypeURI:resourceURIsByType.get(KtbsConstants.OBSEL_TYPE)) {
			RdfObselType obselType = new RdfObselType(obselTypeURI, filteredModel, this);
			resources.put(obselTypeURI, obselType);
		}
		for(String relTypeURI:resourceURIsByType.get(KtbsConstants.RELATION_TYPE)) {
			RdfRelationType rel = new RdfRelationType(relTypeURI, filteredModel, this);
			resources.put(relTypeURI, rel);
		}
		return resourceFound;
	}

	private <T extends RdfKtbsResource> boolean putResourcesInModel(
			Class<T> cls,
			Model model, 
			Collection<String> resourceURIs) throws ResourceLoadException {

		boolean found = false;

		for(String resourceURI:resourceURIs) {
			Model filteredModel =  JenaUtils.filterModel(model, new ResourceSelector(resourceURI));

			try {
				Constructor<T> constructor = cls.getDeclaredConstructor(String.class, Model.class, ResourceRepository.class);
				RdfKtbsResource resource = constructor.newInstance(resourceURI, filteredModel, this);
				putResource(resource, false);
				found = true;
			} catch (Exception e) {
				String message = "Cannot create an instance of type " + cls + " from the stream";
				log.error(message, e);
				throw new ResourceLoadException(message, JenaUtils.toTurtleString(model));
			}

		}

		return found;
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

	private class TraceModelElementSelector extends SimpleSelector {

		private String traceModelURI;

		public TraceModelElementSelector(String traceModelURI) {
			super();
			this.traceModelURI = traceModelURI;
		}
		@Override
		public boolean selects(Statement s) {
			String subjectURI = s.getSubject().getURI();
			String objectURI = null;
			if(s.getObject().isResource())
				objectURI = s.getObject().asResource().getURI();
			if(s.getObject().isLiteral())
				objectURI = s.getObject().asLiteral().getValue().toString();

			if(subjectURI.startsWith(traceModelURI)
					|| (objectURI !=null && objectURI.startsWith(traceModelURI)))
				return true;
			else
				return false;
		}



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

	private <T extends KtbsResource> T putResource(T resource, boolean withChildren) {
		Class<? extends KtbsResource> cls = resource.getClass();
		if(isLeafType(cls)) 
			throw new UnsupportedOperationException("Cannot put an obsel, an obsel type, " +
					"an attribute type, or a relation type. This operation should be performed " +
			"from the Trace or the Trace Model.");
		else if(isStandaloneResource(cls) || Method.class.isAssignableFrom(cls)){
			resources.put(resource.getURI(), (RdfKtbsResource) resource);
		} else {
			/*
			 * Trace, StoredTrace, ComputedTrace.
			 */

			// remove the resource and all its children
			removeResource(resource);

			// adds the resource and all its children
			resources.put(resource.getURI(), (RdfKtbsResource) resource);
			if(withChildren) {
				for(KtbsResource child:getResourceChildren(resource))
					resources.put(child.getURI(), (RdfKtbsResource) child);
			}
		}
		return resource;
	}


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
			} catch(ResourceNotFoundException e) {
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
		|| Root.class.isAssignableFrom(cls);
	}

	private static boolean isTypeWithChildren(Class<? extends KtbsResource> cls) {
		return Trace.class.isAssignableFrom(cls)
		|| TraceModel.class.isAssignableFrom(cls);
	}

	private static boolean isLeafType(Class<? extends KtbsResource> cls) {
		return Obsel.class.isAssignableFrom(cls)
		|| AttributeType.class.isAssignableFrom(cls)
		|| RelationType.class.isAssignableFrom(cls)
		|| ObselType.class.isAssignableFrom(cls);
	}

	@Override
	public  <T extends KtbsResource> boolean existsOfType(String uri, Class<T> class1) {
		KtbsResource r = resources.get(uri);
		return (r!=null) && class1.isAssignableFrom(r.getClass());
	}

	@Override
	public <T extends KtbsResource> T getResource(String uri, Class<T> clazz) {
		checkExistency(uri);
		RdfKtbsResource r = resources.get(uri);
		if(!clazz.isAssignableFrom(r.getClass()))
			throw new ResourceNotFoundException("The resource \""+uri+"\" is not of type " + clazz + ", but actually "+r.getClass()+"\"." +
			" Maybe the resource is en empty trace model.");
		return clazz.cast(r);
	}

	@Override
	public KtbsResource getResource(String uri) {
		return getResource(uri, KtbsResource.class);
	}

	@Override
	public Base createBase(String baseURI) {
		Model rdfModel = ModelFactory.createDefaultModel();
		rdfModel.getResource(baseURI).addProperty(
				RDF.type, 
				rdfModel.getResource(KtbsConstants.BASE));
		RdfBase base = new RdfBase(baseURI, rdfModel, this);
		putResource(base, false);
		return base;
	}

	@Override
	public TraceModel createTraceModel(Base base, String modelURI) {
		checkExistency(base);

		return createTraceModel(base.getURI(), modelURI);
	}

	@Override
	public Method createMethod(Base base, String methodURI, String inherits) {
		checkExistency(base);

		return createMethod(base.getURI(), methodURI, inherits);
	}

	@Override
	public StoredTrace createStoredTrace(Base base, String traceURI, TraceModel tm, String origin) {
		checkExistency(tm, base);
		return createStoredTrace(base.getURI(), traceURI, tm.getURI(), origin);
	}

	@Override
	public ComputedTrace createComputedTrace(Base base, String traceURI, TraceModel tm, Method m, Collection<Trace> sources) {
		checkExistency(m, tm, base);
		for(Trace trace:sources) 
			checkExistency(trace);



		return createComputedTrace(base.getURI(), traceURI, tm.getURI(), m.getURI(), KtbsUtils.toUriCollection(sources));
	}

	private void createBaseChild(Model baseRdfModel, Model childRdfModel, String baseUri, String childURI) {
		baseRdfModel.getResource(baseUri).addProperty(
				baseRdfModel.getProperty(KtbsConstants.P_OWNS),
				baseRdfModel.getResource(childURI)
		);

		childRdfModel.getResource(baseUri).addProperty(
				childRdfModel.getProperty(KtbsConstants.P_OWNS),
				childRdfModel.getResource(childURI)
		);

		// Get the rdf:type of the child and add it to the base model
		Resource type = childRdfModel.getResource(childURI).getPropertyResourceValue(RDF.type);
		baseRdfModel.getResource(childURI).addProperty(
				RDF.type, 
				baseRdfModel.getResource(type.getURI()));
	}

	@Override
	public Obsel createObsel(StoredTrace trace, String obselURI,
			ObselType type, Map<AttributeType, Object> attributes) {
		checkExistency(trace, type);
		AttributeType[] attributeTypes = (attributes==null)?new AttributeType[0]:attributes.keySet().toArray(new AttributeType[attributes.size()]);
		checkExistency(attributeTypes);

		return createObsel(trace.getURI(), obselURI, type.getURI(), KtbsUtils.toUriMap(attributes));
	}

	@Override
	public ObselType createObselType(TraceModel traceModel, String localName) {
		checkExistency(traceModel);

		return createObselType(traceModel.getURI(), localName);
	}

	@Override
	public RelationType createRelationType(TraceModel traceModel, String localName,
			ObselType domain, ObselType range) {
		checkExistency(traceModel);
		if(domain!=null)
			checkExistency(domain);
		if(range!=null)
			checkExistency(range);


		return createRelationType(traceModel.getURI(), localName, domain.getURI(), range.getURI());

	}

	@Override
	public AttributeType createAttributeType(TraceModel traceModel, String localName,
			ObselType domain) {
		checkExistency(traceModel);
		if(domain!=null)
			checkExistency(domain);


		return createAttributeType(traceModel.getURI(), localName, domain.getURI());
	}


	@Override
	public void removeObsel(StoredTrace trace, Obsel obsel) {
		if(!exists(trace.getURI()))
			throw new ResourceNotFoundException(trace.getURI());
		if(!exists(obsel.getURI()))
			throw new ResourceNotFoundException(obsel.getURI());

		RdfStoredTrace jenaTrace = (RdfStoredTrace)trace;
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

	private static final Collection<String> builtinResourcesURI = new HashSet<String>();

	static {
		for(KtbsResource r:ResourceRepository.BUILTIN_RESOURCES)
			builtinResourcesURI.add(r.getURI());
	}

	@Override
	public void checkExistency(String... uris) {
		for(String uri:uris) {
			if(!exists(uri) && !builtinResourcesURI.contains(uri))
				throw new ResourceNotFoundException(uri);
		}
	}

	@Override
	public ComputedTrace createComputedTrace(String baseUri, String traceUri,
			String traceModelUri, String methodUri,
			Collection<String> sourceUris) {

		Resource r = minimalFac.createComputedTraceModel(
				baseUri, 
				traceUri, 
				methodUri, 
				sourceUris);
		Model computedTraceRdfModel = r.getModel();
		computedTraceRdfModel.getResource(traceUri).addProperty(
				computedTraceRdfModel.getProperty(KtbsConstants.P_HAS_MODEL),
				computedTraceRdfModel.getResource(traceModelUri)
		);

		RdfComputedTrace ct = new RdfComputedTrace(traceUri, computedTraceRdfModel, this);
		putResource(ct, false);

		if(exists(baseUri)) {
			createBaseChild(
					((RdfBase)resources.get(baseUri)).rdfModel, 
					computedTraceRdfModel, 
					baseUri, 
					traceUri);
		}
		return ct;
	}

	@Override
	public StoredTrace createStoredTrace(String baseUri, String traceUri,
			String traceModelUri, String origin) {
		Resource r = minimalFac.createStoredTraceModel(baseUri, traceUri, traceModelUri, origin);
		Model stRdfModel = r.getModel();
		RdfStoredTrace st = new RdfStoredTrace(traceUri, stRdfModel, this);
		putResource(st, false);
		if(exists(baseUri)) {
			createBaseChild(
					((RdfBase)resources.get(baseUri)).rdfModel, 
					stRdfModel, 
					baseUri, 
					traceUri);
		}

		return st;
	}

	@Override
	public Method createMethod(String baseUri, String methodUri, String inherits) {
		Resource r = minimalFac.createMethodModel(baseUri, methodUri, inherits, null);

		Model methodRdfModel = r.getModel();
		RdfMethod method = new RdfMethod(methodUri, methodRdfModel, this);
		putResource(method, false);
		if(exists(baseUri)) {
			createBaseChild(
					((RdfBase)resources.get(baseUri)).rdfModel, 
					methodRdfModel, 
					baseUri, 
					methodUri);
		}

		return method;
	}

	@Override
	public TraceModel createTraceModel(String baseUri, String modelUri) {
		Resource r = minimalFac.createTraceModelModel(baseUri, modelUri);

		Model tmRdfModel = r.getModel();

		RdfTraceModel tm = new RdfTraceModel(modelUri, tmRdfModel, this);
		putResource(tm, false);

		if(exists(baseUri)) {
			createBaseChild(
					((RdfBase)resources.get(baseUri)).rdfModel, 
					tmRdfModel, 
					baseUri, 
					modelUri);
		}

		return tm;
	}

	@Override
	public Obsel createObsel(String traceUri, String obselUri, String typeUri,
			Map<String, Object> attributes) {
		Resource r = minimalFac.createObselModel(traceUri, obselUri, typeUri, null, null, null, null, null, attributes);

		Model traceRdfModel = r.getModel();

		Model sharedModelForTrace = mergeWithParentModel(traceUri, traceRdfModel);
		RdfObsel obsel = new RdfObsel(obselUri, sharedModelForTrace, this);

		resources.put(obsel.getURI(), obsel);

		return obsel;
	}

	private Model mergeWithParentModel(String parentUri, Model elementModel) {
		if(exists(parentUri)) {
			RdfKtbsResource rdfKtbsResource = resources.get(parentUri);
			rdfKtbsResource.rdfModel.add(elementModel);
			return rdfKtbsResource.rdfModel;
		} else
			return elementModel;
	}

	@Override
	public ObselType createObselType(String traceModelUri, String localName) {
		String obsTypeUri = getModelElementUri(traceModelUri, localName);
		Resource r = minimalFac.createObselTypeModel(obsTypeUri, null);
		Model rdfModel  = r.getModel();
		Model model = mergeWithParentModel(traceModelUri, rdfModel);

		RdfObselType obselType = new RdfObselType(obsTypeUri, model, this);

		resources.put(obselType.getURI(), obselType);
		return obselType;
	}

	private String getModelElementUri(String traceModelUri, String localName) {
		if(localName == null)
			// anonymous resource
			return null;
		else 
			return traceModelUri+localName;
	}


	@Override
	public RelationType createRelationType(String traceModelUri,
			String localName, String domainUri, String rangeUri) {
		String relUri = getModelElementUri(traceModelUri, localName);

		Resource r = minimalFac.createRelationTypeModel(relUri, null, domainUri, rangeUri);
		Model rdfModel = r.getModel();
		Model model = mergeWithParentModel(traceModelUri, rdfModel);

		RdfRelationType relType = new RdfRelationType(relUri, model, this);

		resources.put(relType.getURI(), relType);
		return relType;
	}

	@Override
	public AttributeType createAttributeType(String traceModelUri,
			String localName, String domainUri) {
		String attUri = getModelElementUri(traceModelUri, localName);

		Resource r = minimalFac.createAttributeTypeModel(attUri, domainUri);

		Model rdfModel = r.getModel();


		Model model = mergeWithParentModel(traceModelUri, rdfModel);
		RdfAttributeType attType = new RdfAttributeType(attUri, model, this);

		resources.put(attType.getURI(), attType);
		return attType;
	}

	@Override
	public void loadResource(String stringRepresentation, String lang)
	throws ResourceLoadException {
		StringReader reader = new StringReader(new String(stringRepresentation));
		loadResourceFromReader(reader, lang);
	}

	@Override
	public void loadTraceModelResource(String traceModelUri, String stringRepresentation, String lang)
	throws ResourceLoadException {
		
		Model model = readModel(new StringReader(stringRepresentation), lang);
		StmtIterator it = model.listStatements(null, RDF.type, (RDFNode)null);
		Multimap<String, String> resourceURIsByType = HashMultimap.create();
		while(it.hasNext()) {
			Statement stmt = it.next();
			String typeURI = stmt.getObject().asResource().getURI();
			resourceURIsByType.put(typeURI, stmt.getSubject().getURI());
		}
		
		loadTraceModel(
				model,
				resourceURIsByType, 
				traceModelUri);
	}
}
