package org.liris.ktbs.tests;

import junit.framework.TestCase;

import org.liris.ktbs.utils.KtbsUtils;


public class KtbsUtilsTestCase extends TestCase {
	
	public void testMakeAbsoluteUri() throws Exception {
		assertEquals("http://localhost:8001/base1/t01/", KtbsUtils.makeAbsoluteURI("http://localhost:8001/", "base1/t01", false));
		assertEquals("http://localhost:8001/base1/t01/", KtbsUtils.makeAbsoluteURI("http://localhost:8001/", "/base1/t01/", false));
		assertEquals("http://localhost:8001/base1/t01/", KtbsUtils.makeAbsoluteURI("http://localhost:8001", "/base1/t01/", false));
		assertEquals("http://localhost:8001/base1/t01/", KtbsUtils.makeAbsoluteURI("http://localhost:8001/", "http://localhost:8001/base1/t01/", false));
		assertEquals("http://localhost:8001/base1/t01/", KtbsUtils.makeAbsoluteURI("http://localhost:8001/", "http://localhost:8001/base1/t01", false));
	}

}
