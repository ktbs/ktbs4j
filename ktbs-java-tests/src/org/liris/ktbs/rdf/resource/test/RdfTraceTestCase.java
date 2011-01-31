package org.liris.ktbs.rdf.resource.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

public class RdfTraceTestCase  extends AbstractKtbsRdfResourceTestCase {

	private String t01uri = "http://localhost:8001/base1/t01/";
	
	private Trace t01;
	private Trace filtered1;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		loadInRepo(
				t01uri,
				"t01-info.ttl", 
				StoredTrace.class);
		
		t01 = loadInRepo(
				t01uri,
				"t01.ttl", 
				StoredTrace.class);

		filtered1 = loadInRepo(
				filtered1uri, 
				"filtered1.ttl", 
				ComputedTrace.class);
		
	}

	@Test
	public void testGet() {
		assertNotNull(t01.get(uri("017885b093580cee5e01573953fbd26f")));
		assertNotNull(t01.get(uri("91eda250f267fa93e4ece8f3ed659139")));
		assertNotNull(t01.get(uri("a08667b20cfe4079d02f2f5ad9239575")));
		assertNotNull(t01.get(uri("obs1")));
		assertNull(t01.get("obs1"));
		assertNull(t01.get(uri("obs2")));
	}

	@Test
	public void testListObsels() {
		assertEquals(4,KtbsUtils.count(t01.listObsels()));
		Collection<Obsel> obsels = KtbsUtils.toLinkedList(t01.listObsels());
		assertEquals(4,obsels.size());

		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs2")));
		assertFalse(obsels.contains(emptyFac.createObsel("obs1")));
	}

	@Test
	public void testListObselsBigIntegerBigInteger() {
		assertEquals(4,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(0),BigInteger.valueOf(Long.MAX_VALUE))));
		Collection<Obsel> obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(0),BigInteger.valueOf(Long.MAX_VALUE)));
		assertEquals(4,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

//		assertEquals(4,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(1000),BigInteger.valueOf(7000))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(1000),BigInteger.valueOf(7000)));
		assertEquals(4,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(3,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(2000),BigInteger.valueOf(7000))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(2000),BigInteger.valueOf(7000)));
		assertEquals(3,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(3,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(1000),BigInteger.valueOf(5000))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(1000),BigInteger.valueOf(5000)));
		assertEquals(3,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(2,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(2000),BigInteger.valueOf(5000))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(2000),BigInteger.valueOf(5000)));
		assertEquals(2,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(2,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(2001),BigInteger.valueOf(7000))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(2001),BigInteger.valueOf(7000)));
		assertEquals(2,obsels.size());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(2,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(1000),BigInteger.valueOf(4999))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(1000),BigInteger.valueOf(4999)));
		assertEquals(2,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));


		assertEquals(0,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(7001),BigInteger.valueOf(Long.MAX_VALUE))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(7001),BigInteger.valueOf(Long.MAX_VALUE)));
		assertEquals(0,obsels.size());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(1,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(3000),BigInteger.valueOf(5000))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(3000),BigInteger.valueOf(5000)));
		assertEquals(1,obsels.size());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(1,KtbsUtils.count(t01.listObsels(BigInteger.valueOf(2000),BigInteger.valueOf(4000))));
		obsels = KtbsUtils.toLinkedList(t01.listObsels(BigInteger.valueOf(2000),BigInteger.valueOf(4000)));
		assertEquals(1,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
	}


	@Test
	public void testListTransformedTraces() {
		Base base = repository.createBase("http://localhost:8001/base1/");
		repository.createComputedTrace(
				base, 
				"http://localhost:8001/base1/count2/", 
				(TraceModel)repository.getResource("http://localhost:8001/base1/model1/"),
				base.newMethod("http://localhost:8001/base1/methodtoto/", KtbsConstants.SCRIPT_PYTHON),
				Arrays.asList(new Trace[]{filtered1,t01})
				);
		repository.createComputedTrace(
				base, 
				"http://localhost:8001/base1/fusioned1/", 
				(TraceModel)repository.getResource("http://localhost:8001/base1/model1/"),
				base.newMethod("http://localhost:8001/base1/methodtoto/", KtbsConstants.SCRIPT_PYTHON),
				Arrays.asList(new Trace[]{filtered1,t01})
		);
		
		
		assertEquals(3,KtbsUtils.count(t01.listTransformedTraces()));
		assertEquals(3,KtbsUtils.count(t01.listTransformedTraces()));

		Collection<Trace> c = KtbsUtils.toLinkedList(t01.listTransformedTraces());
		assertTrue(c.contains(emptyFac.createResource("http://localhost:8001/base1/filtered1/")));
		assertTrue(c.contains(emptyFac.createResource("http://localhost:8001/base1/fusioned1/")));
		assertTrue(c.contains(emptyFac.createResource("http://localhost:8001/base1/count2/")));
		
		assertEquals(0,KtbsUtils.count(filtered1.listTransformedTraces()));
	}

	@Test
	public void testSetOrigin() {
		
		t01.setOrigin("toto");
		assertNotNull(t01.getOrigin());
		assertEquals("toto", t01.getOrigin());
	}
	
	@Test
	public void testGetOrigin() {
		String string = t01.getOrigin();
		assertNotNull(string);
		assertEquals("2010-04-28T18:09:00Z", string);
	}

	@Test
	public void testSetOriginAsDate() {
		Date d = new Date();
		t01.setOriginAsDate(d);
		assertNotNull(t01.getOriginAsDate());
		assertEquals(d, t01.getOriginAsDate());
		
	}
	@Test
	public void testGetOriginAsDate() {
		Date origin = t01.getOriginAsDate();
		assertNotNull(origin);
		assertDateEquals(origin,2010,4,28,20,9,0);
	}


	@Test
	public void testGetObsel() {
		assertNotNull(t01.getObsel(uri("017885b093580cee5e01573953fbd26f")));
		assertNotNull(t01.getObsel(uri("91eda250f267fa93e4ece8f3ed659139")));
		assertNotNull(t01.getObsel(uri("a08667b20cfe4079d02f2f5ad9239575")));
		assertNotNull(t01.getObsel(uri("obs1")));
		assertNull(t01.getObsel(uri("obs2")));
	}

	@Test
	public void testSetTraceModel() {
		TraceModel tm1 = emptyFac.createTraceModel("http://localhost:8001/base1/model1/");
		TraceModel tm2 = loadInRepo(
				"http://localhost:8001/ma-base/model2/", 
				"model2.ttl", 
				TraceModel.class);
		
		assertEquals(tm1, t01.getTraceModel());
		t01.setTraceModel(tm2);
		assertEquals(tm2, t01.getTraceModel());
	}
	
	@Test
	public void testGetTraceModel() {
		TraceModel tm = t01.getTraceModel();
		assertNotNull(tm);
		assertEquals("http://localhost:8001/base1/model1/",tm.getURI());
	}

	@Test
	public void testIsCompliantWithModel() {
		assertTrue(t01.isCompliantWithModel());
	}
	private static String uri(String localName) {
		return "http://localhost:8001/base1/t01/" + localName;
	}

}
