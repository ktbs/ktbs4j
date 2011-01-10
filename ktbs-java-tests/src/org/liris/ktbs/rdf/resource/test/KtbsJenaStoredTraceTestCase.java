package org.liris.ktbs.rdf.resource.test;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.StoredTrace;

public class KtbsJenaStoredTraceTestCase  extends AbstractKtbsJenaTestCase {


	private StoredTrace traceObsels;
	private StoredTrace traceInfo;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		traceObsels = loadInHolder(
				"base1/t01/", 
				"t01.ttl", 
				StoredTrace.class);

		traceInfo = loadInHolder(
				"base1/t01/", 
				"t01-info.ttl", 
				StoredTrace.class);

	}

	@Test
	public void testAddObsel() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDefaultSubject() {
		assertEquals("Damien Cram", traceInfo.getDefaultSubject());
		assertEquals(null, traceObsels.getDefaultSubject());
	}

}
