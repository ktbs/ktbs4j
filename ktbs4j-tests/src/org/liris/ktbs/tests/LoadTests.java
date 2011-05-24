package org.liris.ktbs.tests;

import junit.framework.TestCase;

import org.liris.ktbs.client.ClientFactory;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.service.TraceModelService;

public class LoadTests extends TestCase {

	public void testLoad() throws Exception {
		KtbsClient restClient = Ktbs.getRestClient();
		restClient.getResourceService();
		restClient.getStoredTraceService();
		restClient.getTraceModelService();
		
		KtbsClient memoryClient = Ktbs.getMemoryClient();
		memoryClient.getResourceService();
		memoryClient.getStoredTraceService();
		memoryClient.getTraceModelService();

		ClientFactory provider = Ktbs.getClientFactory();
		KtbsClient client = provider.createRestClient("http://localhost:8001/", "Damien", "");
		ResourceService service = client.getResourceService();
		StoredTraceService traceService = client.getStoredTraceService();
		TraceModelService tmService = client.getTraceModelService();
		
		// deserializtaion test
		IBase base1 = service.getBase("base1");
		
		// serialization test
		base1.addLabel("Toto");
	}
}
