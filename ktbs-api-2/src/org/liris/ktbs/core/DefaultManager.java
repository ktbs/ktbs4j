package org.liris.ktbs.core;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.domain.AttributeType;
import org.liris.ktbs.core.domain.ComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IMethod;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.utils.KtbsUtils;


public class DefaultManager implements ResourceManager {

	/*
	 * Injected by Spring
	 */
	private ResourceDao dao;

	
	@Override
	public IKtbsResource getKtbsResource(String uri) {
		IKtbsResource ktbsResource = dao.get(uri);
		
		return ktbsResource;
	}

	@Override
	public IBase newBase(String rootUri, String baseLocalName, String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStoredTrace newStoredTrace(String baseUri,
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
	public IMethod newMethod(String baseUri, String methodLocalName,
			String inheritedMethod, String etag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITraceModel newTraceModel(String baseUri, String modelLocalName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IObsel newObsel(String storedTraceUri, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Map<String, Object> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IObselType newObselType(String traceModelUri, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRelationType newRelationType(String traceModelUri,
			String localName, Set<String> domains, Set<String> ranges) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAttributeType newAttributeType(String traceModelUri,
			String localName, Collection<String> domainUris,
			Collection<String> rangeUris) {
		
		AttributeType type = new AttributeType();
		type.setURI(KtbsUtils.makeChildURI(traceModelUri, localName, true));
		
		return null;
	}
}
