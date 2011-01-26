package org.liris.ktbs.rdf.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class RdfBase extends RdfKtbsResource implements Base {

	RdfBase(String uri, Model rdfModel, ResourceRepository repo) {
		super(uri, rdfModel, repo);
	}

	@Override
	public KtbsResource get(String resourceURI) {
		KtbsResource resource = getTraceModel(resourceURI);
		if(resource != null)
			return resource;
		resource = getStoredTrace(resourceURI);
		if(resource != null)
			return resource;
		resource = getComputedTrace(resourceURI);
		if(resource != null)
			return resource;
		resource = getMethod(resourceURI);
		if(resource != null)
			return resource;
		return getTrace(resourceURI);
	}

	@Override
	public Iterator<TraceModel> listTraceModels() {
		return new OwnedResourceIterator<TraceModel>(KtbsConstants.TRACE_MODEL);
	}

	@Override
	public Iterator<StoredTrace> listStoredTraces() {
		return new OwnedResourceIterator<StoredTrace>(KtbsConstants.STORED_TRACE);
	}

	@Override
	public Iterator<ComputedTrace> listComputedTraces() {
		return new OwnedResourceIterator<ComputedTrace>(KtbsConstants.COMPUTED_TRACE);
	}

	@Override
	public Iterator<Method> listMethods() {
		return new OwnedResourceIterator<Method>(KtbsConstants.METHOD);
	}

	@Override
	public Iterator<Trace> listTraces() {
		return new OwnedResourceIterator<Trace>(
				KtbsConstants.STORED_TRACE, 
				KtbsConstants.COMPUTED_TRACE);
	}

	@Override
	public Iterator<KtbsResource> listResources() {
		return new OwnedResourceIterator<KtbsResource>(
				KtbsConstants.STORED_TRACE, 
				KtbsConstants.COMPUTED_TRACE,
				KtbsConstants.METHOD,
				KtbsConstants.TRACE_MODEL
		);
	}

	@Override
	public StoredTrace getStoredTrace(String stUri) {
		return (StoredTrace) returnOwnedResource(stUri, KtbsConstants.STORED_TRACE);
	}

	@Override
	public ComputedTrace getComputedTrace(String ctUri) {
		return (ComputedTrace) returnOwnedResource(ctUri, KtbsConstants.COMPUTED_TRACE);
	}

	@Override
	public Trace getTrace(String tUri) {
		return (Trace) returnOwnedResource(tUri, KtbsConstants.COMPUTED_TRACE, KtbsConstants.STORED_TRACE);
	}

	@Override
	public TraceModel getTraceModel(String tmUri) {
		return (TraceModel) returnOwnedResource(tmUri, KtbsConstants.TRACE_MODEL);
	}

	@Override
	public Method getMethod(String methodUri) {
		return (Method) returnOwnedResource(methodUri, KtbsConstants.METHOD);
	}

	private KtbsResource returnOwnedResource(String resourceUri, String resourceType, String... altAcceptedTypes) {
		Resource methodResource = requestRDFResource(
				resourceUri,
				uri,
				KtbsConstants.P_OWNS,
				RDF.type.getURI(),
				resourceType,
				altAcceptedTypes
		);
		if(methodResource == null)
			return null;
		else {
			Resource rdfType = rdfModel.getResource(resourceUri).getPropertyResourceValue(RDF.type);
			Class<? extends KtbsResource> javaClass = KtbsUtils.getJavaClass(rdfType.getURI());
			return repository.getResource(resourceUri, javaClass);
		}
	}

	/**
	 * res1 prop1 ?;
	 * ? prop2 obj2;
	 * @param altObj2Uris 
	 * 
	 * @param methodUri
	 * @return
	 */
	private Resource requestRDFResource(String resourceUri, String res1Uri, String prop1Uri, String prop2Uri, String obj2Uri, String... altObj2Uris) {
		Collection<String> acceptedObj2Uris = new HashSet<String>();
		acceptedObj2Uris.add(obj2Uri);
		for(String altObj2Uri:altObj2Uris)
			acceptedObj2Uris.add(altObj2Uri);

		StmtIterator stmtIt1 = rdfModel.listStatements(
				rdfModel.getResource(res1Uri),
				rdfModel.getProperty(prop1Uri),
				rdfModel.getResource(resourceUri)
		);
		if(stmtIt1.hasNext()) {
			Resource candidateResource = stmtIt1.next().getObject().asResource();
			StmtIterator stmtIt2 = rdfModel.listStatements(
					candidateResource,
					rdfModel.getProperty(prop2Uri),
					(RDFNode)null
			);
			if(stmtIt2.hasNext()) {
				Statement currentObj2 = stmtIt2.next();
				String currentObj2Uri2 = currentObj2.getObject().asResource().getURI();
				if(acceptedObj2Uris.contains(currentObj2Uri2)) 
					return candidateResource;
			}
		}
		return null;
	}

	private class OwnedResourceIterator<T extends KtbsResource> implements Iterator<T> {

		private Collection<String> acceptedTypeUris;

		private NodeIterator ownedResourceIt;

		private KtbsResource next;


		OwnedResourceIterator(String resourceTypeURI, String... otherAcceptedTypeURI) {
			super();
			ownedResourceIt = RdfBase.this.rdfModel.listObjectsOfProperty(
					RdfBase.this.rdfModel.getResource(RdfBase.this.uri), 
					RdfBase.this.rdfModel.getProperty(KtbsConstants.P_OWNS));

			this.acceptedTypeUris = new HashSet<String>();
			this.acceptedTypeUris.add(resourceTypeURI);
			for(String s:otherAcceptedTypeURI)
				this.acceptedTypeUris.add(s);
			doNext();
		}

		private void doNext() {
			boolean foundNext = false;
			while(ownedResourceIt.hasNext() && !foundNext) {
				RDFNode nextNode = ownedResourceIt.next();
				Resource candidateResource = nextNode.asResource();
				NodeIterator it2 = RdfBase.this.rdfModel.listObjectsOfProperty(
						candidateResource,
						RDF.type);

				if(it2.hasNext() && isAcceptedType(it2.next().asResource())) {
					foundNext = true;

					Resource r = candidateResource.getPropertyResourceValue(RDF.type);
					Class<? extends KtbsResource> javaClass = KtbsUtils.getJavaClass(r.getURI());

					next = repository.getResource(
							candidateResource.getURI(), 
							javaClass);
				} 
			} 
			if(!foundNext)
				next = null;
		}

		private boolean isAcceptedType(Resource resource) {
			for(String acceptedType:acceptedTypeUris) {
				if(resource.equals(RdfBase.this.rdfModel.getResource(acceptedType)))
					return true;
			}
			return false;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public T next() {
			@SuppressWarnings("unchecked")
			T toBeReturned = (T)next;
			doNext();
			return toBeReturned;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Cannot use iterator to remove resources from a base.");
		}
	}

	@Override
	public StoredTrace newStoredTrace(String traceURI, TraceModel model, String origin) {
		return repository.createStoredTrace(this, traceURI, model, origin);
	}

	@Override
	public ComputedTrace newComputedTrace(String traceURI, TraceModel model,
			Method method, Collection<Trace> sources) {
		return repository.createComputedTrace(this, traceURI, model, method, sources);
	}

	@Override
	public Method newMethod(String methodURI, String inheritedMethod) {
		return repository.createMethod(this, methodURI, inheritedMethod);
	}

	@Override
	public TraceModel newTraceModel(String modelURI) {
		return repository.createTraceModel(this, modelURI);
	}
}
