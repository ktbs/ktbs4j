package org.liris.ktbs.rdf.resource.test;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.SimpleKtbsParameter;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsParameter;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.utils.KtbsUtils;

public class RdfResourceWithParameterTestCase extends AbstractKtbsRdfResourceTestCase {

	private Method method;
	private ComputedTrace trace;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		trace = loadInRepo(
				filtered1,
				"filtered1.ttl", 
				ComputedTrace.class);

		method = loadInRepo(
				helloworldUri,
				"helloworld.ttl", 
				Method.class);
	}

	@Test
	public void testListParameters() {
		assertEquals(2,KtbsUtils.count(trace.listParameters()));
		Collection<KtbsParameter> c = KtbsUtils.toLinkedList(trace.listParameters());

		assertEquals(2,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		
		assertEquals(1,KtbsUtils.count(method.listParameters()));
		assertEquals("script",method.listParameters().next().getName());
		System.out.println(method.listParameters().next().getValue());
		
		// test the removing from the iterator
		trace.setParameter("tata", "tonton");
		trace.setParameter("toto", "titi");
		c = KtbsUtils.toLinkedList(trace.listParameters());
		assertEquals(4,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		assertTrue(c.contains(new SimpleKtbsParameter("tata", "tonton")));
		assertTrue(c.contains(new SimpleKtbsParameter("toto", "titi")));
		
		Iterator<KtbsParameter> it = trace.listParameters();
		while (it.hasNext()) {
			KtbsParameter ktbsParameter = (KtbsParameter) it.next();
			if(ktbsParameter.getName().equals("start") || ktbsParameter.getName().equals("tata"))
				it.remove();
		}
		c = KtbsUtils.toLinkedList(trace.listParameters());
		assertEquals(2,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		assertTrue(c.contains(new SimpleKtbsParameter("toto", "titi")));
	}

	@Test
	public void testSetParameter() {
		Collection<KtbsParameter> c = KtbsUtils.toLinkedList(trace.listParameters());
		
		assertEquals(2,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));

		trace.setParameter("toto", "titi");
		c = KtbsUtils.toLinkedList(trace.listParameters());
		assertEquals(3,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		assertTrue(c.contains(new SimpleKtbsParameter("toto", "titi")));

		
		trace.setParameter("tata", "tonton");
		c = KtbsUtils.toLinkedList(trace.listParameters());
		assertEquals(4,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		assertTrue(c.contains(new SimpleKtbsParameter("toto", "titi")));
		assertTrue(c.contains(new SimpleKtbsParameter("tata", "tonton")));

		trace.setParameter("tata", "toto");
		c = KtbsUtils.toLinkedList(trace.listParameters());
		assertEquals(4,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		assertTrue(c.contains(new SimpleKtbsParameter("toto", "titi")));
		assertTrue(c.contains(new SimpleKtbsParameter("tata", "toto")));
	}

	@Test
	public void testGetParameter() {
		assertEquals("2000",trace.getParameter("start").getValue());
		assertEquals("42000",trace.getParameter("end").getValue());
		assertEquals(null,trace.getParameter("toto"));
	}

	@Test
	public void testRemoveParameter() {
		trace.setParameter("toto", "titi");
		trace.setParameter("tata", "tonton");
		Collection<KtbsParameter> c = KtbsUtils.toLinkedList(trace.listParameters());
		assertEquals(4,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("start", "2000")));
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		assertTrue(c.contains(new SimpleKtbsParameter("toto", "titi")));
		assertTrue(c.contains(new SimpleKtbsParameter("tata", "tonton")));

		trace.removeParameter("start");
		trace.removeParameter("tata");
		
		
		c = KtbsUtils.toLinkedList(trace.listParameters());
		assertEquals(2,c.size());
		assertTrue(c.contains(new SimpleKtbsParameter("end", "42000")));
		assertTrue(c.contains(new SimpleKtbsParameter("toto", "titi")));
	}

}
