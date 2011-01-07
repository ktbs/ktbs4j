package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;
import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;
import org.liris.ktbs.rdf.KtbsJenaResourceHolderImpl;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaRootTestCase extends TestCase {

	private KtbsRoot ktbsJenaRoot;
	private KtbsJenaResourceHolder holder;
	
	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/ktbsroot.ttl");
		holder = new KtbsJenaResourceHolderImpl();
		ktbsJenaRoot = holder.loadResourceFromStream(
				"http://localhost:8001/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE,
				KtbsRoot.class);
		fis.close();
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
		try {
			ktbsJenaRoot.addBase(
					holder.getResource("http://localhost:8001/base1/", Base.class),
					"Damien");
			fail("Should fail with a read-only exception.");
		} catch(ReadOnlyObjectException e) {
			
		} catch(Exception e) {
			fail("Unexpected exception");
		}
	}

	@Test
	public void testGetBase() {
		Base b = ktbsJenaRoot.getBase("http://localhost:8001/base1/");
		assertEquals("http://localhost:8001/base1/",b.getURI());
	}

}
