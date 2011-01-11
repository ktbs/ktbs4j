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
		
		helloworld = loadInHolder(
				"base1/helloworld/", 
				"helloworld.ttl", 
				Method.class);
		count = loadInHolder(
				"base1/count/", 
				"count.ttl", 
				Method.class);
	}

	@Test
	public void testSetInherits() {
		fail("Not yet implemented");
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
