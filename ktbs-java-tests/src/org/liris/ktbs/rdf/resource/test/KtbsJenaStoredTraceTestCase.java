package org.liris.ktbs.rdf.resource.test;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaStoredTraceTestCase  extends AbstractKtbsJenaTestCase {


	private StoredTrace traceObsels;
	private StoredTrace traceInfo;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		traceObsels = loadInHolder(
				"base1/t01/", 
				"t01.ttl", 
				StoredTrace.class);

		traceInfo = loadInHolder(
				"base1/t01/", 
				"t01-info.ttl", 
				StoredTrace.class);
	}

	@Test
	public void testSetDefaultSubject() {
		traceInfo.setDefaultSubject("Toto");
		assertEquals("Toto",traceInfo.getDefaultSubject());
		traceInfo.setDefaultSubject("Tota");
		assertEquals("Tota",traceInfo.getDefaultSubject());
	}

	@Test
	public void testRemoveObsel() {
		
		Collection<Obsel> obsels;
		assertEquals(4, KtbsUtils.count(traceObsels.listObsels()));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/obs1"));
		
		traceObsels.removeObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f");
		assertEquals(3, KtbsUtils.count(traceObsels.listObsels()));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/obs1"));
		
		
		traceObsels.removeObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139");
		assertEquals(2, KtbsUtils.count(traceObsels.listObsels()));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/obs1"));
		
		
		traceObsels.removeObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575");
		assertEquals(1, KtbsUtils.count(traceObsels.listObsels()));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/obs1"));
		
		
		traceObsels.removeObsel("http://localhost:8001/base1/t01/obs1");
		assertEquals(0, KtbsUtils.count(traceObsels.listObsels()));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f"));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139"));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575"));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/obs1"));
	}
	
	@Test
	public void testAddObsel() {
		assertFalse(holder.exists("http://localhost:8001/base1/t01/obs5"));
		assertFalse(holder.exists("http://localhost:8001/base1/t01/obs6"));
		Obsel obsel5 = loadInHolder(
				"base1/t01/obs5", 
				"obsel5.ttl", 
				Obsel.class);
		Obsel obsel6 = loadInHolder(
				"base1/t01/obs6", 
				"obsel6.ttl", 
				Obsel.class);
		assertTrue(holder.exists("http://localhost:8001/base1/t01/obs5"));
		assertTrue(holder.exists("http://localhost:8001/base1/t01/obs6"));

		assertEquals(4, KtbsUtils.count(traceObsels.listObsels()));
		assertFalse(KtbsUtils.toLinkedList(traceObsels.listObsels()).contains(obsel5));
		assertFalse(KtbsUtils.toLinkedList(traceObsels.listObsels()).contains(obsel6));
		assertNull(obsel5.getTrace());
		assertNull(obsel6.getTrace());

		traceObsels.addObsel(obsel5);
		assertEquals(5, KtbsUtils.count(traceObsels.listObsels()));
		assertTrue(KtbsUtils.toLinkedList(traceObsels.listObsels()).contains(obsel5));
		assertFalse(KtbsUtils.toLinkedList(traceObsels.listObsels()).contains(obsel6));
		assertTrue(obsel5.getTrace().equals(traceObsels));
		assertNull(obsel6.getTrace());

		traceObsels.addObsel(obsel6);
		assertEquals(6, KtbsUtils.count(traceObsels.listObsels()));
		assertTrue(KtbsUtils.toLinkedList(traceObsels.listObsels()).contains(obsel5));
		assertTrue(KtbsUtils.toLinkedList(traceObsels.listObsels()).contains(obsel6));
		assertTrue(obsel5.getTrace().equals(traceObsels));
		assertTrue(obsel6.getTrace().equals(traceObsels));
	}

	@Test
	public void testGetDefaultSubject() {
		assertEquals("Damien Cram", traceInfo.getDefaultSubject());
		assertEquals(null, traceObsels.getDefaultSubject());
	}
}
