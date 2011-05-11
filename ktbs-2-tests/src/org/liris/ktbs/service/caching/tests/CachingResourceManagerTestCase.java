package org.liris.ktbs.service.caching.tests;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IResourceContainer;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.test.utils.Chrono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Precondition : the KTBS is filled with example resources
 * @param args
 */
public class CachingResourceManagerTestCase extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(CachingResourceManagerTestCase.class);
	
	private ResourceService cachingManager;
	private ResourceService defaultManager;

	@Before
	public void setUp() throws Exception {
		cachingManager = Ktbs.getRestCachingClient(1000, 10000).getResourceService();
		defaultManager = Ktbs.getRestClient().getResourceService();
	}

	public void testGet() {

		IRoot root = cachingManager.getRoot();
		assertNotNull(root);

		IBase base1 = root.get("base1");
		assertNotNull(base1);
		assertEquals(1, base1.getStoredTraces().size());

		base1 = cachingManager.getBase("base1");
		assertNotNull(base1);
		assertEquals(1, base1.getStoredTraces().size());

		IStoredTrace trace1 = (IStoredTrace) base1.get("t01");
		assertNotNull(trace1);
		assertEquals(4, trace1.getObsels().size());

		trace1 = cachingManager.getStoredTrace("/base1/t01");
		assertNotNull(trace1);
		assertEquals(4, trace1.getObsels().size());
	}
	
	public void testGoAndReturnWithTrace() {
		
		IStoredTrace cachingT01 = cachingManager.getStoredTrace("/base1/t01");
		long cachingRecursionTime = getRecursionTimeOnContainer(cachingT01, 50);
		
		IStoredTrace defaultT01 = defaultManager.getStoredTrace("/base1/t01");
		long defaultRecursionTime = getRecursionTimeOnContainer(defaultT01, 50);
		
		logger.info("Recursion time on t01 (with {} manager): {}", "default", defaultRecursionTime);
		logger.info("Recursion time on t01 (with {} manager): {}", "caching", cachingRecursionTime);
	}
	
	
	public void testGoAndReturnWithTraceModel() {
		ITraceModel cachingModel1 = cachingManager.getTraceModel("/base1/model1");
		long cachingRecursionTime = getRecursionTimeOnContainer(cachingModel1, 50);

		ITraceModel defaultModel1 = defaultManager.getTraceModel("/base1/model1");
		long defaultRecursionTime = getRecursionTimeOnContainer(defaultModel1, 50);
		
		logger.info("Recursion time on model1 (with {} manager): {}", "default", defaultRecursionTime);
		logger.info("Recursion time on model1 (with {} manager): {}", "caching", cachingRecursionTime);
	}

	private long getRecursionTimeOnContainer(IResourceContainer<? extends IKtbsResource> container, final int nbRecursions) {
		long cachingRecursionTime = 0;
		for(final IKtbsResource child:container) {
			System.out.println(child.getUri());
			long time = Chrono.measure(new Runnable() {
				public void run() {
					CachingResourceManagerTestCase.this.getParentRecursively(child, nbRecursions);
				}
			});
			cachingRecursionTime+=time;
		}
		return cachingRecursionTime;
	}
	
	private void getParentRecursively(IKtbsResource resource, int nbRecursions) {
		if(nbRecursions > 0)  {
			IResourceContainer<?> parent = (IResourceContainer<?>)resource.getParentResource();
			IKtbsResource child = parent.get(resource.getUri());
			getParentRecursively(child, nbRecursions-1);
		}
	}
}
