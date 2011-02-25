package org.liris.ktbs.core;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

public interface ResourceManager {
	
	public IKtbsResource getKtbsResource(String uri);
	public IBase newBase(String rootUri, String baseLocalName, String owner);

	public IStoredTrace newStoredTrace(String baseUri, String traceLocalName, String model, String origin, String defaultSubject);
	public ComputedTrace newComputedTrace(String baseUri, String traceLocalName, String methodUri, Set<String> sourceTraces);
	public IMethod newMethod(String baseUri, String methodLocalName, String inheritedMethod, String etag);
	public ITraceModel newTraceModel(String baseUri, String modelLocalName);


	public IObsel newObsel(
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

	public IObselType newObselType(String traceModelUri, String localName);

	public IRelationType newRelationType(String traceModelUri, String localName, Set<String> domains, Set<String> ranges);
	public IAttributeType newAttributeType(String traceModelUri, String localName, Collection<String> domainUris, Collection<String> rangeUris);

}
