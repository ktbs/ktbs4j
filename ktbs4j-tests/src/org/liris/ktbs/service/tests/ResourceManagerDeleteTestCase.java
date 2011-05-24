package org.liris.ktbs.service.tests;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.service.ResourceService;

public class ResourceManagerDeleteTestCase extends TestCase {

private ResourceService manager;
	
	
	@Before
	public void setUp() throws Exception {
			manager = Ktbs.getRestClient().getResourceService();
	}

	public void testDelete() {

		IRoot root = manager.getRoot();
		assertNotNull(root);
		
		IBase base1 = root.get("base1");
		assertNotNull(base1);
		assertEquals(1, base1.getStoredTraces().size());

		base1 = manager.getBase("base1");
		assertNotNull(base1);
		assertEquals(1, base1.getStoredTraces().size());

		IStoredTrace trace1 = (IStoredTrace) base1.get("t01");
		assertNotNull(trace1);

		trace1 = manager.getStoredTrace("base1/t01");
		assertNotNull(trace1);
		

		// cannot delete obsels
		
		
		assertTrue(manager.deleteResource(trace1));
		assertNull(manager.getStoredTrace("base1/t01"));
		
		base1 = manager.getBase("base1");
		assertNotNull(base1);
		assertEquals(0, base1.getStoredTraces().size());
		assertTrue(manager.deleteResource(base1));
		assertNull(manager.getBase("base1"));
		
		
	}
}
