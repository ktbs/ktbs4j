package org.liris.ktbs.client;

/**
 * Holds codes for {@link KtbsResponse} different status.
 * @author Damien Cram
 *
 */
public interface KtbsResponseStatus {
	
	/**
	 * the KTBS resource has been created on the server with no issue
	 */
	public static final int RESOURCE_CREATED = 0;
	
	
	/**
	 * the KTBS resource has been modified on the server with no issue
	 */
	public static final int RESOURCE_MODIFIED = 1;
	
	
	/**
	 * the KTBS resource has been deleted from the server with no issue
	 */
	public static final int RESOURCE_DELETED = 2;
	
	
	/**
	 * the KTBS resource has been retrieved from the server with no issue
	 */
	public static final int RESOURCE_RETRIVED = 3;
	
	/**
	 * Represents an exception (not necessary an error, e.g. it could be a "resource already exists" 
	 * response when creating a resource) that occurred on the KTBS server when 
	 * executing the request. Users might want to call KtbsResponseStatus.getHttpStatus() to 
	 * get more information about the exception.
	 */
	public static final int SERVER_EXCEPTION = 4;
	
	/**
	 * represents an error occurred within the KTBS client when executing the request.
	 */
	public static final int INTERNAL_ERR0R = 5;
}
