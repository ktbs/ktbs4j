package org.liris.ktbs.core.nocache.tests;

import java.util.HashSet;

import junit.framework.TestCase;

import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.impl.ResourceManager;
import org.liris.ktbs.core.nocache.DefaultManager;
import org.liris.ktbs.tests.utils.ResourceProxy;

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
		ComputedTrace trace = manager.newComputedTrace(
				ResourceProxy.createProxy(Base.class, "http://localhost:8001/base1/"), 
				"t1", 
				ResourceProxy.createProxy(Method.class, "http://localhost:8001/base1/method1/"), 
				new HashSet<Trace>());
		
		System.out.println(trace.getUri());
		trace.addLabel("teozifhcz");
		System.out.println(trace.getLabel());
		System.out.println(trace.getOrigin());
		System.out.println(trace.getLocalName());
		System.out.println(trace.getParentResource().getUri());
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
