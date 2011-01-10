package org.liris.ktbs.rdf.resource.test;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.TraceModel;

public class KtbsJenaAttributeTypeTestCase extends AbstractKtbsJenaTestCase {

	private TraceModel traceModel;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		traceModel = loadInHolder(
				"base1/model1/", 
				"model1.ttl", 
				TraceModel.class);

	}

	@Test
	public void testGetDomain() {
		fail("Not yet implemented");
	}

}
