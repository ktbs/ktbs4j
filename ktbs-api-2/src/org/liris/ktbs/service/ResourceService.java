package org.liris.ktbs.service;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;

public interface ResourceService {
	
	public IRoot getRoot();
	public IBase getBase(String uri);
	public IMethod getMethod(String uri);
	public IStoredTrace getStoredTrace(String uri);
	public IComputedTrace getComputedTrace(String uri);
	public ITrace getTrace(String uri);
	public ITraceModel getTraceModel(String uri);
	
	public <T extends IKtbsResource> T getResource(String uri, Class<T> cls);
	
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


	public boolean deleteResource(String uri, boolean cascadeLinked, boolean cascadeChildren);

	public boolean saveResource(IKtbsResource resource);

	public boolean saveResource(IKtbsResource resource, boolean cascadeChildren);
}
