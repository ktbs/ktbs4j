package org.liris.ktbs.service.caching.tests;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.dao.DefaultCachingDao;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IResourceContainer;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.CachingResourceService;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.impl.CachingResourceManager;
import org.liris.ktbs.test.utils.Chrono;
import org.liris.ktbs.utils.KtbsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Precondition : the KTBS is filled with example resources
 * @param args
 */
public class CachingResourceManagerTestCase extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(CachingResourceManagerTestCase.class);
	private static final int NB_RECURSIONS = 100;
	
	private ResourceService cachingManager;
	private ResourceService defaultManager;

	@Before
	public void setUp() throws Exception {
		cachingManager = Ktbs.getRestCachingClient(1000, 10000l).getResourceService();
		defaultManager = Ktbs.getRestClient().getResourceService();
	}

	public void testExemple() {
		
		// get the caching client
		// max size: 1000 resources, timeout: 10 seconds
		KtbsClient restCachingClient = Ktbs.getRestCachingClient("http://localhost:8001/", 1000, 10000l);
		
		// get the caching resource
		ResourceService resourceservice = restCachingClient.getResourceService();
		
		
		// produce two underlying GET request and cache the resources
		IBase base1 = resourceservice.getRoot().get("base1");

		// retrieved from cache
		IBase base1Again = resourceservice.getRoot().get("base1");
		
		// give access to manual caching management
		CachingResourceService cachingService = (CachingResourceService)resourceservice;
		
		// produce an underlying GET request and update the cache with new version
		IBase base1FromServer = cachingService.getBase("/base1/", true);
		
		
		
	}

	public void testExemple2() {
		
		// get the caching client
		// max size: 1000 resources, timeout: 10 seconds
		KtbsClient restCachingClient = Ktbs.getRestCachingClient("http://localhost:8001/", 1000, 10000l);
		
		// get the caching resource
		ResourceService resourceService = restCachingClient.getResourceService();
		
		
		// produce two underlying GET requests and cache the resources
		IBase base1 = resourceService.getBase("base1");
		
		// base1 contains only one stored trace: t01
		assertEquals(1, base1.getStoredTraces().size());
		
		// generate an underlying POST request
		resourceService.newStoredTrace(
				base1.getUri(), 
				"t02", 
				"http://localhost:8001/base1/model1/", 
				KtbsUtils.now(), 
				null, 
				null, 
				null, 
				null, 
				"Damien Cram");
		
		// retrieve base1 via the resourceService again
		IBase base1Again = resourceService.getBase("base1");
		
		// there is still only t01 in base1Again
		assertEquals(1, base1Again.getStoredTraces().size());
		
		// because base1 and base1Again are now the same Java objects
		// and nothing has been changed locally
		assertTrue(base1 == base1Again);
		
		// base1 must be retrieved from the service explicitely
		CachingResourceService cachingService = (CachingResourceService)resourceService;
		IBase base1Again2 = cachingService.getBase("base1", true);
		
		// now it is OK
		assertEquals(2, base1Again2.getStoredTraces().size());
		assertFalse(base1 == base1Again2);
		
		// alternative: remove base1 from cache
		cachingService.removeFromCache(base1);
		assertEquals(2, resourceService.getBase("base1").getStoredTraces().size());

		// alternative 2: clear the cache
		cachingService.clearCache();
		assertEquals(2, resourceService.getBase("base1").getStoredTraces().size());
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
		long cachingRecursionTime = getRecursionTimeOnContainer(cachingT01, NB_RECURSIONS);
		
		IStoredTrace defaultT01 = defaultManager.getStoredTrace("/base1/t01");
		long defaultRecursionTime = getRecursionTimeOnContainer(defaultT01, NB_RECURSIONS);
		
		logger.info("Recursion time on t01 (with {} manager): {}", "default", defaultRecursionTime);
		logger.info("Recursion time on t01 (with {} manager): {}", "caching", cachingRecursionTime);
	}
	
	
	public void testGoAndReturnWithTraceModel() {
		ITraceModel cachingModel1 = cachingManager.getTraceModel("/base1/model1");
		long cachingRecursionTime = getRecursionTimeOnContainer(cachingModel1, NB_RECURSIONS);

		ITraceModel defaultModel1 = defaultManager.getTraceModel("/base1/model1");
		long defaultRecursionTime = getRecursionTimeOnContainer(defaultModel1, NB_RECURSIONS);
		
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
	
	public void testResourceNumber1() {
		CachingResourceManager cachingManager = (CachingResourceManager) Ktbs.getRestCachingClient("http://localhost:8001/", 20, 1000000l).getResourceService();
		DefaultCachingDao cachingDao = (DefaultCachingDao)cachingManager.getDao();
		
		assertEquals(0, cachingDao.size());

		IRoot root = cachingManager.getRoot();
		assertNotNull(root);
		assertEquals(1, cachingDao.size());
		
		IBase base1 = root.get("base1");
		assertNotNull(base1);
		assertEquals(1, cachingDao.size());
		base1.getLabel();
		assertEquals(2, cachingDao.size());
		
		base1 = root.get("base1");
		base1.getLabel();
		assertNotNull(base1);
		assertEquals(2, cachingDao.size());
		
		IComputedTrace filtered1 = (IComputedTrace)base1.get("filtered1");
		assertNotNull(filtered1);
		assertEquals(2, cachingDao.size());
		filtered1.getLabel();
		assertEquals(3, cachingDao.size());
		
		
		ITrace t01 = filtered1.getSourceTraces().iterator().next();
		assertNotNull(t01);
		// t01 is a proxy, nothing changed
		assertEquals(3, cachingDao.size());
		// retrieve the actual resource
		t01.getOrigin();
		assertEquals(4, cachingDao.size());
		
		t01.getObsels().iterator().next();
		assertEquals(8, cachingDao.size());
	
		assertTrue(cachingDao.getCache().contains(root));
		assertTrue(cachingDao.getCache().contains(base1));
		assertTrue(cachingDao.getCache().contains(filtered1));
		assertTrue(cachingDao.getCache().contains(t01));
	}

	
	public void testCacheTimout() {
		
		// five seconds
		long timeout = 5000l;
		CachingResourceManager cachingManager = (CachingResourceManager) Ktbs.getRestCachingClient("http://localhost:8001/", 1000, timeout).getResourceService();
		DefaultCachingDao cachingDao = (DefaultCachingDao)cachingManager.getDao();
		
		assertEquals(0, cachingDao.size());
		
		IRoot root = cachingManager.getRoot();
		assertNotNull(root);
		assertEquals(1, cachingDao.size());
		assertTrue(cachingDao.getCache().contains(root));
		
		sleep(1000);
		
		IBase base1 = root.get("base1");
		assertNotNull(base1);
		assertEquals(1, cachingDao.size());
		base1.getLabel();
		assertEquals(2, cachingDao.size());

		sleep(2000);
		
		base1 = root.get("base1");
		base1.getLabel();
		assertNotNull(base1);
		assertEquals(2, cachingDao.size());
		assertTrue(cachingDao.getCache().contains(root));
		assertTrue(cachingDao.getCache().contains(base1));
		
		IComputedTrace filtered1 = (IComputedTrace)base1.get("filtered1");
		assertNotNull(filtered1);
		assertEquals(2, cachingDao.size());
		filtered1.getLabel();
		assertEquals(3, cachingDao.size());
		assertTrue(cachingDao.getCache().contains(root));
		assertTrue(cachingDao.getCache().contains(base1));
		assertTrue(cachingDao.getCache().contains(filtered1));
		
		sleep(2500);
		
		ITrace t01 = filtered1.getSourceTraces().iterator().next();
		assertNotNull(t01);
		// t01 is a proxy, nothing changed
		assertEquals(3, cachingDao.size());
		// retrieve the actual resource
		t01.getOrigin();
		assertEquals(3, cachingDao.size());
		assertFalse(cachingDao.getCache().contains(root));
		assertTrue(cachingDao.getCache().contains(base1));
		assertTrue(cachingDao.getCache().contains(filtered1));
		assertTrue(cachingDao.getCache().contains(t01));
		
		sleep(1000);
		
		// retrieve all the 4 obsels of t01
		t01.getObsels().iterator().next();
		assertEquals(6, cachingDao.size());
		assertFalse(cachingDao.getCache().contains(root));
		assertFalse(cachingDao.getCache().contains(base1));
		assertTrue(cachingDao.getCache().contains(filtered1));
		assertTrue(cachingDao.getCache().contains(t01));
	}
	
	private void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void testResourceNumber2() {
		CachingResourceManager cachingManager = (CachingResourceManager) Ktbs.getRestCachingClient("http://localhost:8001/", 3, 1000000l).getResourceService();
		DefaultCachingDao cachingDao = (DefaultCachingDao)cachingManager.getDao();
		
		assertEquals(0, cachingDao.size());
		
		IRoot root = cachingManager.getRoot();
		assertNotNull(root);
		assertEquals(1, cachingDao.size());
		assertTrue(cachingDao.getCache().contains(root));
		
		IBase base1 = root.get("base1");
		assertNotNull(base1);
		assertEquals(1, cachingDao.size());
		base1.getLabel();
		assertEquals(2, cachingDao.size());
		
		base1 = root.get("base1");
		base1.getLabel();
		assertNotNull(base1);
		assertEquals(2, cachingDao.size());
		assertTrue(cachingDao.getCache().contains(root));
		assertTrue(cachingDao.getCache().contains(base1));
		
		IComputedTrace filtered1 = (IComputedTrace)base1.get("filtered1");
		assertNotNull(filtered1);
		assertEquals(2, cachingDao.size());
		filtered1.getLabel();
		assertEquals(3, cachingDao.size());
		assertTrue(cachingDao.getCache().contains(root));
		assertTrue(cachingDao.getCache().contains(base1));
		assertTrue(cachingDao.getCache().contains(filtered1));
		
		
		ITrace t01 = filtered1.getSourceTraces().iterator().next();
		assertNotNull(t01);
		// t01 is a proxy, nothing changed
		assertEquals(3, cachingDao.size());
		// retrieve the actual resource
		t01.getOrigin();
		assertEquals(3, cachingDao.size());
		assertFalse(cachingDao.getCache().contains(root));
		assertTrue(cachingDao.getCache().contains(base1));
		assertTrue(cachingDao.getCache().contains(filtered1));
		assertTrue(cachingDao.getCache().contains(t01));
		
		// retrieve all the 4 obsels of t01
		t01.getObsels().iterator().next();
		assertEquals(3, cachingDao.size());
		assertFalse(cachingDao.getCache().contains(root));
		assertFalse(cachingDao.getCache().contains(base1));
		assertFalse(cachingDao.getCache().contains(filtered1));
		assertFalse(cachingDao.getCache().contains(t01));
		
	}
}
