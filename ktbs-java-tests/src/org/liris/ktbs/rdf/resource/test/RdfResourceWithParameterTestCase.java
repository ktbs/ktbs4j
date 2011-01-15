package org.liris.ktbs.rdf.resource.test;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsParameter;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.SimpleKtbsParameter;
import org.liris.ktbs.utils.KtbsUtils;

public class RdfResourceWithParameterTestCase extends AbstractKtbsRdfResourceTestCase {

	private Method method;
	private ComputedTrace trace;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		trace = loadInRepo(
				filtered1,
				"filtered1.ttl", 
				ComputedTrace.class);

		method = loadInRepo(
				helloworldUri,
				"helloworld.ttl", 
				Method.class);
	}

	@Test
	public void testListParameters() {
		assertEquals(2,KtbsUtils.count(trace.listParameters()));
		Collection<KtbsParameter> c = KtbsUtils.toLinkedList(trace.listParameters());

		assertEquals(2,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		
		assertEquals(1,KtbsUtils.count(method.listParameters()));
		assertEquals("script",method.listParameters().next().getName());
		System.out.println(method.listParameters().next().getValue());
	}

	@Test
	public void testSetParameter() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParameter() {
		assertEquals("2000",trace.getParameter("start").getValue());
		assertEquals("42000",trace.getParameter("end").getValue());
		assertEquals(null,trace.getParameter("toto"));
	}

	@Test
	public void testRemoveParameter() {
		fail("Not yet implemented");
	}

}
