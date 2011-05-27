package org.liris.ktbs.service.tests;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.domain.AttributePair;
import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.rdf.tests.Examples;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.test.utils.KtbsServer;
import org.liris.ktbs.test.utils.ResourceDisplayer;
import org.liris.ktbs.utils.KtbsUtils;

public class ResourceManagerCreateTestCase extends TestCase {
	private ResourceService manager;

	private KtbsServer ktbsServer;
	@Before
	public void setUp() throws Exception {
		ktbsServer = KtbsServer.newInstance("http://localhost:8001/", System.err);
		ktbsServer.start();
		ktbsServer.populateKtbs();
		ktbsServer.populateT01();
		manager = Ktbs.getRestClient().getResourceService();
	}
	
	@Override
	protected void tearDown() throws Exception {
		ktbsServer.stop();
		super.tearDown();
	}

	@Test
	public void testCreateBase() {
		IBase base2 = manager.getBase(manager.newBase("base2"));
		IBase base2remote = manager.getResource("http://localhost:8001/base2/", IBase.class);
		assertNotNull(base2remote);
		assertEquals(base2, base2remote);
		assertEquals(base2.getLabel(), base2remote.getLabel());
	}

	@Test
	public void testCreateTraceModel() {
		manager.newBase("base3");
		String model2Uri = manager.newTraceModel("http://localhost:8001/base3/", "model2");
		
		ITraceModel model2remote = manager.getResource("http://localhost:8001/base3/model2/", ITraceModel.class);
		assertNotNull(model2remote);
		assertEquals(model2Uri, model2remote.getUri());
	}

	@Test
	public void testCreateStoredTrace() {
		manager.newBase("base4");
		manager.newTraceModel("http://localhost:8001/base4/", "model2");
		IStoredTrace st2 = manager.getStoredTrace(manager.newStoredTrace(
				"http://localhost:8001/base4/", 
				"t02", 
				"http://localhost:8001/base4/model2/", 
				"Origine de la trace 2",
				null,
				null,
				null,
				null,
				"Nestor"
				));
		
		IStoredTrace t02remote = manager.getResource("http://localhost:8001/base4/t02/", IStoredTrace.class);
		assertNotNull(t02remote);
		assertEquals(st2, t02remote);
		assertEquals(st2.getTraceModel(), t02remote.getTraceModel());
		assertEquals(st2.getTransformedTraces(), t02remote.getTransformedTraces());
		assertEquals(st2.getOrigin(), t02remote.getOrigin());
		assertEquals(st2.getTypeUri(), t02remote.getTypeUri());
		assertEquals(st2.getDefaultSubject(), t02remote.getDefaultSubject());
		
	}

	@Test
	public void testMethod() {
		manager.newBase("base5");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("script", "le code");
		parameters.put("param2", "value du param2");
		
		String createdMethodUri = manager.newMethod(
				"http://localhost:8001/base5/", 
				"mymethod", 
				KtbsConstants.SCRIPT_PYTHON,
				parameters
		);
		
		IMethod method = manager.getMethod(createdMethodUri);
		
		IMethod method2 = manager.getResource("http://localhost:8001/base5/mymethod/", IMethod.class);
		assertNotNull(method2);
		assertEquals(method, method2);
		assertEquals(method.getTypeUri(), method2.getTypeUri());
		assertEquals(method.getEtag(), method2.getEtag());
		assertEquals(method.getInherits(), method2.getInherits());
		
	}

	@Test
	public void testObsel() throws InterruptedException {
		manager.newBase("base6");
		manager.newTraceModel("http://localhost:8001/base6/", "model6");
		manager.newStoredTrace(
				"http://localhost:8001/base6/", 
				"t02", 
				"http://localhost:8001/base6/model6/", 
				KtbsUtils.now(),
				null,
				null,
				null,
				null,
				"Nestor"
				);
				
		Set<IAttributePair> attributes = new HashSet<IAttributePair>();
		attributes.add(new AttributePair(Examples.getMessage(), "Bonjour tout le monde"));
		
		String o = manager.newObsel(
				"http://localhost:8001/base6/t02/", 
				null, 
				Examples.getCloseChat().getUri(),
				null,
				null,
				new BigInteger("1000"),// 1s
				new BigInteger("1000"),// 1s
				"Toto",
				attributes
		);
		assertNotNull(o);
		ResourceDisplayer.displayObsel(manager.getResource(o, IObsel.class));
		
		// Ensure that the absolute obsel timestamp of o2 is after the relative timestamp of o (1000)
		Thread.sleep(1500);
		
		String o2 = manager.newObsel(
				"http://localhost:8001/base6/t02/", 
				null, 
				Examples.getCloseChat().getUri(),
				KtbsUtils.now(),
				KtbsUtils.now(),
				null,
				null,
				"Toto",
				attributes
		);
		
		assertNotNull(o2);
		IObsel resource = manager.getResource(o2, IObsel.class);

		// There is a KTBS Bug on this call : 500 Internal error
		//		assertNotNull(resource);
		//		KtbsDisplay.displayObsel(resource);
	}
	
	@Test
	public void testCreateComputedTrace() {
		assertNotNull(manager.newBase("base7"));
		
		assertNotNull(manager.newTraceModel("http://localhost:8001/base7/", "model7"));
		
		assertNotNull(manager.newStoredTrace(
				"http://localhost:8001/base7/", 
				"t01", 
				"http://localhost:8001/base7/model7/", 
				"Origine de la trace 2",
				null,
				null,
				null,
				null,
				"Nestor"
				));
		
		assertNotNull(manager.newStoredTrace(
				"http://localhost:8001/base7/", 
				"t02", 
				"http://localhost:8001/base7/model7/", 
				"Origine de la trace 2",
				null,
				null,
				null,
				null,
				"Nestor"
		));
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("script", "le code");
		parameters.put("param2", "value du param2");
		
		String createdMethodUri = manager.newMethod(
				"http://localhost:8001/base7/", 
				"mymethod", 
				KtbsConstants.SCRIPT_PYTHON,
				parameters
		);
		assertNotNull(createdMethodUri);
		
		Set<String> sources = new HashSet<String>();
		sources.add("http://localhost:8001/base7/t01/");
		sources.add("http://localhost:8001/base7/t02/");
		
		String newComputedTrace = manager.newComputedTrace(
				"http://localhost:8001/base7/", 
				"ct1", 
				"http://localhost:8001/base7/mymethod",
				sources,
				null
		);
		
		// Server Error here : 500 Internal Error
		/*
		assertNotNull(newComputedTrace);
		IComputedTrace ct1 = manager.getComputedTrace(newComputedTrace);
		
		IComputedTrace ct1FromServer = manager.getResource("http://localhost:8001/base7/ct1/", IComputedTrace.class);
		assertNotNull(ct1FromServer);
		assertEquals(ct1, ct1FromServer);
		assertEquals(ct1.getSourceTraces(), ct1FromServer.getSourceTraces());
		assertEquals(ct1.getMethodParameters(), ct1FromServer.getMethodParameters());
		assertEquals(ct1.getMethod(), ct1FromServer.getMethod());
		*/
		
	}
}
