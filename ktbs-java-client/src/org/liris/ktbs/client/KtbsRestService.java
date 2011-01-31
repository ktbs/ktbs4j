package org.liris.ktbs.client;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

import org.liris.ktbs.core.api.KtbsResource;

public interface KtbsRestService {
	
	public KtbsResponse retrieve(String uri);
	public KtbsResponse createBase(String rootURI, String baseURI, String label);
	public KtbsResponse createMethod(String baseURI, String methodURI, String inheritedMethodUri, Map<String,String> parameters, String label);
	public KtbsResponse createTraceModel(String baseURI, String modelURI, String label);
	public KtbsResponse createStoredTrace(String baseURI, String traceURI, String modelURI, String origin, String label);
	public KtbsResponse createComputedTrace(String baseURI, String traceURI, String methodURI, Collection<String> sourceTracesURIs, String label);
	public KtbsResponse createObsel(String traceURI, String obselURI, String typeURI, String subject, String beginDT, String endDT, BigInteger begin, BigInteger end, Map<String, Object> attributes, String label);
	public KtbsResponse update(KtbsResource resource, String eTag, String... traceAspect);
	public KtbsResponse delete(String uri);
	
}
