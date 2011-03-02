package org.liris.ktbs.tests;

import org.junit.Before;
import org.liris.ktbs.core.KtbsClient;
import org.liris.ktbs.core.ResourceManager;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;

import junit.framework.TestCase;

public class ResourceManagerSaveTestCase extends TestCase {
	private ResourceManager manager;
	private IRoot root;

	@Before
	public void setUp() throws Exception {
		manager = KtbsClient.getDefaultRestManager();
		root = manager.getKtbsResource("http://localhost:8001/", IRoot.class);
	}

	public void testSaveTraceModel() {
		
	}

	public void testSaveAttributeType() {
		
	}

	public void testSaveStoredTrace() {
		IStoredTrace trace = (IStoredTrace)root.get("base1").get("t01");
		trace.addLabel("Label ajouté");
		assertTrue(manager.saveKtbsResource(trace));
	}

	public void testSaveObsel() {
		
	}
}
