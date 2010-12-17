package org.liris.ktbs.client;

/**
 * Holds codes for {@link KtbsResponse} different status.
 * @author Damien Cram
 *
 */
public enum KtbsResponseStatus {
	
	/**
	 * the KTBS resource has been created on the server with no issue
	 */
	RESOURCE_CREATED,
	
	
	/**
	 * the KTBS resource has been modified on the server with no issue
	 */
	RESOURCE_MODIFIED,
	
	
	/**
	 * the KTBS resource has been deleted from the server with no issue
	 */
	RESOURCE_DELETED,
	
	
	/**
	 * the KTBS resource has been retrieved from the server with no issue
	 */
	RESOURCE_RETRIEVED,
	
	/**
	 * Represents an exception (not necessary an error, e.g. it could be a "resource already exists" 
	 * response when creating a resource) that occurred on the KTBS server when 
	 * executing the request. Users might want to call KtbsResponseStatus.getHttpStatus() to 
	 * get more information about the exception.
	 */
	SERVER_EXCEPTION,
	
	/**
	 * represents an error occurred within the KTBS client when executing the request.
	 */
	INTERNAL_ERR0R
}
