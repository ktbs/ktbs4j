package org.liris.ktbs.core.impl.tests;

import java.util.HashSet;

import junit.framework.TestCase;

import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.impl.ResourceFactory;
import org.liris.ktbs.tests.utils.ResourceProxy;

public class ResourceFactoryTestCase extends TestCase {

	ResourceFactory factory;
	protected void setUp() throws Exception {
		super.setUp();
		factory = new ResourceFactory();
	}

	public void testCreateStoredTrace() {
		fail("Not yet implemented");
	}

	public void testCreateComputedTrace() {
		ComputedTrace trace = factory.createComputedTrace(
				ResourceProxy.createProxy(Base.class, "http://localhost:8001/base1/"), 
				"L'origine", 
				"t1", 
				ResourceProxy.createProxy(TraceModel.class, "http://localhost:8001/base1/model1/"), 
				ResourceProxy.createProxy(Method.class, "http://localhost:8001/base1/method1/"), 
				new HashSet<Trace>());
		
		System.out.println(trace.getUri());
		trace.addLabel("teozifhcz");
		System.out.println(trace.getLabel());
		System.out.println(trace.getOrigin());
		System.out.println(trace.getLocalName());
		System.out.println(trace.getParentResource().getUri());
	}

	public void testCreateMethod() {
		fail("Not yet implemented");
	}

	public void testCreateTraceModel() {
		fail("Not yet implemented");
	}

	public void testCreateBase() {
		fail("Not yet implemented");
	}

	public void testCreateObsel() {
		fail("Not yet implemented");
	}

	public void testCreateObselType() {
		fail("Not yet implemented");
	}

	public void testCreateRelationType() {
		fail("Not yet implemented");
	}

	public void testCreateAttributeType() {
		fail("Not yet implemented");
	}

}
