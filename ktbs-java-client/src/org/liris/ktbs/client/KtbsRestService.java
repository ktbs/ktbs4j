package org.liris.ktbs.client;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

import org.liris.ktbs.core.KtbsResource;

public interface KtbsRestService {
	
	public KtbsResponse retrieve(String uri);
	public KtbsResponse createBase(String rootURI, String baseURI);
	public KtbsResponse createMethod(String baseURI, String methodURI, String inheritedMethodUri, Map<String,String> parameters);
	public KtbsResponse createTraceModel(String baseURI, String modelURI);
	public KtbsResponse createStoredTrace(String baseURI, String traceURI, String modelURI, String origin);
	public KtbsResponse createComputedTrace(String baseURI, String traceURI, String methodURI, Collection<String> sourceTracesURIs);
	public KtbsResponse createObsel(String traceURI, String obselURI, String typeURI, String subject, String beginDT, String endDT, BigInteger begin, BigInteger end, Map<String, String> attributes);
	public KtbsResponse update(KtbsResource resource, String eTag, String... traceAspect);
	public KtbsResponse delete(String uri);
	
}
