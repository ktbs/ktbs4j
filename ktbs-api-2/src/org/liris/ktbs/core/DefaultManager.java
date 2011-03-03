package org.liris.ktbs.core;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.domain.AttributePair;
import org.liris.ktbs.core.domain.KtbsResource;
import org.liris.ktbs.core.domain.MethodParameter;
import org.liris.ktbs.core.domain.ResourceFactory;
import org.liris.ktbs.core.domain.interfaces.IAttributePair;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IMethod;
import org.liris.ktbs.core.domain.interfaces.IMethodParameter;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.core.domain.interfaces.WithParameters;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.utils.KtbsUtils;


public class DefaultManager implements ResourceManager {

	/*
	 * Injected by Spring
	 */
	private ResourceDao dao;
	private ProxyFactory proxyFactory;
	private ResourceFactory pojoFactory;

	public void setDao(ResourceDao dao) {
		this.dao = dao;
	}

	public void setPojoFactory(ResourceFactory pojoFactory) {
		this.pojoFactory = pojoFactory;
	}

	public void setProxyFactory(ProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}

	@Override
	public <T extends IKtbsResource> T getKtbsResource(String uri, Class<T> cls) {
		T ktbsResource = dao.get(uri,cls);
		return ktbsResource;
	}

	@Override
	public IBase newBase(String rootUri, String baseLocalName, String owner) {
		IBase base = createResource(rootUri, baseLocalName, IBase.class, false);

		/*
		 * No ktbs:owner property defined yet in the RDF format.
		 * Put a label instead
		 */
		if(owner != null)
			base.getLabels().add("owner: " + owner);

		/*
		 * base.setOwner(owner);
		 */

		return createAndReturn(base);
	}

	@Override
	public IStoredTrace newStoredTrace(String baseUri,
			String traceLocalName, String model, String origin,
			String defaultSubject) {
		IStoredTrace trace = createResource(baseUri, traceLocalName, IStoredTrace.class, false);
		trace.setOrigin(origin);

		/*
		 * No ktbs:hasSubject property defined yet in the RDF format for a StoredTrace.
		 * Put a label instead
		 */
		if(defaultSubject != null)
			trace.getLabels().add("subject: " + defaultSubject);

		/*
		 * trace.setDefaultSubject(defaultSubject);
		 */

		trace.setTraceModel(proxyFactory.createResource(model, ITraceModel.class));

		return createAndReturn(trace);
	}

	@Override
	public IComputedTrace newComputedTrace(String baseUri,
			String traceLocalName, String methodUri, Set<String> sourceTraces, Map<String,String> parameters) {

		IComputedTrace trace = createResource(baseUri, traceLocalName, IComputedTrace.class, false);
		trace.setMethod(proxyFactory.createResource(methodUri, IMethod.class));

		trace.setSourceTraces(convertToSetOfProxies(sourceTraces, ITrace.class));

		setParameters(trace, parameters);

		return createAndReturn(trace);
	}

	private <T extends IKtbsResource> Set<T> convertToSetOfProxies(Set<String> resourceUris, Class<T> cls) {
		Set<T> proxySet = new HashSet<T>();
		if(resourceUris == null)
			return proxySet;
		for(String sourceTraceUri:resourceUris) 
			proxySet.add(proxyFactory.createResource(sourceTraceUri, cls));
		return proxySet;
	}

	@Override
	public IMethod newMethod(String baseUri, String methodLocalName,
			String inheritedMethod, String etag, Map<String,String> parameters) {

		IMethod method = createResource(baseUri, methodLocalName, IMethod.class, false);
		method.setEtag(etag);
		method.setInherits(inheritedMethod);

		setParameters(method, parameters);

		return createAndReturn(method);
	}

	private void setParameters(WithParameters resource, Map<String, String> parameters) {
		if(parameters != null) {
			Set<IMethodParameter> p = new HashSet<IMethodParameter>();
			for(String paramName:parameters.keySet()) 
				p.add(new MethodParameter(paramName, parameters.get(paramName)));
			resource.setMethodParameters(p);
		}
	}



	@Override
	public ITraceModel newTraceModel(String baseUri, String modelLocalName) {
		ITraceModel model = createResource(baseUri, modelLocalName, ITraceModel.class, false);

		return createAndReturn(model);
	}

	@Override
	public IObsel newObsel(String storedTraceUri, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Map<String, Object> attributes) {

		IObsel obsel = createResource(storedTraceUri, obselLocalName, IObsel.class, true);
		obsel.setBeginDT(beginDT);
		obsel.setEndDT(endDT);
		obsel.setBegin(begin);
		obsel.setEnd(end);
		obsel.setSubject(subject);
		Set<IAttributePair> pairs = new HashSet<IAttributePair>();
		if(attributes != null) {
			for(String key:attributes.keySet()) 
				pairs.add(new AttributePair(
						proxyFactory.createResource(key, IAttributeType.class), 
						attributes.get(key)
				));
			obsel.setAttributePairs(pairs);
		} 

		return createAndReturn(obsel);
	}

	private <T extends IKtbsResource> T createResource(
			String parentUri, 
			String localName, 
			Class<T> cls, 
			boolean leaf) {
		T resource;
		if(localName == null)
			resource = pojoFactory.createResource(cls);
		else 			
			resource = pojoFactory.createResource(KtbsUtils.makeChildURI(parentUri, localName, leaf), cls);
		if(parentUri != null)
			((KtbsResource)resource).setParentResource(pojoFactory.createResource(parentUri));
		return resource;
	}

	private <T extends IKtbsResource> T createAndReturn(T resource) {
		return dao.create(resource);
	}

	@Override
	public boolean saveKtbsResource(IKtbsResource resource) {
		return saveKtbsResource(resource, false);
	}

	@Override
	public boolean deleteKtbsResource(String uri,
			boolean cascadeLinked, boolean cascadeChildren) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean saveKtbsResource(IKtbsResource resource,
			boolean cascadeChildren) {
		return dao.save(resource, cascadeChildren);
	}

}
