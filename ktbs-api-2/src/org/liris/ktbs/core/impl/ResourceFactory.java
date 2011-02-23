package org.liris.ktbs.core.impl;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.ResourceContainer;
import org.liris.ktbs.core.api.Root;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

/*
 * Singleton
 */
public class ResourceFactory {


	public StoredTrace createStoredTrace(Base base, String traceLocalName,
			TraceModel model, String origin) {

		String uri = makeUri(base, traceLocalName, false);
		StoredTrace storedTrace = new StoredTraceImpl(uri);

		if(origin != null)
			storedTrace.setOrigin(origin);
		if(model != null)
			storedTrace.setTraceModel(model);

		return storedTrace;
	}

	private String makeUri(ResourceContainer<?> container, String traceLocalName, boolean leaf) {
		String uri = KtbsUtils.resolveAbsoluteChildURI(container.getUri(), traceLocalName);
		return URI.create(leaf?uri:(uri+"/")).normalize().toString();
	}

	public ComputedTrace createComputedTrace(Base base, String origin, String traceLocalName, TraceModel model, 
			Method method, Set<Trace> sources) {

		String uri = makeUri(base, traceLocalName, false);
		ComputedTraceImpl computedTrace = new ComputedTraceImpl(uri);

		if(origin != null)
			computedTrace.setOrigin(origin);
		if(model != null)
			computedTrace.setTraceModel(model);
		if(method != null)
			computedTrace.setMethod(method);
		setLinkedResources(sources, computedTrace, "setSources");

		return computedTrace;
	}

	private void setLinkedResources(Set<? extends KtbsResource> linkedResources,
			KtbsResource resource, String fieldName) {
		if(linkedResources!= null) {
			Set<String> uris = new HashSet<String>();
			for(KtbsResource r:linkedResources)
				uris.add(r.getUri());
			try {
				resource.getClass().getDeclaredMethod(fieldName, Set.class).invoke(resource, uris);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public Method createMethod(Base base, String methodLocalName,
			String inheritedMethod) {
		// TODO Auto-generated method stub
		return null;
	}

	public TraceModel createTraceModel(Base base, String modelLocalName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Base createBase(Root root, String baseLocalName, String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	public Obsel createObsel(StoredTrace storedTrac, String obselLocalName,
			ObselType type, Map<AttributeType, Object> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	public ObselType createObselType(TraceModel traceModel, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	public RelationType createRelationType(TraceModel traceModel,
			String localName, Collection<ObselType> domains,
			Collection<ObselType> ranges) {
		// TODO Auto-generated method stub
		return null;
	}

	public AttributeType createAttributeType(TraceModel traceModel,
			String localName, Collection<ObselType> domain) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Method createMethod(String baseUri, String methodLocalName,
			String inheritedMethod) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Root createRoot(String rootUri) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public TraceModel createTraceModel(String baseUri, String modelLocalName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Base createBase(String rootUri, String baseLocalName, String owner) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Obsel createObsel(String traceUri, String obselLocalName,
			ObselType type, Map<AttributeType, Object> attributes) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ObselType createObselType(String traceModelUri, String localName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RelationType createRelationType(String traceModelUri,
			String localName, Collection<ObselType> domains,
			Collection<ObselType> ranges) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AttributeType createAttributeType(String traceModelUri,
			String localName, Collection<ObselType> domain) {
		// TODO Auto-generated method stub
		return null;
	}
}
