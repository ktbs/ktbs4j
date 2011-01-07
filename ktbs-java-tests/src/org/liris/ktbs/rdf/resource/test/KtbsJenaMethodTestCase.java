package org.liris.ktbs.rdf.resource.test;

import static org.junit.Assert.*;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.KtbsConstants;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;

public class KtbsJenaMethodTestCase {

	private Method helloworld;
	private Method count;

	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/helloworld.ttl");
		helloworld = KtbsJenaResourceFactory.getInstance().createMethod(
				"http://localhost:8001/base1/helloworld/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();

		fis = new FileInputStream("turtle/count.ttl");
		count = KtbsJenaResourceFactory.getInstance().createMethod(
				"http://localhost:8001/base1/count/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
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
