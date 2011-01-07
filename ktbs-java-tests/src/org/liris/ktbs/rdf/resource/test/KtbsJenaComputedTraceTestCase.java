package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;

public class KtbsJenaComputedTraceTestCase extends TestCase {

	private ComputedTrace trace;
	
	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/count1.ttl");
		trace = KtbsJenaResourceFactory.getInstance().createComputedTrace(
				"http://localhost:8001/base1/count1/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
	}

	@Test
	public void testGetMethod() {
		assertEquals(EmptyResourceFactory.getInstance().createMethod("http://localhost:8001/base1/count/"), trace.getMethod());
	}
}
