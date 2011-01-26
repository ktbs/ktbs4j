package org.liris.ktbs.rdf.resource.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

public class RdfBaseTestCase extends AbstractKtbsRdfResourceTestCase {

	private Base base;
	private Base emptyBase;

	private TraceModel model1;
	private TraceModel model2;
	
	private Method method1;
	private Method method2;
	private Method method3;
	
	private StoredTrace trace1;

	private ComputedTrace ct1;
	private ComputedTrace ct2;
	private ComputedTrace ct3;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();

		base = repository.createBase("http://localhost:8001/base1/");
		model1 = base.newTraceModel(uri("model1"));
		model2 = base.newTraceModel(uri("model2"));
		
		trace1 = base.newStoredTrace(uri("t01"),model2, "Nestor");

		method1 = base.newMethod(uri("method1"), "http://example/method1/");
		method2 = base.newMethod(uri("method2"), "http://example/method2/");
		method3 = base.newMethod(uri("method3"), "http://example/method3/");
		
		ct1 = base.newComputedTrace(uri("ct1"), model1, method1, Arrays.asList(new Trace[]{trace1}));
		ct2 = base.newComputedTrace(uri("ct2"), model2, method2, Arrays.asList(new Trace[]{trace1, ct1}));
		ct3 = base.newComputedTrace(uri("ct3"), model2, method3, Arrays.asList(new Trace[]{ct1, ct2}));
		
	}

	@Test
	public void testGet() {
		KtbsResource model = base.get(uri("model1"));
		assertNotNull(model);
		assertEquals(uri("model1"),model.getURI());
		assertTrue(TraceModel.class.isAssignableFrom(base.get(uri("model1")).getClass()));

		KtbsResource t01 = base.get(uri("t01"));
		assertNotNull(t01);
		assertEquals(uri("t01"),t01.getURI());
		assertTrue(StoredTrace.class.isAssignableFrom(base.get(uri("t01")).getClass()));
		
		
		KtbsResource fusion = base.get(uri("method1"));
		assertNotNull(fusion);
		assertEquals(uri("method1"),fusion.getURI());
		assertTrue(Method.class.isAssignableFrom(base.get(uri("method1")).getClass()));
		
		
		KtbsResource helloworld = base.get(uri("method2"));
		assertNotNull(helloworld);
		assertEquals(uri("method2"),helloworld.getURI());
		assertTrue(Method.class.isAssignableFrom(base.get(uri("method2")).getClass()));
		
		KtbsResource fusioned1 = base.get(uri("ct1"));
		assertNotNull(fusioned1);
		assertEquals(uri("ct1"),fusioned1.getURI());
		assertTrue(ComputedTrace.class.isAssignableFrom(base.get(uri("ct1")).getClass()));
		
		KtbsResource t2 = base.get(uri("ct2"));
		assertNotNull(t2);
		assertEquals(uri("ct2"),t2.getURI());
		assertTrue(ComputedTrace.class.isAssignableFrom(base.get(uri("ct2")).getClass()));

		KtbsResource bidon = base.get(uri("bidon"));
		assertNull(bidon);
	}

	@Test
	public void testListTraceModels() {
		assertEquals(2,KtbsUtils.count(base.listTraceModels()));
		Collection<TraceModel> c = KtbsUtils.toLinkedList(base.listTraceModels());
		assertTrue(c.contains(emptyFac.createTraceModel(uri("model1"))));
		assertTrue(c.contains(emptyFac.createTraceModel(uri("model2"))));
	}

	@Test
	public void testListStoredTraces() {
		assertEquals(1,KtbsUtils.count(base.listStoredTraces()));
		Collection<StoredTrace> c = KtbsUtils.toLinkedList(base.listStoredTraces());
		assertTrue(c.contains(emptyFac.createStoredTrace(uri("t01"))));
	}

	@Test
	public void testListComputedTraces() {
		assertEquals(3,KtbsUtils.count(base.listComputedTraces()));
		Collection<ComputedTrace> c = KtbsUtils.toLinkedList(base.listComputedTraces());
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct3"))));
	}

	@Test
	public void testListMethods() {
		assertEquals(3,KtbsUtils.count(base.listMethods()));
		Collection<Method> c = new HashSet<Method>(KtbsUtils.toLinkedList(base.listMethods()));
		assertTrue(c.contains(emptyFac.createMethod(uri("method1"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("method2"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("method3"))));
	}

	@Test
	public void testListTraces() {
		assertEquals(4,KtbsUtils.count(base.listTraces()));
		Collection<Trace> c = new HashSet<Trace>(KtbsUtils.toLinkedList(base.listTraces()));
		assertTrue(c.contains(emptyFac.createStoredTrace(uri("t01"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct3"))));
	}



	@Test
	public void testListResources() {
		assertEquals(9,KtbsUtils.count(base.listResources()));
		Collection<KtbsResource> c = new HashSet<KtbsResource>(KtbsUtils.toLinkedList(base.listResources()));
		assertTrue(c.contains(emptyFac.createStoredTrace(uri("t01"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct1"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct2"))));
		assertTrue(c.contains(emptyFac.createComputedTrace(uri("ct3"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("method1"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("method2"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("method3"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("model1"))));
		assertTrue(c.contains(emptyFac.createMethod(uri("model2"))));
	}

	@Test
	public void testNewStoredTrace() {
		assertEquals(1,KtbsUtils.count(base.listStoredTraces()));		

	}
	
	@Test
	public void testNewComputedTrace() {
		assertEquals(3,KtbsUtils.count(base.listComputedTraces()));		
	}

	@Test
	public void testGetTrace() {
		Trace model = base.getTrace(uri("model1"));
		assertNull(model);

		Trace t01 = base.getTrace(uri("t01"));
		assertNotNull(t01);
		assertEquals(uri("t01"),t01.getURI());

		Trace method1 = base.getTrace(uri("method1"));
		assertNull(method1);


		Trace ct1 = base.getTrace(uri("ct1"));
		assertNotNull(ct1);
		assertEquals(uri("ct1"),ct1.getURI());
		Trace ct2 = base.getTrace(uri("ct2"));
		assertNotNull(ct2);
		assertEquals(uri("ct2"),ct2.getURI());
		Trace ct3 = base.getTrace(uri("ct3"));
		assertNotNull(ct3);
		assertEquals(uri("ct3"),ct3.getURI());

		Trace bidon = base.getTrace(uri("bidon"));
		assertNull(bidon);
	}


	@Test
	public void testGetComputedTrace() {
		ComputedTrace model = base.getComputedTrace(uri("model1"));
		assertNull(model);

		ComputedTrace t01 = base.getComputedTrace(uri("t01"));
		assertNull(t01);

		ComputedTrace method1 = base.getComputedTrace(uri("method1"));
		assertNull(method1);

		ComputedTrace ct1 = base.getComputedTrace(uri("ct1"));
		assertNotNull(ct1);
		assertEquals(uri("ct1"),ct1.getURI());

		ComputedTrace ct2 = base.getComputedTrace(uri("ct2"));
		assertNotNull(ct2);
		assertEquals(uri("ct2"),ct2.getURI());

		ComputedTrace ct3 = base.getComputedTrace(uri("ct3"));
		assertNotNull(ct3);
		assertEquals(uri("ct3"),ct3.getURI());

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
		assertEquals(2,KtbsUtils.count(base.listTraceModels()));		
		
	}

	@Test
	public void testGetTraceModel() {
		
		
		TraceModel model = base.getTraceModel(uri("model1"));
		assertNotNull(model);
		assertEquals(uri("model1"),model.getURI());
		assertEquals(model1,model);

		model = base.getTraceModel(uri("model2"));
		assertNotNull(model);
		assertEquals(uri("model2"),model.getURI());
		assertEquals(model2,model);

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
		assertEquals(3,KtbsUtils.count(base.listMethods()));		

	}

	@Test
	public void testGetMethod() {
		Method count = base.getMethod(uri("method1"));
		assertNotNull(count);
		assertEquals(uri("method1"),count.getURI());

		Method filter = base.getMethod(uri("method2"));
		assertNotNull(filter);
		assertEquals(uri("method2"),filter.getURI());

		Method fusion = base.getMethod(uri("method3"));
		assertNotNull(fusion);
		assertEquals(uri("method3"),fusion.getURI());

		Method model = base.getMethod(uri("model1"));
		assertNull(model);

		Method t01 = base.getMethod(uri("t01"));
		assertNull(t01);

		Method ct1 = base.getMethod(uri("ct1"));
		assertNull(ct1);

		Method ct2 = base.getMethod(uri("ct2"));
		assertNull(ct2);

		Method bidon = base.getMethod(uri("bidon"));
		assertNull(bidon);
	}

	private String uri(String localName) {
		return base.getURI()+localName+"/";
	}
}
