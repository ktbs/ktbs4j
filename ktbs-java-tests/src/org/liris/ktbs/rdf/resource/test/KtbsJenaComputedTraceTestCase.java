package org.liris.ktbs.rdf.resource.test;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaComputedTraceTestCase extends AbstractKtbsJenaTestCase {

	private ComputedTrace count1;
	private ComputedTrace filtered1;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		count1 = loadInHolder(
				"base1/count1/", 
				"count1.ttl", 
				ComputedTrace.class);
		filtered1 = loadInHolder(
				"base1/filtered1/", 
				"filtered1.ttl", 
				ComputedTrace.class);
	}
	
	@Test
	public void testListSources() {
		assertEquals(1,KtbsUtils.count(count1.listSources()));
		assertEquals(1,KtbsUtils.count(filtered1.listSources()));

		assertEquals(
				emptyFac.createTrace("http://localhost:8001/base1/fusioned1/"),
				count1.listSources().next());
		assertEquals(
				emptyFac.createTrace("http://localhost:8001/base1/filtered2/"),
				filtered1.listSources().next());
	}


	@Test
	public void testGetMethod() {
		assertEquals(
				emptyFac.createMethod("http://localhost:8001/base1/count/"), 
				count1.getMethod());
		assertEquals(
				emptyFac.createMethod(KtbsConstants.FILTER), 
				filtered1.getMethod());
	}
	
	@Test
	public void testSetMethod() {
		Method count = loadInHolder("base1/count1/", "count.ttl", Method.class);
		Method hello = loadInHolder("base1/count1/", "helloworld.ttl", Method.class);
		
		count1.setMethod(count);
		assertEquals(count, count1.getMethod());
		count1.setMethod(hello);
		assertEquals(hello, count1.getMethod());
	}

	@Test
	public void testAddSourceTrace() {
		LinkedList<Trace> c = KtbsUtils.toLinkedList(count1.listSources());
		assertEquals(1,c.size());
		
		count1.addSourceTrace(filtered1);
		c = KtbsUtils.toLinkedList(count1.listSources());
		assertEquals(2,c.size());
		assertTrue(c.contains(filtered1));
		
	}
}
