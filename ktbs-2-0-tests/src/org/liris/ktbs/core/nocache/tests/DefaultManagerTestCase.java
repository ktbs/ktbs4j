package org.liris.ktbs.core.nocache.tests;

import junit.framework.TestCase;

import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.impl.DefaultManager;

public class DefaultManagerTestCase extends TestCase {

	ResourceService manager;
	
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
		fail("Not yet implemented");
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
