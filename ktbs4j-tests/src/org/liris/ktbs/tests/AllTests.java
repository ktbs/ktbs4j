package org.liris.ktbs.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.liris.ktbs.service.caching.tests.CachingResourceManagerTestCase;
import org.liris.ktbs.service.tests.ResourceManagerCreateTestCase;
import org.liris.ktbs.service.tests.ResourceManagerDeleteTestCase;
import org.liris.ktbs.service.tests.ResourceManagerGetTestCase;
import org.liris.ktbs.service.tests.ResourceManagerSaveTestCase;
import org.liris.ktbs.service.tests.StoredTraceServiceTestCase;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(ResourceManagerCreateTestCase.class);
		suite.addTestSuite(StoredTraceServiceTestCase.class);
		suite.addTestSuite(ResourceManagerSaveTestCase.class);
		suite.addTestSuite(ResourceManagerGetTestCase.class);
		suite.addTestSuite(ResourceManagerDeleteTestCase.class);
		suite.addTestSuite(CachingResourceManagerTestCase.class);
		suite.addTestSuite(KtbsUtilsTestCase.class);
		//$JUnit-END$
		return suite;
	}

}
