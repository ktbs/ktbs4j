package org.liris.ktbs.client;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * The entry point of the Java KTBS client API.
 * 
 * <p>
 * 
 * KTBSClientApplication is a singleton that provides client instance for communicating
 * with KTBS servers. Available services are defined in 
 * the interface {@link KtbsClientService}.
 * 
 * </p>
 * The KTBS Rest service for a guven root is obtained via the method {@link #getRestService(String)}.
 * <p>
 * 
 * Each KTBS server is identified by the URI of its KTBS root. The KTBSClientApplication allows only 
 * one instance of KtbsClient for each KTBS root. We chose this strategy for two reasons:
 * <ul>
 * <li>the communications with different KTBS servers may require different client configs 
 * (HTTP caching, timeouts, authentications...),</li>
 * <li>the communication with the same KTBS server should not be bound to several client instances
 * locally, because it may cause troubles if the server interpretes different client instances 
 * running in  the same client host as the same client instance.</li>
 * </ul>
 * 
 * </p>
 * <p>
 * See org.liris.ktbs.example for an illustration of how to use the KTBS client.
 * </p>
 * 
 * 
 * @author Damien Cram
 * @see KtbsClientService
 */
public class KtbsClientApplication {

	private static KtbsClientApplication instance;

	private static final Log log = LogFactory.getLog(KtbsClientApplication.class);
	
	// Only one REST service instance per KtbsRoot
	private HashMap<String, KtbsRestService> restServices = new HashMap<String, KtbsRestService>();
	
	
	
	/**
	 * Returns the {@link KtbsRestService} instance that is mapped with the parameter 
	 * KTBS root URI, and creates it if it does not exist.
	 * 
	 * @param ktbsRootUri the URI of the KTBS root the client will communicate with
	 * @return the {@link KtbsRestService} instance mapped to the parameter KTBS root URI
	 * 
	 */
	public KtbsRestService getRestService(String ktbsRootUri) {
		if(restServices.get(ktbsRootUri) == null) {
			restServices.put(ktbsRootUri, new KtbsRestServiceImpl(ktbsRootUri));
			((KtbsRestServiceImpl)restServices.get(ktbsRootUri)).startSession();
		}
		
		return restServices.get(ktbsRootUri);
	}

	private KtbsClientApplication() {
		log.info("Ktbs client application is started.");
	}

	/**
	 * Get the KTBSClientApplication singleton
	 * @return the only instance of KTBSClientApplication
	 */
	public static KtbsClientApplication getInstance() {
		if(instance == null)
			instance = new KtbsClientApplication();
		return instance;
	}	

}
