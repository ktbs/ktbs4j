package org.liris.ktbs.core.nocache.tests;

import java.util.HashSet;

import junit.framework.TestCase;

import org.liris.ktbs.core.DefaultManager;
import org.liris.ktbs.core.ResourceManager;
import org.liris.ktbs.core.domain.interfaces.IComputedTrace;

public class DefaultManagerTestCase extends TestCase {

	ResourceManager manager;
	
	protected void setUp() throws Exception {
		super.setUp();
		manager = new DefaultManager();
	}

	public void testGetKtbsResource() {
		fail("Not yet implemented");
	}

	public void testNewStoredTrace() {
		fail("Not yet implemented");
	}

	public void testNewComputedTrace() {
		IComputedTrace trace = manager.newComputedTrace(
				"http://localhost:8001/base1/", 
				"t1", 
				"http://localhost:8001/base1/method1/", 
				new HashSet<String>());
		
		System.out.println(trace.getUri());
		trace.addLabel("teozifhcz");
		System.out.println(trace.getLabel());
		System.out.println(trace.getOrigin());
		System.out.println(trace.getLocalName());
	}

	public void testNewMethod() {
		fail("Not yet implemented");
	}

	public void testNewTraceModel() {
		fail("Not yet implemented");
	}

	public void testNewBase() {
		fail("Not yet implemented");
	}

	public void testNewObsel() {
		fail("Not yet implemented");
	}

	public void testNewObselType() {
		fail("Not yet implemented");
	}

	public void testNewRelationType() {
		fail("Not yet implemented");
	}

	public void testNewAttributeType() {
		fail("Not yet implemented");
	}

}
