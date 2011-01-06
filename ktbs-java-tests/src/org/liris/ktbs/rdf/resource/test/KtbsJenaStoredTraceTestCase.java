package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;

public class KtbsJenaStoredTraceTestCase  extends TestCase {


	private StoredTrace traceObsels;
	private StoredTrace traceInfo;
	
	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/t01.ttl");
		traceObsels = KtbsJenaResourceFactory.getInstance().createStoredTrace(
				"http://localhost:8001/base1/t01/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();

		fis = new FileInputStream("turtle/t01-info.ttl");
		traceInfo = KtbsJenaResourceFactory.getInstance().createStoredTrace(
				"http://localhost:8001/base1/t01/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
	}

	@Test
	public void testAddObsel() {
		try {
			traceObsels.addObsel(null);
			fail("Should have failed");
		} catch(ReadOnlyObjectException e) {
			
		} catch(Exception e) {
			fail("Unexcepted exception");
		}
	}

	@Test
	public void testGetDefaultSubject() {
		assertEquals("Damien Cram", traceInfo.getDefaultSubject());
		assertEquals(null, traceObsels.getDefaultSubject());
	}

}
