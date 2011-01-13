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
 * KTBSClientApplication is a singleton that provides clients for communicating
 * with KTBS servers. Each client is an instance of the class 
 * {@link KtbsClient}. Available services are defined in 
 * the interface {@link KtbsClientService} and implemented by <code>KtbsClient</code>.
 * 
 * </p>
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
 * 
 * The KTBS Java client API must be used as follows:
 * 
 * <code><pre>
 * // get the application
 * KTBSClientApplication app = KTBSClientApplication.getInstance();
 * 
 * // get a KTBS client object for the KTBS root uri "http://localhost:8001/"
 * KtbsClient client = app.getKtbsClient("http://localhost:8001/");
 * 
 * // initiate the underlying HTTP client, and perform authentication if required
 * client.startSession();
 * 
 * // Call services on the KTBS server through the object client
 * KtbsResponse response1 = client.getTraceObsels("http://localhost:8001/", "base1", "t01");
 * Trace trace = (Trace)response1.getBodyAsKtbsResource());
 * 
 * KtbsResponse response2 = client.createObsel("http://localhost:8001/base1/t01/", ...);
 * // ...
 * 
 * // stop the underlying HTTP client.
 * client.closeSession();
 * </pre></code>
 * </p>
 * 
 * 
 * @author Damien Cram
 * @see KtbsClientService
 */
public class KtbsClientApplication {

	private static KtbsClientApplication instance;

	private static final Log log = LogFactory.getLog(KtbsClientApplication.class);
	
	// Only one KtbsClient instance per KtbsRoot
	private HashMap<String, KtbsClient> ktbsClients = new HashMap<String, KtbsClient>();

	/**
	 * Returns the {@link KtbsClient} instance that is mapped with the parameter 
	 * KTBS root URI, and creates it if it does not exist.
	 * 
	 * @param ktbsRootUri the URI of the KTBS root the client will communicate with
	 * @return the KtbsClient instance mapped to the parameter KTBS root URI
	 * 
	 */
	public KtbsClient getKtbsClient(String ktbsRootUri) {
		if(ktbsClients.get(ktbsRootUri) == null)
			ktbsClients.put(ktbsRootUri, new KtbsClient(ktbsRootUri));
	
		return ktbsClients.get(ktbsRootUri);
	}
	
	// Only one REST service instance per KtbsRoot
	private HashMap<String, KtbsRestService> restServices = new HashMap<String, KtbsRestService>();
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
