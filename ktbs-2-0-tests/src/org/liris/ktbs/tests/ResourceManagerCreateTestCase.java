package org.liris.ktbs.tests;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.Ktbs;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IMethod;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.ResourceService;

public class ResourceManagerCreateTestCase extends TestCase {
	private ResourceService manager;
	private IRoot root;

	@Before
	public void setUp() throws Exception {
		manager = Ktbs.getRestClient().getResourceService();
		root = manager.getKtbsResource("http://localhost:8001/", IRoot.class);
	}

	@Test
	public void testCreateBase() {
		IBase base2 = manager.newBase(root.getUri(), "base2", "Owner de la base 2");
		IBase base2remote = manager.getKtbsResource("http://localhost:8001/base2/", IBase.class);
		assertNotNull(base2remote);
		assertEquals(base2, base2remote);
		assertEquals(base2.getLabel(), base2remote.getLabel());
	}

	@Test
	public void testCreateTraceModel() {
		ITraceModel model2 = manager.newTraceModel("http://localhost:8001/base1/", "model2");
		
		ITraceModel model2remote = manager.getKtbsResource("http://localhost:8001/base1/model2/", ITraceModel.class);
		assertNotNull(model2remote);
		assertEquals(model2, model2remote);
		assertEquals(model2.getLabel(), model2remote.getLabel());
	}

	@Test
	public void testCreateStoredTrace() {
		IStoredTrace st2 = manager.newStoredTrace(
				"http://localhost:8001/base1/", 
				"t02", 
				"http://localhost:8001/base1/model2/", 
				"Origine de la trace 2",
				"Nestor"
				);
		
		IStoredTrace t02remote = manager.getKtbsResource("http://localhost:8001/base1/t02/", IStoredTrace.class);
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
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("script", "le code");
		parameters.put("param2", "value du param2");
		
		IMethod method = manager.newMethod(
				"http://localhost:8001/base1/", 
				"mymethod", 
				KtbsConstants.SCRIPT_PYTHON,
				"erifuzperufpzeoiuffuopz",
				parameters
		);
		
		IMethod method2 = manager.getKtbsResource("http://localhost:8001/base1/mymethod/", IMethod.class);
		assertNotNull(method2);
		assertEquals(method, method2);
		assertEquals(method.getTypeUri(), method2.getTypeUri());
		assertEquals(method.getEtag(), method2.getEtag());
		assertEquals(method.getInherits(), method2.getInherits());
		
	}

	@Test
	public void testObsel() {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(Examples.getMessage().getUri(), "Bonjour tout le monde");
		
		IObsel o = manager.newObsel(
				"http://localhost:8001/base1/t01/", 
				null, 
				Examples.getCloseChat().getUri(),
				null,
				null,
				new BigInteger("20000"),
				new BigInteger("20000"),
				"Toto",
				attributes
		);
		
		assertNotNull(o);
		ResourceManagerGetTestCase.displayObsel(o);
		
	}
	
	@Test
	public void testCreateComputedTrace() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("start", "startValue");
		
		Set<String> sources = new HashSet<String>();
		sources.add("http://localhost:8001/base1/t01/");
		sources.add("http://localhost:8001/base1/t02/");
		
		IComputedTrace trace = manager.newComputedTrace(
				"http://localhost:8001/base1/", 
				"ct1", 
				"http://localhost:8001/base1/count/",
				sources,
				null
		);
		
		IComputedTrace ct2 = manager.getKtbsResource("http://localhost:8001/base1/ct1/", IComputedTrace.class);
		assertNotNull(ct2);
		assertEquals(trace, ct2);
//		assertEquals(trace.getTraceModel(), ct2.getTraceModel());
//		assertEquals(trace.getOrigin(), ct2.getOrigin());
		assertEquals(trace.getSourceTraces(), ct2.getSourceTraces());
		assertEquals(trace.getMethodParameters(), ct2.getMethodParameters());
		assertEquals(trace.getMethod(), ct2.getMethod());
		
		
	}
}
