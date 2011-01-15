package org.liris.ktbs.rdf.resource.test;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaComputedTraceTestCase extends AbstractKtbsJenaTestCase {

	private ComputedTrace count1;
	private ComputedTrace filtered1;
	private ComputedTrace filtered2;
	private Method count;
	private Method hello;
	private Base base;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		count1 = loadInRepo(
				count1uri, 
				"count1.ttl", 
				ComputedTrace.class);
		filtered1 = loadInRepo(
				filtered1uri, 
				"filtered1.ttl", 
				ComputedTrace.class);
		loadInRepo(
				fusioned1uri, 
				"fusioned1.ttl", 
				ComputedTrace.class);

		base = repository.createBase("http://localhost:8001/base1/");
		
		filtered2 = base.newComputedTrace(
				"http://localhost:8001/base1/filtered2/", 
				repository.getResource("http://localhost:8001/base1/model1/", TraceModel.class), 
				Method.FILTER, 
				Arrays.asList(new Trace[] {filtered1, count1})
		);
		
		count = loadInRepo(
				countUri,
				"count.ttl", 
				Method.class);
		hello = loadInRepo(
				helloworldUri, 
				"helloworld.ttl", 
				Method.class);
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
				Method.FILTER, 
				filtered1.getMethod());
	}

	@Test
	public void testSetMethod() {

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
