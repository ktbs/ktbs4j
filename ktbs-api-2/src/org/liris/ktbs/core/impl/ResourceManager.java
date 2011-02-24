package org.liris.ktbs.core.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

public interface ResourceManager {
	
	public ResourcePojo getKtbsResource(String uri);
	public BasePojo newBase(String rootUri, String baseLocalName, String owner);

	public StoredTracePojo newStoredTrace(String baseUri, String traceLocalName, String model, String origin, String defaultSubject);
	public ComputedTracePojo newComputedTrace(String baseUri, String traceLocalName, String methodUri, Set<String> sourceTraces);
	public MethodPojo newMethod(String baseUri, String methodLocalName, String inheritedMethod, String etag);
	public TraceModelPojo newTraceModel(String baseUri, String modelLocalName);


	public ObselPojo newObsel(
			String storedTraceUri,
			String obselLocalName,
			String typeUri,
			String beginDT,
			String endDT,
			BigInteger begin,
			BigInteger end,
			String subject,
			Map<String, Object> attributes
			);

	public ObselTypePojo newObselType(String traceModelUri, String localName);

	public RelationTypePojo newRelationType(String traceModelUri, String localName, Set<String> domains, Set<String> ranges);
	public AttributeTypePojo newAttributeType(String traceModelUri, String localName, Collection<String> domainUris, Collection<String> rangeUris);

}
