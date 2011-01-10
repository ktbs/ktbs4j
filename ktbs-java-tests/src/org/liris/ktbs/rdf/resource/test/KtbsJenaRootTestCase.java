package org.liris.ktbs.rdf.resource.test;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaRootTestCase extends AbstractKtbsJenaTestCase {

	private KtbsRoot ktbsJenaRoot;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		ktbsJenaRoot = loadInHolder(
				"", 
				"ktbsroot.ttl", 
				KtbsRoot.class);
	}

	@Test
	public void testGet() {
		KtbsResource b = ktbsJenaRoot.get("http://localhost:8001/base1/");
		assertNotNull(b);
		assertTrue(Base.class.isAssignableFrom(b.getClass()));
		assertEquals("http://localhost:8001/base1/",b.getURI());
	}

	@Test
	public void testListBases() {
		Iterator<Base> it = ktbsJenaRoot.listBases();
		boolean containsBase1 = false;
		while(it.hasNext()) {
			Base b = it.next();
			if(b.getURI().equals("http://localhost:8001/base1/"))
				containsBase1 = true;
		}
		assertTrue(containsBase1);
		assertEquals(1,KtbsUtils.count(ktbsJenaRoot.listBases()));
	}

	@Test
	public void testAddBase() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBase() {
		Base b = ktbsJenaRoot.getBase("http://localhost:8001/base1/");
		assertEquals("http://localhost:8001/base1/",b.getURI());
	}

}
