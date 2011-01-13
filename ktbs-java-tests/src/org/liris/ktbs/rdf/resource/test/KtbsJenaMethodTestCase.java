package org.liris.ktbs.rdf.resource.test;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.Method;

public class KtbsJenaMethodTestCase extends AbstractKtbsJenaTestCase {

	private Method helloworld;
	private Method count;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		helloworld = loadInRepo(
				"helloworld.ttl", 
				Method.class);
		count = loadInRepo(
				"count.ttl", 
				Method.class);
	}

	@Test
	public void testSetInherits() {
		assertEquals(KtbsConstants.SCRIPT_PYTHON, helloworld.getInherits());
		helloworld.setInherits("Hello Space");
		assertEquals("Hello Space", helloworld.getInherits());
	}
	
	@Test
	public void testGetInherits() {
		assertEquals(KtbsConstants.SCRIPT_PYTHON, helloworld.getInherits());
		assertEquals(KtbsConstants.SCRIPT_PYTHON, count.getInherits());
	}

	@Test
	public void testGetETag() {
		assertEquals("34f733259fe1077cd593ef062b16d184", helloworld.getETag());
		assertEquals("d90e04c5a22ae055bdc913dd179af594", count.getETag());
	}
}
