package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

import com.ibm.icu.text.SimpleDateFormat;

public class KtbsJenaTraceTestCase  extends TestCase {

	private Trace traceObsels;
	private Trace traceInfo;
	private Trace filtered1;

	private EmptyResourceFactory emptyFac = EmptyResourceFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/t01.ttl");
		traceObsels = KtbsJenaResourceFactory.getInstance().createStoredTrace(
				"http://localhost:8001/base1/t01/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();

		fis = new FileInputStream("turtle/t01-info.ttl");
		traceInfo = KtbsJenaResourceFactory.getInstance().createStoredTrace(
				"http://localhost:8001/base1/t01/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
		fis = new FileInputStream("turtle/filtered1.ttl");
		filtered1 = KtbsJenaResourceFactory.getInstance().createStoredTrace(
				"http://localhost:8001/base1/filtered1/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
	}

	@Test
	public void testGet() {
		assertNotNull(traceObsels.get(uri("017885b093580cee5e01573953fbd26f")));
		assertNotNull(traceObsels.get(uri("91eda250f267fa93e4ece8f3ed659139")));
		assertNotNull(traceObsels.get(uri("a08667b20cfe4079d02f2f5ad9239575")));
		assertNotNull(traceObsels.get(uri("obs1")));
		assertNull(traceObsels.get("obs1"));
		assertNull(traceObsels.get(uri("obs2")));
	}

	@Test
	public void testListObsels() {
		assertEquals(4,KtbsUtils.count(traceObsels.listObsels()));
		Collection<Obsel> obsels = KtbsUtils.toLinkedList(traceObsels.listObsels());
		assertEquals(4,obsels.size());

		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs2")));
		assertFalse(obsels.contains(emptyFac.createObsel("obs1")));
	}

	@Test
	public void testListObselsLongLong() {
		assertEquals(4,KtbsUtils.count(traceObsels.listObsels(0,Long.MAX_VALUE)));
		Collection<Obsel> obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(0,Long.MAX_VALUE));
		assertEquals(4,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(4,KtbsUtils.count(traceObsels.listObsels(1000,7000)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(1000,7000));
		assertEquals(4,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(3,KtbsUtils.count(traceObsels.listObsels(2000,7000)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(2000,7000));
		assertEquals(3,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(3,KtbsUtils.count(traceObsels.listObsels(1000,5000)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(1000,5000));
		assertEquals(3,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(2,KtbsUtils.count(traceObsels.listObsels(2000,5000)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(2000,5000));
		assertEquals(2,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(2,KtbsUtils.count(traceObsels.listObsels(2001,7000)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(2001,7000));
		assertEquals(2,obsels.size());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(2,KtbsUtils.count(traceObsels.listObsels(1000,4999)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(1000,4999));
		assertEquals(2,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));


		assertEquals(0,KtbsUtils.count(traceObsels.listObsels(7001,Long.MAX_VALUE)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(7001,Long.MAX_VALUE));
		assertEquals(0,obsels.size());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(1,KtbsUtils.count(traceObsels.listObsels(3000,5000)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(3000,5000));
		assertEquals(1,obsels.size());
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));

		assertEquals(1,KtbsUtils.count(traceObsels.listObsels(2000,4000)));
		obsels = KtbsUtils.toLinkedList(traceObsels.listObsels(2000,4000));
		assertEquals(1,obsels.size());
		assertTrue(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575")));
		assertFalse(obsels.contains(emptyFac.createObsel("http://localhost:8001/base1/t01/obs1")));
	}

	@Test
	public void testListSources() {
		assertEquals(0,KtbsUtils.count(traceObsels.listSources()));
		assertEquals(0,KtbsUtils.count(traceInfo.listSources()));
		assertEquals(1,KtbsUtils.count(filtered1.listSources()));
		assertEquals(
				emptyFac.createRelationType("http://localhost:8001/base1/filtered2/"),
				filtered1.listSources().next());

	}

	@Test
	public void testListTransformedTraces() {
		assertEquals(0,KtbsUtils.count(traceObsels.listTransformedTraces()));
		assertEquals(3,KtbsUtils.count(traceInfo.listTransformedTraces()));

		Collection<Trace> c = KtbsUtils.toLinkedList(traceInfo.listTransformedTraces());
		assertTrue(c.contains(emptyFac.createResource("http://localhost:8001/base1/filtered1/")));
		assertTrue(c.contains(emptyFac.createResource("http://localhost:8001/base1/fusioned1/")));
		assertTrue(c.contains(emptyFac.createResource("http://localhost:8001/base1/count2/")));
		
		assertEquals(0,KtbsUtils.count(filtered1.listTransformedTraces()));

	}

	@Test
	public void testGetOrigin() {
		String string = traceInfo.getOrigin();
		assertNotNull(string);
		assertEquals("2010-04-28T18:09:00Z", string);
	}

	@Test
	public void testGetOriginAsDate() {
		Date origin = traceInfo.getOriginAsDate();
		assertNotNull(origin);
		try {
			assertEquals(
					new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse("2010-04-28 20:09:00"),
					origin
			);
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetObsel() {
		assertNotNull(traceObsels.getObsel(uri("017885b093580cee5e01573953fbd26f")));
		assertNotNull(traceObsels.getObsel(uri("91eda250f267fa93e4ece8f3ed659139")));
		assertNotNull(traceObsels.getObsel(uri("a08667b20cfe4079d02f2f5ad9239575")));
		assertNotNull(traceObsels.getObsel(uri("obs1")));
		assertNull(traceObsels.getObsel("obs1"));
		assertNull(traceObsels.getObsel(uri("obs2")));
	}

	@Test
	public void testRemoveObsel() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBase() {
		Base base = traceInfo.getBase();
		assertNotNull(base);
		assertEquals("http://localhost:8001/base1/",base.getURI());
	}

	@Test
	public void testGetTraceModel() {
		TraceModel tm = traceInfo.getTraceModel();
		assertNotNull(tm);
		assertEquals("http://localhost:8001/base1/model1/",tm.getURI());
	}

	@Test
	public void testIsCompliantWithModel() {
		assertTrue(traceInfo.isCompliantWithModel());
	}
	private static String uri(String localName) {
		return "http://localhost:8001/base1/t01/" + localName;
	}

}
