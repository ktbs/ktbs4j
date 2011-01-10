package org.liris.ktbs.utils.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.liris.ktbs.core.SimpleKtbsParameter;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsUtilsTestCase {

	@Test
	public void testResolveLocalName() {
		String uri1 = "http://localhost:8001/base1/model1/AbstractMsg";
		String uri2 = "http://localhost:8001/base1/model1/";
		assertEquals("AbstractMsg", KtbsUtils.resolveLocalName(uri1));
		assertEquals("model1", KtbsUtils.resolveLocalName(uri2));
	}

	@Test
	public void testResolveParentURI() {
		String uri1 = "http://localhost:8001/base1/model1/AbstractMsg";
		String uri2 = "http://localhost:8001/base1/model1/";
		String uri3 = "http://localhost:8001/base1/model1/base2/model1/";
		assertEquals("http://localhost:8001/base1/model1/", KtbsUtils.resolveParentURI(uri1));
		assertEquals("http://localhost:8001/base1/", KtbsUtils.resolveParentURI(uri2));
		assertEquals("http://localhost:8001/base1/model1/base2/", KtbsUtils.resolveParentURI(uri3));
	}
	
	
	@Test
	public void testParseParameter() {
		assertEquals(new SimpleKtbsParameter("a", "b"), KtbsUtils.parseParameter("a=b"));
		assertEquals(new SimpleKtbsParameter("a\"", "b\"\""), KtbsUtils.parseParameter("a\"=b\"\""));
		assertEquals(new SimpleKtbsParameter("a", "=b"), KtbsUtils.parseParameter("a==b"));
		assertEquals(new SimpleKtbsParameter("a", "b=c"), KtbsUtils.parseParameter("a=b=c"));
		assertEquals(new SimpleKtbsParameter("a", ""), KtbsUtils.parseParameter("a="));
		assertEquals(new SimpleKtbsParameter("", "b"), KtbsUtils.parseParameter("=b"));
			
	}
}
