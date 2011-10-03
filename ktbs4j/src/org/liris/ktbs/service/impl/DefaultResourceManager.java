package org.liris.ktbs.service.impl;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.dao.rest.KtbsResponse;
import org.liris.ktbs.domain.AttributePair;
import org.liris.ktbs.domain.KtbsResource;
import org.liris.ktbs.domain.MethodParameter;
import org.liris.ktbs.domain.PojoFactory;
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
import org.liris.ktbs.service.IRootAwareService;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.utils.KtbsUtils;


public class DefaultResourceManager implements ResourceService, IRootAwareService {

	/*
	 * Injected by Spring
	 */
	protected ResourceDao dao;
	private PojoFactory pojoFactory;

	public DefaultResourceManager(ResourceDao dao, PojoFactory pojoFactory) {
		super();
		this.dao = dao;
		this.pojoFactory = pojoFactory;
	}

	@Override
	public <T extends IKtbsResource> T getResource(String uri, Class<T> cls) {
		String absoluteResourceUri = KtbsUtils.makeAbsoluteURI(getRootUri(), uri, KtbsUtils.isLeafType(cls));
		T ktbsResource = dao.get(absoluteResourceUri,cls);
		return ktbsResource;
	}

	@Override
	public String newBase(String baseLocalName) {
		IBase base = createResource("", baseLocalName, IBase.class, false);

		return dao.create(base);
	}


	@Override
	public String newComputedTrace(String baseUri,
			String traceLocalName, String methodUri, Set<String> sourceTraces, Map<String,String> parameters) {

		IComputedTrace trace = createResource(baseUri, traceLocalName, IComputedTrace.class, false);
		trace.setMethod(dao.getProxyFactory().createResource(methodUri, IMethod.class));

		trace.setSourceTraces(convertToSetOfProxies(sourceTraces, ITrace.class));

		setParameters(trace, parameters);

		return dao.create(trace);
	}

	@Override
	public String newComputedTrace(String baseUri,
			String traceLocalName, String methodUri, Set<String> sourceTraces, Map<String,String> parameters, String label) {

		IComputedTrace trace = createResource(baseUri, traceLocalName, IComputedTrace.class, false);
		trace.setMethod(dao.getProxyFactory().createResource(methodUri, IMethod.class));

		trace.setSourceTraces(convertToSetOfProxies(sourceTraces, ITrace.class));
		trace.addLabel(label);
		setParameters(trace, parameters);

		return dao.create(trace);
	}
	
	private <T extends IKtbsResource> Set<T> convertToSetOfProxies(Set<String> resourceUris, Class<T> cls) {
		Set<T> proxySet = new HashSet<T>();
		if(resourceUris == null)
			return proxySet;
		for(String sourceTraceUri:resourceUris) 
			proxySet.add(dao.getProxyFactory().createResource(sourceTraceUri, cls));
		return proxySet;
	}

	@Override
	public String newMethod(String baseUri, String methodLocalName,
			String inheritedMethod, Map<String,String> parameters) {

		IMethod method = createResource(baseUri, methodLocalName, IMethod.class, true);
		method.setInherits(inheritedMethod);

		setParameters(method, parameters);

		return dao.create(method);
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
	public String newTraceModel(String baseUri, String modelLocalName) {
		ITraceModel model = createResource(baseUri, modelLocalName, ITraceModel.class, false);
		return dao.create(model);
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
						dao.getProxyFactory().createResource(pair.getAttributeType().getUri(), IAttributeType.class), 
						pair.getValue()
				));
			obsel.setAttributePairs(pairs);
		} 

		return dao.create(obsel);
	}
	@Override
	public String newObsel(String storedTraceUri, String obselLocalName,
		String typeUri, String beginDT, String endDT, BigInteger begin,
		BigInteger end, String subject, Set<IAttributePair> attributes, Set<String> labels) {
	    
	    IObsel obsel = createResource(storedTraceUri, obselLocalName, IObsel.class, true);
	    obsel.setBeginDT(beginDT);
	    obsel.setEndDT(endDT);
	    obsel.setBegin(begin);
	    obsel.setEnd(end);
	    obsel.setSubject(subject);
	    obsel.setLabels(labels);
	    
	    if(typeUri != null)
		obsel.setObselType(pojoFactory.createResource(typeUri, IObselType.class));
	    
	    Set<IAttributePair> pairs = new HashSet<IAttributePair>();
	    if(attributes != null) {
		for(IAttributePair pair:attributes) 
		    pairs.add(new AttributePair(
			    dao.getProxyFactory().createResource(pair.getAttributeType().getUri(), IAttributeType.class), 
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
		String absoluteParentUri = KtbsUtils.makeAbsoluteURI(getRootUri(), parentUri, false);

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
	public boolean deleteResource(String uri) {
		return dao.delete(uri);
	}

	@Override
	public boolean deleteResource(IKtbsResource resource) {
		return dao.delete(resource.getUri());
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

	@Override
	public KtbsResponse getLastResponse() {
		return dao.getLastResponse();
	}

	@Override
	public IKtbsResource getResource(String uri) {
	    return dao.get(uri);
	}

	@Override
	public boolean exists(String uri) {
		return getResource(uri) != null;
	}

	@Override
	public String getRootUri() {
		return dao.getRootUri();
	}
	
	
	public ResourceDao getDao() {
		return dao;
	}

	@Override
	public String newStoredTrace(String baseUri, String traceLocalName,
			String modelUri, String origin, BigInteger traceBegin,
			String traceBeginDT, BigInteger traceEnd, String traceEndDT,
			String defaultSubject) {
		
		IStoredTrace trace = createResource(baseUri, traceLocalName, IStoredTrace.class, false);
		trace.setOrigin(origin);
		trace.setTraceBegin(traceBegin);
		trace.setTraceBeginDT(traceBeginDT);
		trace.setTraceEnd(traceEnd);
		trace.setTraceEndDT(traceEndDT);

		/*
		 * No ktbs:hasSubject property defined yet in the RDF format for a StoredTrace.
		 * Put a label instead
		 */
		if(defaultSubject != null)
			trace.getLabels().add("subject: " + defaultSubject);

		/*
		 * trace.setDefaultSubject(defaultSubject);
		 */

		trace.setTraceModel(dao.getProxyFactory().createResource(modelUri, ITraceModel.class));

		return dao.create(trace);
	}
}
