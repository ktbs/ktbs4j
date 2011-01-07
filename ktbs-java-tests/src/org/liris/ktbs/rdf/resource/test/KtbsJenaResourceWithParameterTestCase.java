package org.liris.ktbs.rdf.resource.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsParameter;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.SimpleKtbsParameter;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaResourceWithParameterTestCase {

	private Method method;
	private ComputedTrace trace;
	
	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/filtered1.ttl");
		trace = KtbsJenaResourceFactory.getInstance().createComputedTrace(
				"http://localhost:8001/base1/filtered1/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();

		fis = new FileInputStream("turtle/helloworld.ttl");
		method = KtbsJenaResourceFactory.getInstance().createMethod(
				"http://localhost:8001/base1/helloworld/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
	}

	@Test
	public void testListParameters() {
		assertEquals(2,KtbsUtils.count(trace.listParameters()));
		Collection<KtbsParameter> c = KtbsUtils.toLinkedList(trace.listParameters());

		assertEquals(2,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
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
