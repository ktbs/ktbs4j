package org.liris.ktbs.rdf.resource.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class KtbsJenaResourceAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				KtbsJenaResourceAllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(KtbsJenaResourceTestCase.class);
		suite.addTestSuite(KtbsJenaRootTestCase.class);
		suite.addTestSuite(KtbsJenaBaseTestCase.class);
		suite.addTestSuite(KtbsJenaTraceTestCase.class);
		suite.addTestSuite(KtbsJenaStoredTraceTestCase.class);
		suite.addTestSuite(KtbsJenaComputedTraceTestCase.class);
		suite.addTestSuite(KtbsJenaObselTypeTestCase.class);
		suite.addTestSuite(KtbsJenaAttributeTypeTestCase.class);
		suite.addTestSuite(KtbsJenaRelationTypeTestCase.class);
		suite.addTestSuite(KtbsJenaTraceModelTestCase.class);
		suite.addTestSuite(KtbsJenaMethodTestCase.class);
		suite.addTestSuite(KtbsJenaObselTestCase.class);
		//$JUnit-END$
		suite.addTestSuite(KtbsJenaObselTestCase.class);
		return suite;
	}

}
