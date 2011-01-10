package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.TraceModel;

public class KtbsJenaRelationTypeTestCase extends AbstractKtbsJenaTestCase {

	private TraceModel traceModel;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		FileInputStream fis = new FileInputStream("turtle/model1.ttl");
		traceModel = loadInHolder(
				"base1/model1/", 
				"model1.ttl", 
				TraceModel.class);
		fis.close();
	}
	
	
	@Test
	public void testGetDomain() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRange() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSuperRelationType() {
		fail("Not yet implemented");
	}

}
