package org.liris.ktbs.rdf.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class KtbsJenaBase extends KtbsJenaResource implements Base {

	KtbsJenaBase(String uri, Model rdfModel, KtbsResourceHolder holder) {
		super(uri, rdfModel, holder);
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
	public KtbsRoot getKtbsRoot() {
		/*
		 * TODO KTBS BUG : the triple "root ktbs:hasBase base" is not returned by the KTBS
		 */
		throw new UnsupportedOperationException("The KTBS server does not provide the root uri at the moment.");
//		ResIterator it = rdfModel.listResourcesWithProperty(
//				rdfModel.getProperty(KtbsConstants.P_OWNS), 
//				rdfModel.getResource(uri));
//		if(it.hasNext())
//			return EmptyResourceFactory.getInstance().createKtbsRoot(it.next().getURI());
//		return null;
	}

	@Override
	public Iterator<TraceModel> listTraceModels() {
		return new OwnedResourceIterator<TraceModel>(KtbsConstants.TRACE_MODEL, TraceModel.class);
	}

	@Override
	public Iterator<StoredTrace> listStoredTraces() {
		return new OwnedResourceIterator<StoredTrace>(KtbsConstants.STORED_TRACE, StoredTrace.class);
	}

	@Override
	public Iterator<ComputedTrace> listComputedTraces() {
		return new OwnedResourceIterator<ComputedTrace>(KtbsConstants.COMPUTED_TRACE, ComputedTrace.class);
	}

	@Override
	public Iterator<Method> listMethods() {
		return new OwnedResourceIterator<Method>(KtbsConstants.METHOD, Method.class);
	}

	@Override
	public Iterator<Trace> listTraces() {
		return new OwnedResourceIterator<Trace>(KtbsConstants.STORED_TRACE, Trace.class, KtbsConstants.COMPUTED_TRACE);
	}

	@Override
	public Iterator<KtbsResource> listResources() {
		return new OwnedResourceIterator<KtbsResource>(
				KtbsConstants.STORED_TRACE, 
				KtbsResource.class, 
				KtbsConstants.COMPUTED_TRACE,
				KtbsConstants.METHOD,
				KtbsConstants.TRACE_MODEL
		);
	}

	@Override
	public void addStoredTrace(StoredTrace trace) {
		createParentConnection(this, trace);
		throw new ReadOnlyObjectException(this);
	}

	@Override
	public StoredTrace getStoredTrace(String stUri) {
		return returnOwnedResource(stUri, KtbsConstants.STORED_TRACE, StoredTrace.class);
	}

	@Override
	public void addComputedTrace(ComputedTrace trace) {
		createParentConnection(this, trace);
	}
	
	@Override
	public ComputedTrace getComputedTrace(String ctUri) {
		return returnOwnedResource(ctUri, KtbsConstants.COMPUTED_TRACE, ComputedTrace.class);
	}

	@Override
	public Trace getTrace(String tUri) {
		return returnOwnedResource(tUri, KtbsConstants.COMPUTED_TRACE, Trace.class, KtbsConstants.STORED_TRACE);
	}

	@Override
	public void addTraceModel(TraceModel traceModel) {
		createParentConnection(this, traceModel);
	}

	@Override
	public TraceModel getTraceModel(String tmUri) {
		return returnOwnedResource(tmUri, KtbsConstants.TRACE_MODEL, TraceModel.class);
	}

	@Override
	public void addMethod(Method method) {
		createParentConnection(this, method);
	}

	@Override
	public Method getMethod(String methodUri) {
		return returnOwnedResource(methodUri, KtbsConstants.METHOD, Method.class);
	}

	private <T extends KtbsResource> T returnOwnedResource(String resourceUri, String resourceType, Class<T> resourceClass, String... altAcceptedTypes) {
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
		else
			return holder.getResource(resourceUri, resourceClass);
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

		private Class<T> resourceClass;
		private Collection<String> acceptedTypeUris;

		private NodeIterator ownedResourceIt;

		private T next;


		OwnedResourceIterator(String resourceTypeURI, Class<T> resourceClass, String... otherAcceptedTypeURI) {
			super();
			this.resourceClass = resourceClass;
			ownedResourceIt = KtbsJenaBase.this.rdfModel.listObjectsOfProperty(
					KtbsJenaBase.this.rdfModel.getResource(KtbsJenaBase.this.uri), 
					KtbsJenaBase.this.rdfModel.getProperty(KtbsConstants.P_OWNS));

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
				NodeIterator it2 = KtbsJenaBase.this.rdfModel.listObjectsOfProperty(
						candidateResource,
						RDF.type);

				if(it2.hasNext() && isAcceptedType(it2.next().asResource())) {
					foundNext = true;
					next = holder.getResource(
							candidateResource.getURI(), 
							resourceClass);
				} 
			} 
			if(!foundNext)
				next = null;
		}

		private boolean isAcceptedType(Resource resource) {
			for(String acceptedType:acceptedTypeUris) {
				if(resource.equals(KtbsJenaBase.this.rdfModel.getResource(acceptedType)))
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
			T toBeReturned = next;
			doNext();
			return toBeReturned;
		}

		@Override
		public void remove() {
			throw new ReadOnlyObjectException(KtbsJenaBase.this);
		}

	}

	
}
