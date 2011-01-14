package org.liris.ktbs.rdf.resource.test;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaStoredTraceTestCase  extends AbstractKtbsJenaTestCase {


	private StoredTrace t01;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		loadInRepo(
				"http://localhost:8001/base1/t01/",
				"t01-info.ttl", 
				StoredTrace.class);
		t01 = loadInRepo(
				"http://localhost:8001/base1/t01/",
				"t01.ttl", 
				StoredTrace.class);
	}

	@Test
	public void testSetDefaultSubject() {
		t01.setDefaultSubject("Toto");
		assertEquals("Toto",t01.getDefaultSubject());
		t01.setDefaultSubject("Tota");
		assertEquals("Tota",t01.getDefaultSubject());
	}

	@Test
	public void testRemoveObsel() {
		
		Collection<Obsel> obsels;
		assertEquals(4, KtbsUtils.count(t01.listObsels()));
		obsels = KtbsUtils.toLinkedList(t01.listObsels());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/obs1"));
		
		t01.removeObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f");
		assertEquals(3, KtbsUtils.count(t01.listObsels()));
		obsels = KtbsUtils.toLinkedList(t01.listObsels());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/obs1"));
		
		
		t01.removeObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139");
		assertEquals(2, KtbsUtils.count(t01.listObsels()));
		obsels = KtbsUtils.toLinkedList(t01.listObsels());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/obs1"));
		
		
		t01.removeObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575");
		assertEquals(1, KtbsUtils.count(t01.listObsels()));
		obsels = KtbsUtils.toLinkedList(t01.listObsels());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertTrue(repository.exists("http://localhost:8001/base1/t01/obs1"));
		
		
		t01.removeObsel("http://localhost:8001/base1/t01/obs1");
		assertEquals(0, KtbsUtils.count(t01.listObsels()));
		obsels = KtbsUtils.toLinkedList(t01.listObsels());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertFalse(repository.exists("http://localhost:8001/base1/t01/obs1"));
	}
	
	@Test
	public void testNewObsel() {
		assertEquals(4, KtbsUtils.count(t01.listObsels()));
		
		Obsel o = t01.newObsel(
				"http://localhost:8001/base1/t01/obs2", 
				(ObselType)repository.getResource("http://localhost:8001/base1/model1/SendMsg"), 
				null);
		
		assertEquals(5, KtbsUtils.count(t01.listObsels()));
		assertTrue(KtbsUtils.toLinkedList(t01.listObsels()).contains(o));
	}

	@Test
	public void testGetDefaultSubject() {
		assertEquals("Damien Cram", t01.getDefaultSubject());
	}
}
