package org.liris.ktbs.client;

import org.apache.http.HttpResponse;
import org.liris.ktbs.core.KtbsResource;

/**
 * A holder for properties of a KTBS response: the returned resource, status...
 *  
 * @author Damien Cram
 * @see KtbsResponseStatus
 * @see KtbsClientService
 * 
 */
public interface KtbsResponse {

	/**
	 * @return the body of the server HTTP response parsed and transformed 
	 * into a KtbsResponse
	 */
	public KtbsResource getBodyAsKtbsResource();
	
	/**
	 * @return true if the service method has been executed on the KTBS server
	 * with success
	 */
	public boolean executedWithSuccess();

	/**
	 * A utility method that converts the HTTP response body, if any,
	 *  sent by the KTBS server to a string.
	 * @return the body as a string, or an empty string if there is no body 
	 * or the response is an error.
	 */
	public String getServerMessage();
	
	/**
	 * @return the KTBS status of the response
	 */
	public KtbsResponseStatus getKtbsStatus();
	
	/**
	 * 
	 * @return the underlying {@link org.apache.http.HttpResponse} object from the Apache 
	 * HttpClient API that has been used to interprete the response of the KTBS server.
	 */
	public HttpResponse getHTTPResponse();
	
	/**
	 * 
	 * @return the ETag header of a GET response, null if none.
	 */
	public String getHTTPETag();

	/**
	 * 
	 * @return the location header returned by the KTBS server to give the URI
	 * of the created resource, null if none.
	 */
	public String getHTTPLocation();
	
	/**
	 * 
	 * @return the value of the request once the KTBS response body 
	 * is interpreted by the client
	 */
	public Object getValue();
	void setValue(Object value);
}