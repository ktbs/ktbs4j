package org.liris.ktbs.rdf.resource.test;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaBaseTestCase extends AbstractKtbsJenaTestCase {

	private Base base;
	private Base emptyBase;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		base = loadInRepo(
				"base1.ttl", 
				Base.class);
		emptyBase = loadInRepo(
				"empty-base.ttl", 
				Base.class);
	}

	@Test
	public void testGet() {
		KtbsResource model = base.get(uri("model1"));
		assertNotNull(model);
		assertEquals(uri("model1"),model.getURI());

		KtbsResource t01 = base.get(uri("t01"));
		assertNotNull(t01);
		assertEquals(uri("t01"),t01.getURI());

		KtbsResource fusion = base.get(uri("fusion"));
		assertNotNull(fusion);
		assertEquals(uri("fusion"),fusion.getURI());

		KtbsResource helloworld = base.get(uri("helloworld"));
		assertNotNull(helloworld);
		assertEquals(uri("helloworld"),helloworld.getURI());

		KtbsResource fusioned1 = base.get(uri("fusioned1"));
		assertNotNull(fusioned1);
		assertEquals(uri("fusioned1"),fusioned1.getURI());

		KtbsResource filtered1 = base.get(uri("filtered1"));
		assertNotNull(filtered1);
		assertEquals(uri("filtered1"),filtered1.getURI());

		KtbsResource filtered2 = base.get(uri("filtered2"));
		assertNotNull(filtered2);
		assertEquals(uri("filtered2"),filtered2.getURI());

		KtbsResource helloworld1 = base.get(uri("helloworld1"));
		assertNotNull(helloworld1);
		assertEquals(uri("helloworld1"),helloworld1.getURI());

		KtbsResource count1 = base.get(uri("count1"));
		assertNotNull(count1);
		assertEquals(uri("count1"),count1.getURI());

		KtbsResource count2 = base.get(uri("count2"));
		assertNotNull(count2);
		assertEquals(uri("count2"),count2.getURI());

		KtbsResource bidon = base.get(uri("bidon"));
		assertNull(bidon);
	}

	@Test
	public void testListTraceModels() {
		assertEquals(1,KtbsUtils.count(base.listTraceModels()));
		Collection<TraceModel> c = KtbsUtils.toLinkedList(base.listTraceModels());
		assertTrue(c.contains(emptyFac.createTraceModel(uri("model1"))));
	}

	@Test
	public void testListStoredTraces() {
		assertEquals(1,KtbsUtils.count(base.listStoredTraces()));
		Collection<StoredTrace> c = KtbsUtils.toLinkedList(base.listStoredTraces());
		assertTrue(c.contains(emptyFac.createStoredTrace(uri("t01"))));
	}

	@Test
	public void testListComputedTraces() {
		assertEquals(6,KtbsUtils.count(base.listComputedTraces()));
		Collection<ComputedTrace> c = KtbsUtils.toLinkedList(base.listComputedTraces());
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("count1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("count2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("filtered2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("filtered1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("fusioned1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("helloworld1"))));
	}

	@Test
	public void testListMethods() {
		assertEquals(4,KtbsUtils.count(base.listMethods()));
		Collection<Method> c = new HashSet<Method>(KtbsUtils.toLinkedList(base.listMethods()));
		assertTrue(c.contains(emptyFac.createMethod(uri("count"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("filter"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("fusion"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("helloworld"))));
	}

	@Test
	public void testListTraces() {
		assertEquals(7,KtbsUtils.count(base.listTraces()));
		Collection<Trace> c = new HashSet<Trace>(KtbsUtils.toLinkedList(base.listTraces()));
		assertTrue(c.contains(emptyFac.createStoredTrace(uri("t01"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("count1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("count2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("filtered2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("filtered1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("fusioned1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("helloworld1"))));
	}



	@Test
	public void testListResources() {
		assertEquals(12,KtbsUtils.count(base.listResources()));
		Collection<KtbsResource> c = new HashSet<KtbsResource>(KtbsUtils.toLinkedList(base.listResources()));
		assertTrue(c.contains(emptyFac.createStoredTrace(uri("t01"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("count1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("count2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("filtered2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("filtered1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("fusioned1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("helloworld1"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("count"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("filter"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("fusion"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("helloworld"))));
		assertTrue(c.contains(emptyFac.createTraceModel(uri("model1"))));
	}

	@Test
	public void testNewStoredTrace() {
		fail("Not yet tested since modified");

//		
//		assertEquals(0, KtbsUtils.count(emptyBase.listStoredTraces()));
//
//		StoredTrace t01 = loadInHolder(
//				"base1/t01/", 
//				"t01.ttl", 
//				StoredTrace.class);
//
//		emptyBase.addStoredTrace(t01);
//		assertEquals(1, KtbsUtils.count(emptyBase.listStoredTraces()));
//
//		assertTrue(KtbsUtils.toLinkedList(emptyBase.listStoredTraces()).contains(t01));
//
//		assertEquals(emptyBase,t01.getBase());
	}

	@Test
	public void testNewComputedTrace() {
		fail("Not yet tested since modified");
//		assertEquals(0, KtbsUtils.count(emptyBase.listComputedTraces()));
//
//		ComputedTrace count1 = loadInHolder(
//				"base1/count1/", 
//				"count1.ttl", 
//				ComputedTrace.class);
//
//		ComputedTrace filtered1 = loadInHolder(
//				"base1/filtered1/", 
//				"filtered1.ttl", 
//				ComputedTrace.class);
//		assertFalse(emptyBase.equals(count1.getBase()));
//		assertFalse(emptyBase.equals(filtered1.getBase()));
//
//		emptyBase.addComputedTrace(count1);
//		assertEquals(1, KtbsUtils.count(emptyBase.listComputedTraces()));
//
//		emptyBase.addComputedTrace(filtered1);
//		assertEquals(2, KtbsUtils.count(emptyBase.listComputedTraces()));
//
//		assertTrue(KtbsUtils.toLinkedList(emptyBase.listComputedTraces()).contains(count1));
//		assertTrue(KtbsUtils.toLinkedList(emptyBase.listComputedTraces()).contains(filtered1));
//
//		assertEquals(emptyBase,count1.getBase());
//		assertEquals(emptyBase,filtered1.getBase());

	}

	@Test
	public void testGetTrace() {
		Trace model = base.getTrace(uri("model1"));
		assertNull(model);

		Trace t01 = base.getTrace(uri("t01"));
		assertNotNull(t01);
		assertEquals(uri("t01"),t01.getURI());

		Trace fusion = base.getTrace(uri("fusion"));
		assertNull(fusion);

		Trace helloworld = base.getTrace(uri("helloworld"));
		assertNull(helloworld);

		Trace fusioned1 = base.getTrace(uri("fusioned1"));
		assertNotNull(fusioned1);
		assertEquals(uri("fusioned1"),fusioned1.getURI());

		Trace filtered1 = base.getTrace(uri("filtered1"));
		assertNotNull(filtered1);
		assertEquals(uri("filtered1"),filtered1.getURI());

		Trace filtered2 = base.getTrace(uri("filtered2"));
		assertNotNull(filtered2);
		assertEquals(uri("filtered2"),filtered2.getURI());

		Trace helloworld1 = base.getTrace(uri("helloworld1"));
		assertNotNull(helloworld1);
		assertEquals(uri("helloworld1"),helloworld1.getURI());

		Trace count1 = base.getTrace(uri("count1"));
		assertNotNull(count1);
		assertEquals(uri("count1"),count1.getURI());

		Trace count2 = base.getTrace(uri("count2"));
		assertNotNull(count2);
		assertEquals(uri("count2"),count2.getURI());

		Trace bidon = base.getTrace(uri("bidon"));
		assertNull(bidon);
	}


	@Test
	public void testGetComputedTrace() {
		ComputedTrace model = base.getComputedTrace(uri("model1"));
		assertNull(model);

		ComputedTrace t01 = base.getComputedTrace(uri("t01"));
		assertNull(t01);

		ComputedTrace fusion = base.getComputedTrace(uri("fusion"));
		assertNull(fusion);

		ComputedTrace helloworld = base.getComputedTrace(uri("helloworld"));
		assertNull(helloworld);

		ComputedTrace fusioned1 = base.getComputedTrace(uri("fusioned1"));
		assertNotNull(fusioned1);
		assertEquals(uri("fusioned1"),fusioned1.getURI());

		ComputedTrace filtered1 = base.getComputedTrace(uri("filtered1"));
		assertNotNull(filtered1);
		assertEquals(uri("filtered1"),filtered1.getURI());

		ComputedTrace filtered2 = base.getComputedTrace(uri("filtered2"));
		assertNotNull(filtered2);
		assertEquals(uri("filtered2"),filtered2.getURI());

		ComputedTrace helloworld1 = base.getComputedTrace(uri("helloworld1"));
		assertNotNull(helloworld1);
		assertEquals(uri("helloworld1"),helloworld1.getURI());

		ComputedTrace count1 = base.getComputedTrace(uri("count1"));
		assertNotNull(count1);
		assertEquals(uri("count1"),count1.getURI());

		ComputedTrace count2 = base.getComputedTrace(uri("count2"));
		assertNotNull(count2);
		assertEquals(uri("count2"),count2.getURI());

		ComputedTrace bidon = base.getComputedTrace(uri("bidon"));
		assertNull(bidon);
	}
	@Test
	public void testGetStoredTrace() {
		StoredTrace model = base.getStoredTrace(uri("model1"));
		assertNull(model);

		StoredTrace t01 = base.getStoredTrace(uri("t01"));
		assertNotNull(t01);
		assertEquals(uri("t01"),t01.getURI());

		StoredTrace fusion = base.getStoredTrace(uri("fusion"));
		assertNull(fusion);

		StoredTrace helloworld = base.getStoredTrace(uri("helloworld"));
		assertNull(helloworld);

		StoredTrace fusioned1 = base.getStoredTrace(uri("fusioned1"));
		assertNull(fusioned1);

		StoredTrace filtered1 = base.getStoredTrace(uri("filtered1"));
		assertNull(filtered1);

		StoredTrace filtered2 = base.getStoredTrace(uri("filtered2"));
		assertNull(filtered2);

		StoredTrace helloworld1 = base.getStoredTrace(uri("helloworld1"));
		assertNull(helloworld1);

		StoredTrace count1 = base.getStoredTrace(uri("count1"));
		assertNull(count1);

		StoredTrace count2 = base.getStoredTrace(uri("count2"));
		assertNull(count2);

		StoredTrace bidon = base.getStoredTrace(uri("bidon"));
		assertNull(bidon);
	}

	@Test
	public void testNewTraceModel() {
		fail("Not yet tested since modified");
//		assertEquals(0, KtbsUtils.count(emptyBase.listTraceModels()));
//
//		TraceModel tm = loadInHolder(
//				"base1/model1/", 
//				"model1.ttl", 
//				TraceModel.class);
//
//		emptyBase.addTraceModel(tm);
//		assertEquals(1, KtbsUtils.count(emptyBase.listTraceModels()));
//
//		assertTrue(KtbsUtils.toLinkedList(emptyBase.listTraceModels()).contains(tm));
	}

	@Test
	public void testGetTraceModel() {
		TraceModel model = base.getTraceModel(uri("model1"));
		assertNotNull(model);
		assertEquals(uri("model1"),model.getURI());

		TraceModel filter = base.getTraceModel(uri("filter"));
		assertNull(filter);

		TraceModel fusion = base.getTraceModel(uri("fusion"));
		assertNull(fusion);

		TraceModel helloworld = base.getTraceModel(uri("helloworld"));
		assertNull(helloworld);

		TraceModel fusioned1 = base.getTraceModel(uri("fusioned1"));
		assertNull(fusioned1);

		TraceModel filtered1 = base.getTraceModel(uri("filtered1"));
		assertNull(filtered1);

		TraceModel filtered2 = base.getTraceModel(uri("filtered2"));
		assertNull(filtered2);

		TraceModel helloworld1 = base.getTraceModel(uri("helloworld1"));
		assertNull(helloworld1);

		TraceModel count1 = base.getTraceModel(uri("count1"));
		assertNull(count1);

		TraceModel count2 = base.getTraceModel(uri("count2"));
		assertNull(count2);

		TraceModel bidon = base.getTraceModel(uri("bidon"));
		assertNull(bidon);
	}

	@Test
	public void testNewMethod() {
		fail("Not yet tested since modified");

//		assertEquals(0, KtbsUtils.count(emptyBase.listMethods()));
//
//		Method method1 = loadInHolder(
//				"base1/count/", 
//				"count.ttl", 
//				Method.class);
//
//		Method method2 = loadInHolder(
//				"base1/helloworld/", 
//				"helloworld.ttl", 
//				Method.class);
//
//		emptyBase.addMethod(method1);
//		assertEquals(1, KtbsUtils.count(emptyBase.listMethods()));
//		
//		emptyBase.addMethod(method2);
//		assertEquals(2, KtbsUtils.count(emptyBase.listMethods()));
//
//		assertTrue(KtbsUtils.toLinkedList(emptyBase.listMethods()).contains(method1));
//		assertTrue(KtbsUtils.toLinkedList(emptyBase.listMethods()).contains(method2));
	}

	@Test
	public void testGetMethod() {
		Method count = base.getMethod(uri("count"));
		assertNotNull(count);
		assertEquals(uri("count"),count.getURI());

		Method filter = base.getMethod(uri("filter"));
		assertNotNull(filter);
		assertEquals(uri("filter"),filter.getURI());

		Method fusion = base.getMethod(uri("fusion"));
		assertNotNull(fusion);
		assertEquals(uri("fusion"),fusion.getURI());

		Method helloworld = base.getMethod(uri("helloworld"));
		assertNotNull(helloworld);
		assertEquals(uri("helloworld"),helloworld.getURI());

		Method model = base.getMethod(uri("model1"));
		assertNull(model);

		Method t01 = base.getMethod(uri("t01"));
		assertNull(t01);

		Method fusioned1 = base.getMethod(uri("fusioned1"));
		assertNull(fusioned1);

		Method filtered1 = base.getMethod(uri("filtered1"));
		assertNull(filtered1);

		Method filtered2 = base.getMethod(uri("filtered2"));
		assertNull(filtered2);

		Method helloworld1 = base.getMethod(uri("helloworld1"));
		assertNull(helloworld1);

		Method count1 = base.getMethod(uri("count1"));
		assertNull(count1);

		Method count2 = base.getMethod(uri("count2"));
		assertNull(count2);

		Method bidon = base.getMethod(uri("bidon"));
		assertNull(bidon);
	}

	private String uri(String localName) {
		return base.getURI()+localName+"/";
	}
}
