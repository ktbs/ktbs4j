package org.liris.ktbs.rdf.resource.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestsKtbsRdfResource {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				AllTestsKtbsRdfResource.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(RdfResourceTestCase.class);
		suite.addTestSuite(RdfRootTestCase.class);
		suite.addTestSuite(RdfBaseTestCase.class);
		suite.addTestSuite(RdfTraceTestCase.class);
		suite.addTestSuite(RdfStoredTraceTestCase.class);
		suite.addTestSuite(RdfComputedTraceTestCase.class);
		suite.addTestSuite(RdfObselTypeTestCase.class);
		suite.addTestSuite(RdfAttributeTypeTestCase.class);
		suite.addTestSuite(RdfRelationTypeTestCase.class);
		suite.addTestSuite(RdfTraceModelTestCase.class);
		suite.addTestSuite(RdfMethodTestCase.class);
		suite.addTestSuite(RdfObselTestCase.class);
		//$JUnit-END$
		suite.addTestSuite(RdfObselTestCase.class);
		suite.addTestSuite(RdfResourceWithParameterTestCase.class);
		return suite;
	}

}
