package org.liris.ktbs.rdf.resource.test;

import static org.junit.Assert.fail;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;

public class KtbsJenaAttributeTypeTestCase {

	private TraceModel traceModel;

	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/model1.ttl");
		traceModel = KtbsJenaResourceFactory.getInstance().createTraceModel(
				"http://localhost:8001/base1/model1/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
	}

	@Test
	public void testGetDomain() {
		fail("Not yet implemented");
	}

}
