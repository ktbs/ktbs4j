package org.liris.ktbs.rdf.resource.test;

import java.util.Collection;
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
		KtbsResource b = ktbsJenaRoot.get("http://localhost:8001/base2/");
		assertNotNull(b);
		assertTrue(Base.class.isAssignableFrom(b.getClass()));
		assertEquals("http://localhost:8001/base2/",b.getURI());
	}

	@Test
	public void testListBases() {
		Iterator<Base> it = ktbsJenaRoot.listBases();
		boolean containsBase1 = false;
		while(it.hasNext()) {
			Base b = it.next();
			if(b.getURI().equals("http://localhost:8001/base2/"))
				containsBase1 = true;
		}
		assertTrue(containsBase1);
		assertEquals(1,KtbsUtils.count(ktbsJenaRoot.listBases()));
	}

	@Test
	public void testAddBase() {
		Base b = loadInHolder(
				"base1/", 
				"base1.ttl", 
				Base.class);
		assertEquals(1, KtbsUtils.count(ktbsJenaRoot.listBases()));
		Collection<Base> c = KtbsUtils.toLinkedList(ktbsJenaRoot.listBases());
		assertTrue(c.contains(emptyFac.createResource("http://localhost:8001/base2/")));
		assertFalse(c.contains(b));
		ktbsJenaRoot.addBase(b);
		assertEquals(2, KtbsUtils.count(ktbsJenaRoot.listBases()));
		c = KtbsUtils.toLinkedList(ktbsJenaRoot.listBases());
		assertEquals(2, c.size());
		assertTrue(c.contains(emptyFac.createResource("http://localhost:8001/base2/")));
		assertTrue(c.contains(b));
	}

	@Test
	public void testGetBase() {
		Base b = ktbsJenaRoot.getBase("http://localhost:8001/base2/");
		assertEquals("http://localhost:8001/base2/",b.getURI());
	}

}
