package org.liris.ktbs.rdf.tests;

import java.io.FileInputStream;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.domain.AttributeType;
import org.liris.ktbs.core.domain.Base;
import org.liris.ktbs.core.domain.KtbsResource;
import org.liris.ktbs.core.domain.Obsel;
import org.liris.ktbs.core.domain.ObselType;
import org.liris.ktbs.core.domain.RelationType;
import org.liris.ktbs.core.domain.StoredTrace;
import org.liris.ktbs.core.domain.TraceModel;
import org.liris.ktbs.core.domain.UriResourceImpl;
import org.liris.ktbs.rdf.Rdf2Pojo;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class Model2ResourceMapperTestCase extends TestCase {
	Model model;
	Rdf2Pojo mapper;
	
	@Override
	protected void setUp() throws Exception {
		model = ModelFactory.createDefaultModel();
		mapper = new Rdf2Pojo(model);
		
		
	}
	
	public void testReadBase() throws Exception {
		model.read(new FileInputStream("turtle/base1.ttl"), "", KtbsConstants.JENA_TURTLE);
		KtbsResource pojo = mapper.getResource("http://localhost:8001/base1/");
		assertTrue(Base.class.isAssignableFrom(pojo.getClass()));
		Base base = (Base)pojo;
		assertEquals(1, base.getLabels().size());
		assertEquals("A trace base", base.getLabels().iterator().next());
		assertEquals(1, base.getStoredTraces().size());
		assertEquals(6, base.getComputedTraces().size());
		assertEquals(4, base.getMethods().size());
		assertEquals(1, base.getTraceModels().size());
	}
	
	public void testReadStoredTrace() throws Exception {
		model.read(new FileInputStream("turtle/t01-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		KtbsResource pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		
		assertTrue(StoredTrace.class.isAssignableFrom(pojo.getClass()));
		StoredTrace trace = (StoredTrace)pojo;
		assertEquals(0, trace.getObsels().size());
		assertEquals("2010-04-28T18:09:00Z", trace.getOrigin());
		assertEquals("Damien Cram", trace.getDefaultSubject());
		assertEquals("yes", trace.getCompliesWithModel());
		
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/model1/"), trace.getTraceModel());
		assertEquals(TraceModel.class, trace.getTraceModel().getClass());
		
		assertEquals(3, trace.getTransformedTraces().size());

		model.read(new FileInputStream("turtle/t01-obsels-and-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		assertTrue(StoredTrace.class.isAssignableFrom(pojo.getClass()));
		trace = (StoredTrace)pojo;
		assertEquals(4, trace.getObsels().size());
	}
	
	public void testReadTraceModel() throws Exception {
		model.read(new FileInputStream("turtle/gra_model1.ttl"), "", KtbsConstants.JENA_TURTLE);
		KtbsResource pojo = mapper.readResource("http://localhost:8001/base1/model1/", TraceModel.class);
		assertTrue(TraceModel.class.isAssignableFrom(pojo.getClass()));
		TraceModel traceModel = (TraceModel) pojo;

		assertEquals(3, traceModel.getAttributeTypes().size());
		assertEquals(2, traceModel.getRelationTypes().size());
		assertEquals(6, traceModel.getObselTypes().size());

		pojo = mapper.getResource("http://localhost:8001/base1/model1/SendMsg");
		assertTrue(ObselType.class.isAssignableFrom(pojo.getClass()));
		ObselType obsType = (ObselType)pojo;
		assertEquals(1, obsType.getSuperObselTypes().size());
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/model1/AbstractMsg"), obsType.getSuperObselTypes().iterator().next());
		assertEquals(ObselType.class, obsType.getSuperObselTypes().iterator().next().getClass());

		pojo = mapper.getResource("http://localhost:8001/base1/model1/from");
		assertTrue(AttributeType.class.isAssignableFrom(pojo.getClass()));
		AttributeType attType = (AttributeType)pojo;
		assertEquals(1, attType.getDomains().size());
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/model1/RecvMsg"), attType.getDomains().iterator().next());
		assertEquals(0, attType.getRanges().size());

		pojo = mapper.getResource("http://localhost:8001/base1/model1/closes");
		assertTrue(RelationType.class.isAssignableFrom(pojo.getClass()));
		RelationType relType = (RelationType)pojo;
		assertEquals(1, relType.getDomains().size());
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/model1/CloseChat"), relType.getDomains().iterator().next());
		assertEquals(0, relType.getRanges().size());
		assertEquals(1, relType.getSuperRelationTypes().size());
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/model1/onChannel"), relType.getSuperRelationTypes().iterator().next());
//		assertEquals(1, attType.get)
		
	}
	
	public void testReadObsel() throws Exception {
		model.read(new FileInputStream("turtle/t01-obsels-and-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		KtbsResource pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		assertTrue(StoredTrace.class.isAssignableFrom(pojo.getClass()));
		StoredTrace trace = (StoredTrace)pojo;
		assertEquals(4, trace.getObsels().size());
		
		// obs1
		KtbsResource pojo1 = mapper.getResource("http://localhost:8001/base1/t01/obs1");
		assertTrue(Obsel.class.isAssignableFrom(pojo1.getClass()));
		Obsel o1 = (Obsel)pojo1;
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
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/t01/"), o1.getTrace());
		
		
		// obs1
		KtbsResource pojo2 = mapper.getResource("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f");
		assertTrue(Obsel.class.isAssignableFrom(pojo2.getClass()));
		Obsel o2 = (Obsel)pojo2;
		assertEquals(1, o2.getOutgoingRelations().size());
		assertEquals(0, o2.getIncomingRelations().size());
		assertEquals(1, o2.getAttributePairs().size());
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/t01/"), o2.getTrace());
		
		// obs1
		KtbsResource pojo3 = mapper.getResource("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139");
		assertTrue(Obsel.class.isAssignableFrom(pojo3.getClass()));
		Obsel o3 = (Obsel)pojo3;
		assertEquals(1, o3.getOutgoingRelations().size());
		assertEquals(0, o3.getIncomingRelations().size());
		assertEquals(2, o3.getAttributePairs().size());
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/t01/"), o3.getTrace());
		
		// obs1
		KtbsResource pojo4 = mapper.getResource("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575");
		assertTrue(Obsel.class.isAssignableFrom(pojo4.getClass()));
		Obsel o4 = (Obsel)pojo4;
		assertEquals(1, o4.getOutgoingRelations().size());
		assertEquals(0, o4.getIncomingRelations().size());
		assertEquals(0, o4.getAttributePairs().size());
		assertEquals(new UriResourceImpl("http://localhost:8001/base1/t01/"), o4.getTrace());
		
		
	}
}
