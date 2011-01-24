package org.liris.ktbs.client;

import java.io.InputStream;

import org.apache.http.HttpResponse;

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
	 * @return true if the service method has been executed on the KTBS server
	 * with success
	 */
	public boolean hasSucceeded();

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
	 * Give the mime type of the content
	 * 
	 * @return the mime type, null if none.
	 */
	public String getMimeType();

	/**
	 * 
	 * @return the location header returned by the KTBS server to give the URI
	 * of the created resource, null if none.
	 */
	public String getHTTPLocation();
	
	/**
	 * The body of the HTTP response as a stream.
	 * 
	 * <p>
	 * The stream is obtained by calling the method <code>getContentStream()</code> on the underlying HTTP entity. 
	 * </p>
	 * 
	 * @return the body of the underlying HTTP response, as 
	 * an input stream.
	 */
	public InputStream getBody();

	/**
	 * Remove the body from the memory by calling the underlying EntityUtils.consume() method.
	 */
	public void consume();
}
