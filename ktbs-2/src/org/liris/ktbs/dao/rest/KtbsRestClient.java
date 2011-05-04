package org.liris.ktbs.dao.rest;


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
public interface KtbsRestClient {
	
	/**
	 * Perform a GET request and retrieves a KTBS resource 
	 * that can be accessed through the REST API.
	 * 
	 * @param resourceURI the absolute resource URI to retrieve
	 * @return the {@link KtbsResponse} object produced by this REST request
	 */
	public KtbsResponse get(String resourceURI);

	/**
	 * Init the http client with no athentication
	 */
	public void startSession();
	public void endSession();
	
	public KtbsResponse post(String uri, String resourceAsString);
	
	/**
	 * Perform a DELETE request to remotely delete a KTBS resource
	 * through the REST API.
	 * 
	 * @param resourceURI the absolute URI of the resource to remove
	 * @return the {@link KtbsResponse} object produced by this REST request
	 */
	public KtbsResponse delete(String resourceURI);

	public KtbsResponse update(String updateUri, String resourceAsString, String etag);

	public void setCredentials(String username, String password);
}
