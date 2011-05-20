package org.liris.ktbs.service.tests;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import junit.framework.TestCase;

import org.liris.ktbs.client.ClientFactory;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.examples.KtbsClientExample2;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.service.impl.ObselBuilder;

public class StoredTraceServiceTestCase extends TestCase {

	private StoredTraceService storedTraceService;
	private ResourceService resourceService;

	@Override
	protected void setUp() throws Exception {
		ClientFactory service = Ktbs.getClientFactory();
		KtbsClient clientDamien = service.createRestClient("http://localhost:8001/", "Damien", "dtotio");
		storedTraceService = clientDamien.getStoredTraceService();
		resourceService = clientDamien.getResourceService();
		
		// init the trace model
		ITraceModel model = resourceService.getTraceModel("http://localhost:8001/base1/visuModel/");
		if(model == null)
			KtbsClientExample2.create("http://localhost:8001/base1/", "visuModel");
	}

	private void testNewObsel2() {
		KtbsClient ktbsClient = Ktbs.getRestClient();

		// get the resource service
		ResourceService resourceService = ktbsClient.getResourceService();
		
		// retrieve the stored trace t01
		IStoredTrace t01 = resourceService.getStoredTrace("/base1/t01/");
		
		// get the stored trace service
		StoredTraceService storedTraceService = ktbsClient.getStoredTraceService();
		
		// collect a new anonymous obsel with begin time and end time set to 1000
		storedTraceService.newObsel(t01, "http://localhost:8001/base1/model1/OpenChat", 1000);
		
		// collect a new anonymous obsel with begin time and end time set to 3000
		// and with two attributes
		storedTraceService.newObsel(t01, "http://localhost:8001/base1/model1/SendMsg", 3000, new Object[]{
				"http://localhost:8001/base1/model1/message", "Hello world !",
				"http://localhost:8001/base1/model1/lang", "english"
		});
	}

	private void testNewObsel3() {
		KtbsClient ktbsClient = Ktbs.getRestClient();
		
		// get the resource service
		ResourceService resourceService = ktbsClient.getResourceService();
		
		// retrieve the stored trace t01
		IStoredTrace t01 = resourceService.getStoredTrace("/base1/t01/");
		// get the stored trace service
		StoredTraceService storedTraceService = ktbsClient.getStoredTraceService();
		
		// open a new obsel buffer for the trace t01
		storedTraceService.startBufferedCollect(t01);
		
		// collect some obsels
		storedTraceService.newObsel(t01, "http://localhost:8001/base1/model1/OpenChat", 1000);
		storedTraceService.newObsel(t01, "http://localhost:8001/base1/model1/SendMsg", 3000, new Object[]{
				"http://localhost:8001/base1/model1/message", "Hello world !",
				"http://localhost:8001/base1/model1/lang", "english"
		});
		
		// create all obsels in t01's buffer
		storedTraceService.postBufferedObsels(t01);
		
		
	}
	
	public void testNewObsel() {
		
		String traceUri = storedTraceService.newStoredTrace(
				"http://localhost:8001/base1/", 
				"http://localhost:8001/base1/visuModel/", 
		"Damien");
		IStoredTrace trace = resourceService.getStoredTrace(traceUri);
		
		Set<IObsel> set = new HashSet<IObsel>();
		
		set.add(Ktbs.getPojoFactory().createObsel(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroWorkbenchEvent", 1000)));
		set.add(Ktbs.getPojoFactory().createObsel(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroViewResizeEvent", 2000)));
		set.add(Ktbs.getPojoFactory().createObsel(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroDoubleClickTraceLineStartCreateCommentEvent", 4000)));
		set.add(Ktbs.getPojoFactory().createObsel(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroWorkbenchEvent", 4500)));
		set.add(Ktbs.getPojoFactory().createObsel(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroViewResizeEvent", 98000)));
		set.add(Ktbs.getPojoFactory().createObsel(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroDoubleClickTraceLineStartCreateCommentEvent", 101000)));
		set.add(Ktbs.getPojoFactory().createObsel(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroWorkbenchEvent", 110000)));
		
		ITrace traceOnServer = resourceService.getStoredTrace(trace.getUri());
		
		assertEquals(set, traceOnServer.getObsels());
		
		ObselBuilder builder = storedTraceService.newObselBuilder(trace);
		builder.setType("http://localhost:8001/base1/visuModel/RetroWorkbenchEvent");
		builder.setBegin(120000);
		builder.setEnd(140000);
		builder.setSubject("Nestor");
		builder.addAttribute("http://localhost:8001/base1/visuModel/from", 2);
		builder.addAttribute("http://localhost:8001/base1/visuModel/to", 3);
		String builtObselUri = builder.create();
		assertNotNull(builtObselUri);
		IObsel builtObsel = resourceService.getResource(builtObselUri, IObsel.class);
		assertNotNull(builtObsel);
		set.add(builtObsel);
		
		traceOnServer = resourceService.getStoredTrace(trace.getUri());
		
		Set<IObsel> obselsOnServer = traceOnServer.getObsels();
		assertEquals(set, traceOnServer.getObsels());
		
		IObsel builtObselOnServeur = traceOnServer.get(builtObsel.getUri());
		assertEquals(2, builtObselOnServeur.getAttributePairs().size());
		assertEquals("http://localhost:8001/base1/visuModel/RetroWorkbenchEvent", builtObselOnServeur.getObselType().getUri());
	}

	public void testBufferedCollect() {
		String traceUri = storedTraceService.newStoredTrace(
				"http://localhost:8001/base1/", 
				"http://localhost:8001/base1/visuModel/", 
		"Damien");
		IStoredTrace trace = resourceService.getStoredTrace(traceUri);
		
		storedTraceService.startBufferedCollect(trace);
		Deque<IObsel> set = new LinkedList<IObsel>();
		
		String o1 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s1", 1);
		set.add(Ktbs.getPojoFactory().createObsel(o1));
		String  o2 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s2", 2);
		set.add(Ktbs.getPojoFactory().createObsel(o2));
		String  o3 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s3", 4);
		set.add(Ktbs.getPojoFactory().createObsel(o3));
		String  o4 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s4", 4);
		set.add(Ktbs.getPojoFactory().createObsel(o4));
		String  o5 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s5", 6);
		set.add(Ktbs.getPojoFactory().createObsel(o5));
		String  o6 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s6", 9);
		set.add(Ktbs.getPojoFactory().createObsel(o6));
		String  o7 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s7", 11);
		set.add(Ktbs.getPojoFactory().createObsel(o7));
		
		ITrace traceOnServer = resourceService.getStoredTrace(trace.getUri());
		assertEquals(Collections.EMPTY_SET, resourceService.getStoredTrace(trace.getUri()).getObsels());
		
		long start = System.currentTimeMillis();
		storedTraceService.postBufferedObsels(trace);
		long end = System.currentTimeMillis();
		
		traceOnServer = resourceService.getStoredTrace(trace.getUri());
		assertEquals(set.size(), resourceService.getStoredTrace(trace.getUri()).getObsels().size());
		
		System.out.println("Duration in ms: " + (end-start) + " \tnb obsels: " + traceOnServer.getObsels().size());
		
	}
	
	public void testlistObsel() {
		String traceUri = storedTraceService.newStoredTrace(
				"http://localhost:8001/base1/", 
				"http://localhost:8001/base1/visuModel/", 
		"Damien");
		
		IStoredTrace trace= resourceService.getStoredTrace(traceUri);
		Set<IObsel> set = new HashSet<IObsel>();
		
		String o1 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s1", 1);
		PojoFactory f = Ktbs.getPojoFactory();
		set.add(f.createObsel(o1));
		String o2 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s2", 2);
		set.add(f.createObsel(o2));
		String o3 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s3", 4);
		set.add(f.createObsel(o3));
		String o4 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s4", 4);
		set.add(f.createObsel(o4));
		String o5 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s5", 6);
		set.add(f.createObsel(o5));
		String o6 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s6", 9);
		set.add(f.createObsel(o6));
		String o7 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s7", 11);
		set.add(f.createObsel(o7));
		
		ITrace traceOnServer = resourceService.getStoredTrace(trace.getUri());
		assertEquals(set, traceOnServer.getObsels());
		
		Collection<IObsel> set1 = storedTraceService.listObsels(trace, 1, 11);
		assertEquals(set, set1);
		
		Collection<IObsel> set2 = storedTraceService.listObsels(trace, 2, 6);
		Set<IObsel> set2expected = new HashSet<IObsel>();
		set2expected.add(f.createObsel(o2));
		set2expected.add(f.createObsel(o3));
		set2expected.add(f.createObsel(o4));
		set2expected.add(f.createObsel(o5));
		assertEquals(set2expected, set2);
		
		Collection<IObsel> set3 = storedTraceService.listObsels(trace, 5, 10);
		Set<IObsel> set3expected = new HashSet<IObsel>();
		set3expected.add(f.createObsel(o6));
		set3expected.add(f.createObsel(o5));
		assertEquals(set3expected, set3);
	}

	public void testNewStoredTrace() {
		IBase base1 = resourceService.getResource("http://localhost:8001/base1/", IBase.class);
		
		int size1 = base1.getStoredTraces().size();
		
		int nb = 10;
		for(int i=1; i<=nb; i++) {
			storedTraceService.newStoredTrace(
					"http://localhost:8001/base1/", 
					"http://localhost:8001/base1/visuModel/", 
			"Damien");
			
		}
			 
		int size2 =resourceService.getResource("http://localhost:8001/base1/", IBase.class).getStoredTraces().size();

		assertEquals(size1+nb, size2);
	}
}
