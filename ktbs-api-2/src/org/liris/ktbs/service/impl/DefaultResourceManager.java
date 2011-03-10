package org.liris.ktbs.service.impl;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.dao.ProxyFactory;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.domain.AttributePair;
import org.liris.ktbs.domain.KtbsResource;
import org.liris.ktbs.domain.MethodParameter;
import org.liris.ktbs.domain.ResourceFactory;
import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IMethodParameter;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.domain.interfaces.WithParameters;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.utils.KtbsUtils;


public class DefaultResourceManager extends RootAwareService implements ResourceService {

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
	public <T extends IKtbsResource> T getResource(String uri, Class<T> cls) {
		T ktbsResource = dao.get(uri,cls);
		return ktbsResource;
	}

	@Override
	public IBase newBase(String baseLocalName, String owner) {
		IBase base = createResource("", baseLocalName, IBase.class, false);

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
			String inheritedMethod, Map<String,String> parameters) {

		IMethod method = createResource(baseUri, methodLocalName, IMethod.class, false);
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
	public String newObsel(String storedTraceUri, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Set<IAttributePair> attributes) {

		IObsel obsel = createResource(storedTraceUri, obselLocalName, IObsel.class, true);
		obsel.setBeginDT(beginDT);
		obsel.setEndDT(endDT);
		obsel.setBegin(begin);
		obsel.setEnd(end);
		obsel.setSubject(subject);

		if(typeUri != null)
			obsel.setObselType(pojoFactory.createResource(typeUri, IObselType.class));

		Set<IAttributePair> pairs = new HashSet<IAttributePair>();
		if(attributes != null) {
			for(IAttributePair pair:attributes) 
				pairs.add(new AttributePair(
						proxyFactory.createResource(pair.getAttributeType().getUri(), IAttributeType.class), 
						pair.getValue()
				));
			obsel.setAttributePairs(pairs);
		} 

		return dao.create(obsel);
	}

	private <T extends IKtbsResource> T createResource(
			String parentUri, 
			String localName, 
			Class<T> cls, 
			boolean leaf) {
		String absoluteParentUri = KtbsUtils.makeChildURI(rootUri, parentUri, false);

		T resource = pojoFactory.createResource(absoluteParentUri, localName, leaf, cls);

		((KtbsResource)resource).setParentResource(pojoFactory.createResource(absoluteParentUri));
		return resource;
	}

	private <T extends IKtbsResource> T createAndReturn(T resource) {
		return dao.createAndGet(resource);
	}

	@Override
	public boolean saveResource(IKtbsResource resource) {
		return saveResource(resource, false);
	}

	@Override
	public boolean deleteResource(String uri,
			boolean cascadeLinked, boolean cascadeChildren) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean saveResource(IKtbsResource resource,
			boolean cascadeChildren) {
		return dao.save(resource, cascadeChildren);
	}

	@Override
	public IBase getBase(String uri) {
		return getResource(uri, IBase.class);
	}

	@Override
	public IStoredTrace getStoredTrace(String uri) {
		return getResource(uri, IStoredTrace.class);
	}

	@Override
	public ITrace getTrace(String uri) {
		return getResource(uri, ITrace.class);
	}

	@Override
	public ITraceModel getTraceModel(String uri) {
		return getResource(uri, ITraceModel.class);
	}

	@Override
	public IRoot getRoot() {
		return getResource("", IRoot.class);
	}

	@Override
	public IMethod getMethod(String uri) {
		return getResource(uri, IMethod.class);
	}

	@Override
	public IComputedTrace getComputedTrace(String uri) {
		return getResource(uri, IComputedTrace.class);
	}
	
}
