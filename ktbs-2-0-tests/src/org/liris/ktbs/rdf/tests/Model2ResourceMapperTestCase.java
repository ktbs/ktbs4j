package org.liris.ktbs.rdf.tests;

import java.io.FileInputStream;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.pojo.AttributeTypePojo;
import org.liris.ktbs.core.pojo.BasePojo;
import org.liris.ktbs.core.pojo.ObselPojo;
import org.liris.ktbs.core.pojo.ObselTypePojo;
import org.liris.ktbs.core.pojo.RelationTypePojo;
import org.liris.ktbs.core.pojo.ResourcePojo;
import org.liris.ktbs.core.pojo.StoredTracePojo;
import org.liris.ktbs.core.pojo.TraceModelPojo;
import org.liris.ktbs.core.pojo.UriResource;
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
		ResourcePojo pojo = mapper.getResource("http://localhost:8001/base1/");
		assertTrue(BasePojo.class.isAssignableFrom(pojo.getClass()));
		BasePojo base = (BasePojo)pojo;
		assertEquals(1, base.getLabels().size());
		assertEquals("A trace base", base.getLabels().iterator().next());
		assertEquals(1, base.getStoredTraces().size());
		assertEquals(6, base.getComputedTraces().size());
		assertEquals(4, base.getMethods().size());
		assertEquals(1, base.getTraceModels().size());
	}
	
	public void testReadStoredTrace() throws Exception {
		model.read(new FileInputStream("turtle/t01-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		ResourcePojo pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		
		assertTrue(StoredTracePojo.class.isAssignableFrom(pojo.getClass()));
		StoredTracePojo trace = (StoredTracePojo)pojo;
		assertEquals(0, trace.getObsels().size());
		assertEquals("2010-04-28T18:09:00Z", trace.getOrigin());
		assertEquals("Damien Cram", trace.getDefaultSubject());
		assertEquals("yes", trace.getCompliesWithModel());
		
		assertEquals(new UriResource("http://localhost:8001/base1/model1/"), trace.getTraceModel());
		assertEquals(TraceModelPojo.class, trace.getTraceModel().getClass());
		
		assertEquals(3, trace.getTransformedTraces().size());

		model.read(new FileInputStream("turtle/t01-obsels-and-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		assertTrue(StoredTracePojo.class.isAssignableFrom(pojo.getClass()));
		trace = (StoredTracePojo)pojo;
		assertEquals(4, trace.getObsels().size());
	}
	
	public void testReadTraceModel() throws Exception {
		model.read(new FileInputStream("turtle/gra_model1.ttl"), "", KtbsConstants.JENA_TURTLE);
		ResourcePojo pojo = mapper.readResource("http://localhost:8001/base1/model1/", TraceModel.class);
		assertTrue(TraceModelPojo.class.isAssignableFrom(pojo.getClass()));
		TraceModelPojo traceModel = (TraceModelPojo) pojo;

		assertEquals(3, traceModel.getAttributeTypes().size());
		assertEquals(2, traceModel.getRelationTypes().size());
		assertEquals(6, traceModel.getObselTypes().size());

		pojo = mapper.getResource("http://localhost:8001/base1/model1/SendMsg");
		assertTrue(ObselTypePojo.class.isAssignableFrom(pojo.getClass()));
		ObselTypePojo obsType = (ObselTypePojo)pojo;
		assertEquals(1, obsType.getSuperObselTypes().size());
		assertEquals(new UriResource("http://localhost:8001/base1/model1/AbstractMsg"), obsType.getSuperObselTypes().iterator().next());
		assertEquals(ObselTypePojo.class, obsType.getSuperObselTypes().iterator().next().getClass());

		pojo = mapper.getResource("http://localhost:8001/base1/model1/from");
		assertTrue(AttributeTypePojo.class.isAssignableFrom(pojo.getClass()));
		AttributeTypePojo attType = (AttributeTypePojo)pojo;
		assertEquals(1, attType.getDomains().size());
		assertEquals(new UriResource("http://localhost:8001/base1/model1/RecvMsg"), attType.getDomains().iterator().next());
		assertEquals(0, attType.getRanges().size());

		pojo = mapper.getResource("http://localhost:8001/base1/model1/closes");
		assertTrue(RelationTypePojo.class.isAssignableFrom(pojo.getClass()));
		RelationTypePojo relType = (RelationTypePojo)pojo;
		assertEquals(1, relType.getDomains().size());
		assertEquals(new UriResource("http://localhost:8001/base1/model1/CloseChat"), relType.getDomains().iterator().next());
		assertEquals(0, relType.getRanges().size());
		assertEquals(1, relType.getSuperRelationTypes().size());
		assertEquals(new UriResource("http://localhost:8001/base1/model1/onChannel"), relType.getSuperRelationTypes().iterator().next());
//		assertEquals(1, attType.get)
		
	}
	
	public void testReadObsel() throws Exception {
		model.read(new FileInputStream("turtle/t01-obsels-and-info.ttl"), "", KtbsConstants.JENA_TURTLE);
		ResourcePojo pojo = mapper.getResource("http://localhost:8001/base1/t01/");
		assertTrue(StoredTracePojo.class.isAssignableFrom(pojo.getClass()));
		StoredTracePojo trace = (StoredTracePojo)pojo;
		assertEquals(4, trace.getObsels().size());
		
		// obs1
		ResourcePojo pojo1 = mapper.getResource("http://localhost:8001/base1/t01/obs1");
		assertTrue(ObselPojo.class.isAssignableFrom(pojo1.getClass()));
		ObselPojo o1 = (ObselPojo)pojo1;
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
		
		
		// obs1
		ResourcePojo pojo2 = mapper.getResource("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f");
		assertTrue(ObselPojo.class.isAssignableFrom(pojo2.getClass()));
		ObselPojo o2 = (ObselPojo)pojo2;
		assertEquals(1, o2.getOutgoingRelations().size());
		assertEquals(0, o2.getIncomingRelations().size());
		assertEquals(1, o2.getAttributePairs().size());
		
		// obs1
		ResourcePojo pojo3 = mapper.getResource("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139");
		assertTrue(ObselPojo.class.isAssignableFrom(pojo3.getClass()));
		ObselPojo o3 = (ObselPojo)pojo3;
		assertEquals(1, o3.getOutgoingRelations().size());
		assertEquals(0, o3.getIncomingRelations().size());
		assertEquals(2, o3.getAttributePairs().size());
		
		// obs1
		ResourcePojo pojo4 = mapper.getResource("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575");
		assertTrue(ObselPojo.class.isAssignableFrom(pojo4.getClass()));
		ObselPojo o4 = (ObselPojo)pojo4;
		assertEquals(1, o4.getOutgoingRelations().size());
		assertEquals(0, o4.getIncomingRelations().size());
		assertEquals(0, o4.getAttributePairs().size());
		
		
	}
}
