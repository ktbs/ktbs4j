package org.liris.ktbs.service.tests;


import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.core.Ktbs;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.service.ResourceService;

public class ResourceManagerGetTestCase extends TestCase {

	private ResourceService manager;
	
	@Before
	public void setUp() throws Exception {
		try {
			manager = Ktbs.getRestClient().getResourceService();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void testGet() {

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
		assertEquals(4, trace1.getObsels().size());

		trace1 = manager.getStoredTrace("base1/t01");
		assertNotNull(trace1);
		assertEquals(4, trace1.getObsels().size());
		
	}

	
}
