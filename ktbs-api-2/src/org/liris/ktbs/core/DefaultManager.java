package org.liris.ktbs.core;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.domain.AttributeType;
import org.liris.ktbs.core.domain.Base;
import org.liris.ktbs.core.domain.ComputedTrace;
import org.liris.ktbs.core.domain.KtbsResource;
import org.liris.ktbs.core.domain.Method;
import org.liris.ktbs.core.domain.Obsel;
import org.liris.ktbs.core.domain.ObselType;
import org.liris.ktbs.core.domain.RelationType;
import org.liris.ktbs.core.domain.StoredTrace;
import org.liris.ktbs.core.domain.TraceModel;
import org.liris.ktbs.dao.ResourceDao;


public class DefaultManager implements ResourceManager {

	/*
	 * Injected by Spring
	 */
	private ResourceDao dao;

	@Override
	public KtbsResource getKtbsResource(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Base newBase(String rootUri, String baseLocalName, String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoredTrace newStoredTrace(String baseUri,
			String traceLocalName, String model, String origin,
			String defaultSubject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComputedTrace newComputedTrace(String baseUri,
			String traceLocalName, String methodUri, Set<String> sourceTraces) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method newMethod(String baseUri, String methodLocalName,
			String inheritedMethod, String etag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TraceModel newTraceModel(String baseUri, String modelLocalName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Obsel newObsel(String storedTraceUri, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Map<String, Object> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObselType newObselType(String traceModelUri, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RelationType newRelationType(String traceModelUri,
			String localName, Set<String> domains, Set<String> ranges) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeType newAttributeType(String traceModelUri,
			String localName, Collection<String> domainUris,
			Collection<String> rangeUris) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
