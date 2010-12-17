package org.liris.ktbs.client;

import org.apache.http.HttpResponse;
import org.liris.ktbs.core.KtbsResource;

/**
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
	 * A utility method that converts the HTTP repsonse body, if any,
	 * to a string.
	 * @return
	 */
	public String getBodyAsString();
	public KtbsResponseStatus getKtbsStatus();
	public HttpResponse getHTTPResponse();
	
}
