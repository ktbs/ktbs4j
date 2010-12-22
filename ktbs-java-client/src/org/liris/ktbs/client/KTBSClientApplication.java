package org.liris.ktbs.client;

import java.util.HashMap;

/**
 * TODO Writes the user manual here
 * 
 * @author Damien Cram
 *
 */
public class KTBSClientApplication {

	private static final KTBSClientApplication instance = new KTBSClientApplication();

	// Only one KtbsClient instance per KtbsRoot
	private HashMap<String, KtbsClient> ktbsClients = new HashMap<String, KtbsClient>();

	public KtbsClient getKtbsClient(String ktbsRootUri) {
		if(ktbsClients.get(ktbsRootUri) == null)
			ktbsClients.put(ktbsRootUri, new KtbsClient());
	
		return ktbsClients.get(ktbsRootUri);
	}

	private KTBSClientApplication() {
	}

	public static KTBSClientApplication getInstance() {
		return instance;
	}	

}
