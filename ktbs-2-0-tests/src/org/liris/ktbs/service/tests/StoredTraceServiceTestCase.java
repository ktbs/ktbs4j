package org.liris.ktbs.service.tests;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import junit.framework.TestCase;

import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsRootClient;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.MultiUserRootProvider;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;

public class StoredTraceServiceTestCase extends TestCase {

	private StoredTraceService storedTraceService;
	private ResourceService resourceService;

	@Override
	protected void setUp() throws Exception {
		MultiUserRootProvider service = Ktbs.getMultiUserRestRootProvider();
		KtbsRootClient clientDamien;
		if(service.openClient("Damien", "dtotio")) {
			clientDamien = service.getClient("Damien");
		} else 
			clientDamien = service.getClient("Damien");
		storedTraceService = clientDamien.getStoredTraceService();
		resourceService = clientDamien.getResourceService();
		
		// init the trace model
		ITraceModel model = resourceService.getTraceModel("http://localhost:8001/base1/visuModel/");
		if(model == null)
			RetroRoomTraceModel.create("http://localhost:8001/base1/", "visuModel");
	}


	public void testNewObsel() {
		IStoredTrace trace = storedTraceService.newStoredTrace(
				"http://localhost:8001/base1/", 
				"http://localhost:8001/base1/visuModel/", 
		"Damien");
		
		Set<IObsel> set = new HashSet<IObsel>();
		
		set.add(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroWorkbenchEvent", 1000));
		set.add(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroViewResizeEvent", 2000));
		set.add(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroDoubleClickTraceLineStartCreateCommentEvent", 4000));
		set.add(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroWorkbenchEvent", 4500));
		set.add(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroViewResizeEvent", 98000));
		set.add(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroDoubleClickTraceLineStartCreateCommentEvent", 101000));
		set.add(storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/RetroWorkbenchEvent", 110000));
		
		ITrace traceOnServer = resourceService.getStoredTrace(trace.getUri());
		
		assertEquals(set, traceOnServer.getObsels());
		
	}

	public void testBufferedCollect() {
		IStoredTrace trace = storedTraceService.newStoredTrace(
				"http://localhost:8001/base1/", 
				"http://localhost:8001/base1/visuModel/", 
		"Damien");
		
		storedTraceService.startBufferedCollect(trace);
		Deque<IObsel> set = new LinkedList<IObsel>();
		
		IObsel o1 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s1", 1);
		set.add(o1);
		IObsel o2 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s2", 2);
		set.add(o2);
		IObsel o3 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s3", 4);
		set.add(o3);
		IObsel o4 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s4", 4);
		set.add(o4);
		IObsel o5 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s5", 6);
		set.add(o5);
		IObsel o6 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s6", 9);
		set.add(o6);
		IObsel o7 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s7", 11);
		set.add(o7);
		
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
		IStoredTrace trace = storedTraceService.newStoredTrace(
				"http://localhost:8001/base1/", 
				"http://localhost:8001/base1/visuModel/", 
		"Damien");
		
		Set<IObsel> set = new HashSet<IObsel>();
		
		IObsel o1 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s1", 1);
		set.add(o1);
		IObsel o2 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s2", 2);
		set.add(o2);
		IObsel o3 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s3", 4);
		set.add(o3);
		IObsel o4 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s4", 4);
		set.add(o4);
		IObsel o5 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s5", 6);
		set.add(o5);
		IObsel o6 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s6", 9);
		set.add(o6);
		IObsel o7 = storedTraceService.newObsel(trace, "http://localhost:8001/base1/visuModel/s7", 11);
		set.add(o7);
		
		ITrace traceOnServer = resourceService.getStoredTrace(trace.getUri());
		assertEquals(set, traceOnServer.getObsels());
		
		Collection<IObsel> set1 = storedTraceService.listObsels(trace, 1, 11);
		assertEquals(set, set1);
		
		Collection<IObsel> set2 = storedTraceService.listObsels(trace, 2, 6);
		Set<IObsel> set2expected = new HashSet<IObsel>();
		set2expected.add(o2);
		set2expected.add(o3);
		set2expected.add(o4);
		set2expected.add(o5);
		assertEquals(set2expected, set2);
		
		Collection<IObsel> set3 = storedTraceService.listObsels(trace, 5, 10);
		Set<IObsel> set3expected = new HashSet<IObsel>();
		set3expected.add(o6);
		set3expected.add(o5);
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
