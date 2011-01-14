package org.liris.ktbs.rdf.resource.test;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaRootTestCase extends AbstractKtbsJenaTestCase {

	private KtbsRoot ktbsJenaRoot;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		ktbsJenaRoot = loadInRepo(
				root,
				"ktbsroot.ttl", 
				KtbsRoot.class);
	}

	@Test
	public void testGet() {
		Base b2 = loadInRepo(
				"http://localhost:8001/base2/",
				"base2.ttl", 
				Base.class);
		ktbsJenaRoot.addBase(b2);
		assertTrue(Base.class.isAssignableFrom(b2.getClass()));
		assertEquals("http://localhost:8001/base2/",b2.getURI());
	}

	@Test
	public void testListBases() {
		Iterator<Base> it = ktbsJenaRoot.listBases();
		loadInRepo(
				base1,
				"base1.ttl", 
				Base.class);
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
		Base b = loadInRepo(
				base1,
				"base1.ttl", 
				Base.class);
		ktbsJenaRoot.addBase(b);
		assertEquals(1, KtbsUtils.count(ktbsJenaRoot.listBases()));
		Collection<Base> c = KtbsUtils.toLinkedList(ktbsJenaRoot.listBases());
		assertTrue(c.contains(b));
		Base b2 = loadInRepo(
				"http://localhost:8001/base2/",
				"base2.ttl", 
				Base.class);
		ktbsJenaRoot.addBase(b2);
		assertEquals(2, KtbsUtils.count(ktbsJenaRoot.listBases()));
		c = KtbsUtils.toLinkedList(ktbsJenaRoot.listBases());
		assertEquals(2, c.size());
		assertTrue(c.contains(b2));
		assertTrue(c.contains(b));
	}

	@Test
	public void testGetBase() {
		Base b = loadInRepo(
				base1,
				"base1.ttl", 
				Base.class);
		ktbsJenaRoot.addBase(b);
		Base b2 = ktbsJenaRoot.getBase("http://localhost:8001/base1/");
		assertEquals("http://localhost:8001/base1/",b2.getURI());
	}

}
