package org.liris.ktbs.core;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IMethod;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;

public interface ResourceManager {
	
	public <T extends IKtbsResource> T getKtbsResource(String uri, Class<T> cls);
	
	public IBase newBase(String rootUri, String baseLocalName, String owner);
	public IStoredTrace newStoredTrace(String baseUri, String traceLocalName, String model, String origin, String defaultSubject);
	
	public IComputedTrace newComputedTrace(
			String baseUri, 
			String traceLocalName, 
			String methodUri, 
			Set<String> sourceTraces,
			Map<String,String> parameters
			);
	
	public IMethod newMethod(
			String baseUri, 
			String methodLocalName, 
			String inheritedMethod, 
			String etag,
			Map<String,String> parameters
			);
	
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


	public boolean deleteKtbsResource(String uri, boolean cascadeLinked, boolean cascadeChildren);

	public boolean saveKtbsResource(IKtbsResource resource);

	public boolean saveKtbsResource(IKtbsResource resource, boolean cascadeChildren);
}
