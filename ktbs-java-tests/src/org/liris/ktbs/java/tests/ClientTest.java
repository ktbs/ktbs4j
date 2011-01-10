package org.liris.ktbs.java.tests;

import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.client.KtbsClientApplication;

public class ClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KtbsClientApplication app = KtbsClientApplication.getInstance();

		KtbsClient client = app.getKtbsClient("http://localhost:8001/");
		client.startSession();

		client.closeSession();
	}


}
