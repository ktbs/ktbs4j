package org.liris.ktbs.rdf.tests;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ProxyFactory;
import org.liris.ktbs.core.domain.PojoFactory;
import org.liris.ktbs.core.domain.UriResource;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.rdf.Rdf2Java;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Rdf2JavaTestCase extends TestCase {
	private Model model;
	private Rdf2Java mapper;
	
	@Override
	protected void setUp() throws Exception {
		model = ModelFactory.createDefaultModel();
		mapper = new Rdf2Java(model);
		Field proxyFactoryField = Rdf2Java.class.getDeclaredField("proxyFactory");
		proxyFactoryField.setAccessible(true);
		proxyFactoryField.set(mapper, new ProxyFactory());
			
		Field pojoFactoryField = Rdf2Java.class.getDeclaredField("pojoFactory");
		pojoFactoryField.setAccessible(true);
		pojoFactoryField.set(mapper, new PojoFactory());
		
	}
	
	public void testReadBase() throws Exception {
		model.read(new FileInputStream("turtle/base1.ttl"), "", KtbsConstants.JENA_TURTLE);
		IKtbsResource pojo = mapper.getResource("http://localhost:8001/base1/");
		assertTrue(IBase.class.isAssignableFrom(pojo.getClass()));
		IBase base = (IBase)pojo;
		assertEquals(1, base.getLabels().size());
		assertEquals("A trace base", base.getLabels().iterator().next());
		assertEquals(1, base.getStoredTraces().size());
		assertEquals(6, base.getComputedTraces().size());
		assertEquals(4, base.getMethods().size());
		assertEquals(1, base.getTraceModels().size());
	}
	
	public void testReadStoredTrace() throws Exception {
		model.read(new FileInputStream("turtle/t01-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		IKtbsResource pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		
		assertTrue(IStoredTrace.class.isAssignableFrom(pojo.getClass()));
		IStoredTrace trace = (IStoredTrace)pojo;
		assertEquals(0, trace.getObsels().size());
		assertEquals("2010-04-28T18:09:00Z", trace.getOrigin());
		assertEquals("Damien Cram", trace.getDefaultSubject());
		assertEquals("yes", trace.getCompliesWithModel());
		
		assertEquals(new UriResource("http://localhost:8001/base1/model1/"), trace.getTraceModel());
		assertTrue(trace.getTraceModel() instanceof ITraceModel);
		
		assertEquals(3, trace.getTransformedTraces().size());

		model.read(new FileInputStream("turtle/t01-obsels-and-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		assertTrue(IStoredTrace.class.isAssignableFrom(pojo.getClass()));
		trace = (IStoredTrace)pojo;
		assertEquals(4, trace.getObsels().size());
	}
	
	public void testReadTraceModel() throws Exception {
		model.read(new FileInputStream("turtle/gra_model1.ttl"), "", KtbsConstants.JENA_TURTLE);
		ITraceModel traceModel = mapper.getResource("http://localhost:8001/base1/model1/", ITraceModel.class);

		assertEquals(3, traceModel.getAttributeTypes().size());
		assertEquals(2, traceModel.getRelationTypes().size());
		assertEquals(6, traceModel.getObselTypes().size());

		IKtbsResource pojo = mapper.getResource("http://localhost:8001/base1/model1/SendMsg");
		assertTrue(IObselType.class.isAssignableFrom(pojo.getClass()));
		IObselType obsType = (IObselType)pojo;
		assertEquals(1, obsType.getSuperObselTypes().size());
		assertEquals(new UriResource("http://localhost:8001/base1/model1/AbstractMsg"), obsType.getSuperObselTypes().iterator().next());
		assertTrue(obsType.getSuperObselTypes().iterator().next() instanceof IObselType);

		pojo = mapper.getResource("http://localhost:8001/base1/model1/from");
		assertTrue(IAttributeType.class.isAssignableFrom(pojo.getClass()));
		IAttributeType attType = (IAttributeType)pojo;
		assertEquals(1, attType.getDomains().size());
		assertEquals(new UriResource("http://localhost:8001/base1/model1/RecvMsg"), attType.getDomains().iterator().next());
		assertEquals(0, attType.getRanges().size());

		pojo = mapper.getResource("http://localhost:8001/base1/model1/closes");
		assertTrue(IRelationType.class.isAssignableFrom(pojo.getClass()));
		IRelationType relType = (IRelationType)pojo;
		assertEquals(1, relType.getDomains().size());
		assertEquals(new UriResource("http://localhost:8001/base1/model1/CloseChat"), relType.getDomains().iterator().next());
		assertEquals(0, relType.getRanges().size());
		assertEquals(1, relType.getSuperRelationTypes().size());
		assertEquals(new UriResource("http://localhost:8001/base1/model1/onChannel"), relType.getSuperRelationTypes().iterator().next());
		
		
	}
	
	public void testReadObsel() throws Exception {
		model.read(new FileInputStream("turtle/t01-obsels-and-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		IKtbsResource pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		assertTrue(IStoredTrace.class.isAssignableFrom(pojo.getClass()));
		IStoredTrace trace = (IStoredTrace)pojo;
		assertEquals(4, trace.getObsels().size());
		
		// obs1
		IKtbsResource pojo1 = mapper.getResource("http://localhost:8001/base1/t01/obs1");
		assertTrue(pojo1 instanceof IObsel);
		IObsel o1 = (IObsel)pojo1;
		assertEquals(1, o1.getAttributePairs().size());
		assertEquals(3, o1.getIncomingRelations().size());
		assertEquals(0, o1.getOutgoingRelations().size());
		assertEquals(new BigInteger("1000"), o1.getBegin());
		assertEquals("2010-04-28T18:09:01Z", o1.getBeginDT());
		assertEquals(new BigInteger("1000"), o1.getEnd());
		assertEquals("2010-04-28T18:09:01Z", o1.getEndDT());
		assertEquals("béa", o1.getSubject());
		assertEquals(0, o1.getSourceObsels().size());
		assertEquals(1, o1.getAttributePairs().size());
		assertEquals(new UriResource("http://localhost:8001/base1/t01/"), o1.getTrace());
		
		
		// obs1
		IKtbsResource pojo2 = mapper.getResource("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f");
		assertTrue(pojo2 instanceof IObsel);
		IObsel o2 = (IObsel)pojo2;
		assertEquals(1, o2.getOutgoingRelations().size());
		assertEquals(0, o2.getIncomingRelations().size());
		assertEquals(1, o2.getAttributePairs().size());
		assertEquals(new UriResource("http://localhost:8001/base1/t01/"), o2.getTrace());
		
		// obs1
		IKtbsResource pojo3 = mapper.getResource("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139");
		assertTrue(pojo3 instanceof IObsel);
		IObsel o3 = (IObsel)pojo3;
		assertEquals(1, o3.getOutgoingRelations().size());
		assertEquals(0, o3.getIncomingRelations().size());
		assertEquals(2, o3.getAttributePairs().size());
		assertEquals(new UriResource("http://localhost:8001/base1/t01/"), o3.getTrace());
		
		// obs1
		IKtbsResource pojo4 = mapper.getResource("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575");
		assertTrue(pojo4 instanceof IObsel);
		IObsel o4 = (IObsel)pojo4;
		assertEquals(1, o4.getOutgoingRelations().size());
		assertEquals(0, o4.getIncomingRelations().size());
		assertEquals(0, o4.getAttributePairs().size());
		assertEquals(new UriResource("http://localhost:8001/base1/t01/"), o4.getTrace());
		
		
		//
		IObselType type = o4.getObselType();
		try {
			type.getLabel();
			fail("NullPointerException expected");
		} catch(NullPointerException e) {
			/*
			 * Ok, the dao is null and the lazy loading fails
			 */
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception: " + e.toString());
		}
		
	}
}
