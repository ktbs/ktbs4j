package org.liris.ktbs.client;

import org.liris.ktbs.dao.ProxyFactory;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.service.TraceModelService;

/**
 * The client object that provides services for communication 
 * with a KTBS.
 * 
 * @author Damien Cram
 *
 */
public interface KtbsClient {
	
	public ProxyFactory getProxyFactory();
	
	/**
	 * Give the uri of the KTBS root against which this client operates.
	 * 
	 * @return the root uri
	 */
	public String getRootUri();

	/**
	 * Register authentication data required for this root.
	 * 
	 * @param username the KTBS username
	 * @param password the password
	 */
	public void setCredentials(String username, String password);
	
	/**
	 * Give a service object for general KTBS resource handling.
	 * 
	 * @return the unique {@link ResourceService} object contained in this client
	 */
	public ResourceService getResourceService();
	
	/**
	 * Give a service object for trace model manipulation.
	 * 
	 * @return the unique {@link TraceModelService} object contained in this client
	 */
	public TraceModelService getTraceModelService();

	/**
	 * Give a service object for trace collection.
	 * 
	 * @return the unique {@link StoredTraceService} object contained in this client
	 */
	public StoredTraceService getStoredTraceService();

}