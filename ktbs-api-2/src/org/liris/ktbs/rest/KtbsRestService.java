package org.liris.ktbs.rest;

import org.liris.ktbs.core.pojo.ResourcePojo;


/**
 * 
 * A RDF-level interface to a KTBS server through the REST API. Instances 
 * of this interface must handle the HTTP communication with the remote KTBS.
 * 
 * <p>
 * This interface is said to be RDF-level because it does not 
 * handle Java representation of KTBS resources, but instead the 
 * RDF representation defined by the KTBS Server API.
 * </p>
 * 
 * @author Damien Cram
 *
 */
public interface KtbsRestService {
	
	/**
	 * Perform a GET request and retrieves a KTBS resource 
	 * that can be accessed through the REST API.
	 * 
	 * @param resourceURI the absolute resource URI to retrieve
	 * @return the {@link KtbsResponse} object produced by this REST request
	 */
	public KtbsResponse get(String resourceURI);
	
//	/**
//	 * Perform a POST request and creates a KTBS base 
//	 * remotely through the REST API.
//	 * 
//	 * @param rootURI the URI of the remote root that will contain the new base
//	 * @param baseLocalName the local base name, relatively to the root uri
//	 * @param label the label of the created base (can be null)
//	 * @return the {@link KtbsResponse} object produced by this REST request
//	 */
//	public KtbsResponse createBase(String rootURI, String baseLocalName, String label);

	public KtbsResponse post(ResourcePojo resource);
	
//	/**
//	 * Perform a POST request and creates a KTBS method
//	 * remotely through the REST API.
//	 * 
//	 * @param baseURI the absolute uri of the remote base that will contain the new created trace model
//	 * @param modelLocalName the local name of the trace model, relatively to the base uri
//	 * @param label the label of the created trace model (can be null)
//	 * @return the {@link KtbsResponse} object produced by this REST request
//	 */
//	public KtbsResponse createTraceModel(String baseURI, String modelLocalName, String label);
	
//	/**
//	 * 
//	 * @param baseURI
//	 * @param traceLocalName
//	 * @param modelURI
//	 * @param origin
//	 * @param label (can be null)
//	 * @return
//	 */
//	public KtbsResponse createStoredTrace(String baseURI, String traceLocalName, String modelURI, String origin, String label);
	
//	/**
//	 * 
//	 * @param baseURI
//	 * @param traceLocalName
//	 * @param methodURI
//	 * @param sourceTracesURIs
//	 * @param label (can be null)
//	 * @return
//	 */
//	public KtbsResponse createComputedTrace(String baseURI, String traceLocalName, String methodURI, Collection<String> sourceTracesURIs, String label);
	
//	/**
//	 * @param traceURI
//	 * @param obselLocalName (can be null, for anonymous obsels)
//	 * @param typeURI
//	 * @param subject (can be null)
//	 * @param beginDT (can be null)
//	 * @param endDT (can be null)
//	 * @param begin (can be null)
//	 * @param end (can be null)
//	 * @param attributes (can be null)
//	 * @param label (can be null)
//	 * @return
//	 */
//	public KtbsResponse createObsel(String traceURI, String obselLocalName, String typeURI, String subject, String beginDT, String endDT, String begin, String end, Map<String, Object> attributes, String label);
	
//	/**
//	 * Perform a UPDATE request and modifies a KTBS base
//	 * remotely through the REST API.
//	 * 
//	 * @param eTag the HTTP version tag of the base since it was last accessed
//	 * @param baseURI the absolute URI of the base to update
//	 * @param label (can be null)
//	 * 
//	 * @return the {@link KtbsResponse} object produced by this REST request
//	 */
//	public KtbsResponse updateBase(String eTag, String baseURI, String label);
	
//	/**
//	 * 
//	 * @param eTag
//	 * @param methodURI
//	 * @param inheritedMethodUri
//	 * @param parameters
//	 * @param label (can be null)
//	 * @return
//	 */
//	public KtbsResponse updateMethod(String eTag, String methodURI, String inheritedMethodUri, Map<String,String> parameters, String label);
	
//	/**
//	 * 
//	 * @param eTag
//	 * @param modelURI
//	 * @param label
//	 * @return
//	 */
//	public KtbsResponse updateTraceModel(String eTag, String modelURI, String label);
	
//	/**
//	 * 
//	 * @param eTag
//	 * @param traceURI
//	 * @param modelURI
//	 * @param origin
//	 * @param label (can be null)
//	 * @return
//	 */
//	public KtbsResponse updateStoredTrace(String eTag, String traceURI, String modelURI, String origin, String label);
	
//	/**
//	 * 
//	 * @param eTag
//	 * @param traceURI
//	 * @param methodURI
//	 * @param sourceTracesURIs
//	 * @param label
//	 * @return
//	 */
//	public KtbsResponse updateComputedTrace(String eTag, String traceURI, String methodURI, Collection<String> sourceTracesURIs, String label);
	
//	/**
//	 * @param eTag
//	 * @param obselURI the abolute uri of the obsel to be updated
//	 * @param typeURI 
//	 * @param subject (can be null)
//	 * @param beginDT (can be null)
//	 * @param endDT (can be null)
//	 * @param begin (can be null)
//	 * @param end (can be null)
//	 * @param attributes (can be null)
//	 * @param label (can be null)
//	 * @return
//	 */
//	public KtbsResponse updateObsel(String eTag, String obselURI, String typeURI, String subject, String beginDT, String endDT, String begin, String end, Map<String, Object> attributes, String label);
	
	/**
	 * Perform a DELETE request to remotely delete a KTBS resource
	 * through the REST API.
	 * 
	 * @param resourceURI the absolute URI of the resource to remove
	 * @return the {@link KtbsResponse} object produced by this REST request
	 */
	public KtbsResponse delete(String resourceURI);

	public KtbsResponse update(ResourcePojo resource, String etag);
}
