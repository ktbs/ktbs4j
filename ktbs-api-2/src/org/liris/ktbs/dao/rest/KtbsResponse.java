package org.liris.ktbs.dao.rest;


/**
 * The response object produced by a KTBS operation performed through the REST API.
 *  
 * @author Damien Cram
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
	 * @return the ETag header of a GET response, null if none.
	 */
	public String getHTTPETag();

	/**
	 * Give the mime type of the content.
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
	 * The body of the HTTP response as a Java string.
	 * 
	 * <p>
	 * The string is obtained from the stream returned by the method <code>getContentStream()</code>
	 *  on the underlying HTTP entity. 
	 * </p>
	 * 
	 * @return the body of the underlying HTTP response, as 
	 * a string.
	 */
	public String getBodyAsString();

	public int getHttpStatusCode();
}
