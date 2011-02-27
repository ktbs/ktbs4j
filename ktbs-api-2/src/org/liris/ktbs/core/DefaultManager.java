package org.liris.ktbs.core;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.domain.AttributePair;
import org.liris.ktbs.core.domain.AttributeType;
import org.liris.ktbs.core.domain.Base;
import org.liris.ktbs.core.domain.ComputedTrace;
import org.liris.ktbs.core.domain.Method;
import org.liris.ktbs.core.domain.Obsel;
import org.liris.ktbs.core.domain.ObselType;
import org.liris.ktbs.core.domain.RelationType;
import org.liris.ktbs.core.domain.ResourceFactory;
import org.liris.ktbs.core.domain.StoredTrace;
import org.liris.ktbs.core.domain.TraceModel;
import org.liris.ktbs.core.domain.interfaces.IAttributePair;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IMethod;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
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
		Base base = new Base();
		base.setURI(KtbsUtils.makeChildURI(rootUri, baseLocalName, false));
		base.setOwner(owner);

		return createAndReturn(base);
	}

	@Override
	public IStoredTrace newStoredTrace(String baseUri,
			String traceLocalName, String model, String origin,
			String defaultSubject) {
		StoredTrace trace = new StoredTrace();
		trace.setURI(KtbsUtils.makeChildURI(baseUri, traceLocalName, false));
		trace.setOrigin(origin);
		trace.setDefaultSubject(defaultSubject);
		trace.setTraceModel(proxyFactory.createResource(model, ITraceModel.class));

		return createAndReturn(trace);
	}

	@Override
	public IComputedTrace newComputedTrace(String baseUri,
			String traceLocalName, String methodUri, Set<String> sourceTraces) {
		ComputedTrace trace = new ComputedTrace();
		trace.setURI(KtbsUtils.makeChildURI(baseUri, traceLocalName, false));
		trace.setMethod(proxyFactory.createResource(methodUri, IMethod.class));

		trace.setSourceTraces(convertToSetOfProxies(sourceTraces, ITrace.class));
		
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
			String inheritedMethod, String etag) {

		Method method = new Method();
		method.setURI(KtbsUtils.makeChildURI(baseUri, methodLocalName, false));
		method.setEtag(etag);
		method.setInherits(inheritedMethod);

		return createAndReturn(method);
	}



	@Override
	public ITraceModel newTraceModel(String baseUri, String modelLocalName) {
		TraceModel model = new TraceModel();
		model.setURI(KtbsUtils.makeChildURI(baseUri, modelLocalName, false));

		return createAndReturn(model);
	}

	@Override
	public IObsel newObsel(String storedTraceUri, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Map<String, Object> attributes) {

		Obsel obsel = new Obsel();
		obsel.setURI(KtbsUtils.makeChildURI(storedTraceUri, obselLocalName, true));
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

	@Override
	public IObselType newObselType(String traceModelUri, String localName) {
		ObselType type = new ObselType();
		type.setURI(KtbsUtils.makeChildURI(traceModelUri, localName, true));
		
		return createAndReturn(type);
	}

	@Override
	public IRelationType newRelationType(String traceModelUri,
			String localName, Set<String> domains, Set<String> ranges) {
		
		RelationType relType = new RelationType();
		relType.setURI(KtbsUtils.makeChildURI(traceModelUri, localName, true));
		relType.setDomains(convertToSetOfProxies(domains, IObselType.class));
		relType.setRanges(convertToSetOfProxies(ranges, IObselType.class));
		
		return createAndReturn(relType);
	}

	@Override
	public IAttributeType newAttributeType(String traceModelUri,
			String localName, Set<String> domainUris,
			Set<String> ranges) {

		AttributeType attType = new AttributeType();
		attType.setURI(KtbsUtils.makeChildURI(traceModelUri, localName, true));
		attType.setDomains(convertToSetOfProxies(domainUris, IObselType.class));
		attType.setRanges(ranges);
		
		return createAndReturn(attType);
	}

	private <T extends IKtbsResource> T createAndReturn(T resource) {
		boolean created = dao.create(resource);
		return created?resource:null;
	}

	@Override
	public boolean saveKtbsResource(IKtbsResource resource,
			boolean cascadeLinked, boolean cascadeChildren) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean deleteKtbsResource(String uri,
			boolean cascadeLinked, boolean cascadeChildren) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
