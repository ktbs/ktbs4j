package org.liris.ktbs.core.nocache;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.impl.ResourceManager;
import org.liris.ktbs.core.pojo.AttributeTypePojo;
import org.liris.ktbs.core.pojo.BasePojo;
import org.liris.ktbs.core.pojo.ComputedTracePojo;
import org.liris.ktbs.core.pojo.MethodPojo;
import org.liris.ktbs.core.pojo.ObselPojo;
import org.liris.ktbs.core.pojo.ObselTypePojo;
import org.liris.ktbs.core.pojo.RelationTypePojo;
import org.liris.ktbs.core.pojo.ResourcePojo;
import org.liris.ktbs.core.pojo.StoredTracePojo;
import org.liris.ktbs.core.pojo.TraceModelPojo;
import org.liris.ktbs.dao.ResourceDao;


public class DefaultManager implements ResourceManager {

	/*
	 * Injected by Spring
	 */
	private ResourceDao dao;

	@Override
	public ResourcePojo getKtbsResource(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasePojo newBase(String rootUri, String baseLocalName, String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoredTracePojo newStoredTrace(String baseUri,
			String traceLocalName, String model, String origin,
			String defaultSubject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComputedTracePojo newComputedTrace(String baseUri,
			String traceLocalName, String methodUri, Set<String> sourceTraces) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodPojo newMethod(String baseUri, String methodLocalName,
			String inheritedMethod, String etag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TraceModelPojo newTraceModel(String baseUri, String modelLocalName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObselPojo newObsel(String storedTraceUri, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Map<String, Object> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObselTypePojo newObselType(String traceModelUri, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RelationTypePojo newRelationType(String traceModelUri,
			String localName, Set<String> domains, Set<String> ranges) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeTypePojo newAttributeType(String traceModelUri,
			String localName, Collection<String> domainUris,
			Collection<String> rangeUris) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
