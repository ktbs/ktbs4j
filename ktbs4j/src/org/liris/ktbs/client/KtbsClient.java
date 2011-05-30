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
	 * Gives the uri of the KTBS root against which this client operates.
	 * 
	 * @return the root uri
	 */
	public String getRootUri();

	/**
	 * Gives a service object for general KTBS resource handling.
	 * 
	 * @return the unique {@link ResourceService} object contained in this client
	 */
	public ResourceService getResourceService();
	
	/**
	 * Gives a service object for trace model manipulation.
	 * 
	 * @return the unique {@link TraceModelService} object contained in this client
	 */
	public TraceModelService getTraceModelService();

	/**
	 * Gives a service object for trace collection.
	 * 
	 * @return the unique {@link StoredTraceService} object contained in this client
	 */
	public StoredTraceService getStoredTraceService();

}