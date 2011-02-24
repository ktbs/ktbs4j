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

public interface ResourceManager {
	
	public KtbsResource getKtbsResource(String uri);
	public Base newBase(String rootUri, String baseLocalName, String owner);

	public StoredTrace newStoredTrace(String baseUri, String traceLocalName, String model, String origin, String defaultSubject);
	public ComputedTrace newComputedTrace(String baseUri, String traceLocalName, String methodUri, Set<String> sourceTraces);
	public Method newMethod(String baseUri, String methodLocalName, String inheritedMethod, String etag);
	public TraceModel newTraceModel(String baseUri, String modelLocalName);


	public Obsel newObsel(
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

	public ObselType newObselType(String traceModelUri, String localName);

	public RelationType newRelationType(String traceModelUri, String localName, Set<String> domains, Set<String> ranges);
	public AttributeType newAttributeType(String traceModelUri, String localName, Collection<String> domainUris, Collection<String> rangeUris);

}
