package org.liris.ktbs.service.tests;


import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.test.utils.KtbsServer;
import org.liris.ktbs.utils.KtbsUtils;

public class ResourceManagerGetTestCase extends TestCase {

	private ResourceService service;

	private KtbsServer ktbsServer;
	@Before
	public void setUp() throws Exception {
		ktbsServer = KtbsServer.newInstance("http://localhost:8001/", System.err);
		ktbsServer.start();
		ktbsServer.populateKtbs();
		ktbsServer.populateT01();
		service = Ktbs.getRestClient().getResourceService();
	}
	
	@Override
	protected void tearDown() throws Exception {
		ktbsServer.stop();
		super.tearDown();
	}

	public void testCreateDeleteSave() {

		// generate an underlying GET request
		IBase base1 = service.getBase("/base1/");

		// generate an underlying POST request
		service.newStoredTrace(
				base1.getUri(), 
				"t02", 
				"http://localhost:8001/base1/model1/", 
				KtbsUtils.now(), 
				null,
				null,
				null,
				null,
		"Damien Cram");

		// generate an underlying GET request
		IStoredTrace t02 = service.getStoredTrace("/base1/t02/");

		// modify t02 locally
		t02.setDefaultSubject("Tonton Nestor");

		// modify t02 remotely (underlying UPDATE)
		service.saveResource(t02);

		// delete t02 remotely (underlying DELETE)
		service.deleteResource(t02.getUri() + "@about");

		// but t02 is still in base1 locally
		Set<IStoredTrace> storedTraces = base1.getStoredTraces();
		
		// Will be true when the delete is implemented on the server
		assertFalse(storedTraces.contains(t02));
	}

	public void testGetAnReturn() {
		// generate an underlying GET request
		IStoredTrace t01 = service.getStoredTrace("/base1/t01/");

		// generate an underlying GET request
		IObsel obs1 = t01.get("obs1");

		// generate an underlying GET request
		ITrace t01Again = (ITrace) obs1.getParentResource();

		// t01 and t01Again represent the same remote trace
		assertEquals(t01.getUri(), t01Again.getUri());

		// but are not the same Java obect
		assertTrue(t01 != t01Again);
	}

	public void testGet() {

		IRoot root = service.getRoot();
		assertNotNull(root);

		IBase base1 = root.get("base1");
		assertNotNull(base1);
		assertEquals(1, base1.getStoredTraces().size());

		base1 = service.getBase("base1");
		assertNotNull(base1);
		assertEquals(1, base1.getStoredTraces().size());

		IStoredTrace trace1 = (IStoredTrace) base1.get("t01");
		assertNotNull(trace1);
		assertEquals(4, trace1.getObsels().size());

		trace1 = service.getStoredTrace("base1/t01");
		assertNotNull(trace1);
		assertEquals(4, trace1.getObsels().size());

	}


}
