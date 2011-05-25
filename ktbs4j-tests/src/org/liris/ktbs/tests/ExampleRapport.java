package org.liris.ktbs.tests;

import org.liris.ktbs.client.ClientFactory;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.serial.DeserializationConfig;
import org.liris.ktbs.serial.DeserializationMode;
import org.liris.ktbs.serial.SerializationConfig;

public class ExampleRapport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientFactory clientFactory = Ktbs.getClientFactory();

		// the default serialization config
		SerializationConfig serializationConfig = new SerializationConfig();
		
		// the deserialization config
		DeserializationConfig deSerializationConfig = new DeserializationConfig();
		// proxy all access to children ressource
		deSerializationConfig.setChildMode(DeserializationMode.PROXY);
		
		// just put the uris of linked resource (e.g. ITrace.getTraceModel(), 
		// IObsel.getObselType(), IObselType.getDomains(), etc)
		// in empty resource objects
		deSerializationConfig.setLinkMode(DeserializationMode.URI_IN_PLAIN);

		// do the same for linked resources of the same types
		// e.g. ITrace.getSourceTraces(), IObsel.getSourceObsel(), etc
		deSerializationConfig.setLinkSameTypeMode(DeserializationMode.URI_IN_PLAIN);
		
		KtbsClient client = clientFactory.createRestClient(
				"http://ktbs.com/monroot/", 
				"user", 
				"pwd",
				serializationConfig,
				deSerializationConfig
				);

	}

}
