package org.liris.ktbs.rdf.resource.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.DomainException;
import org.liris.ktbs.core.KtbsResourceNotFoundException;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.RangeException;
import org.liris.ktbs.core.RelationStatement;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsJenaObselTestCase  extends AbstractKtbsJenaTestCase {
	
	private Obsel obsel1;
	private Obsel obsel2;
	private Obsel obsel3;
	private Obsel obsel4;
	private Obsel obsel5;
	private Obsel obsel6;
	
	private StoredTrace trace;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		trace = loadInHolder(
				"base1/t01/", 
				"t01.ttl", 
				StoredTrace.class);

		obsel1 = trace.getObsel("http://localhost:8001/base1/t01/obs1");
		obsel2 = trace.getObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f");
		obsel3 = trace.getObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139");
		obsel4 = trace.getObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575");

		obsel5 = loadInHolder(
				"base1/t01/obs5", 
				"obsel5.ttl", 
				Obsel.class);
		obsel6 = loadInHolder(
				"base1/t01/obs6", 
				"obsel6.ttl", 
				Obsel.class);
	}

	@Test
	public void testGetTrace() {
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/t01/"), obsel1.getTrace());
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/t01/"), obsel2.getTrace());
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/t01/"), obsel3.getTrace());
		assertEquals(EmptyResourceFactory.getInstance().createResource("http://localhost:8001/base1/t01/"), obsel4.getTrace());
		assertEquals(null, obsel5.getTrace());
		assertEquals(null, obsel6.getTrace());
	}

	@Test
	public void testSetBegin() {
		assertEquals(1000,obsel1.getBegin());
		obsel1.setBegin(2000);
		assertEquals(2000,obsel1.getBegin());
		obsel1.setBegin(309730);
		assertEquals(309730,obsel1.getBegin());
	}
	@Test
	public void testSetEnd() {
		assertEquals(1000,obsel1.getEnd());
		obsel1.setEnd(2000);
		assertEquals(2000,obsel1.getEnd());
		obsel1.setEnd(309730);
		assertEquals(309730,obsel1.getEnd());
	}
	@Test
	public void testSetBeginDT() {
		assertEquals("2010-04-28T18:09:01+00:00",obsel1.getBeginDT());
		obsel1.setBeginDT("Toto");
		assertEquals("Toto",obsel1.getBeginDT());
		obsel1.setBeginDT("Tot");
		assertEquals("Tot",obsel1.getBeginDT());
	}
	
	@Test
	public void testSetEndDT() {
		assertEquals("2010-04-28T18:09:01+00:00",obsel1.getEndDT());
		obsel1.setEndDT("Toto");
		assertEquals("Toto",obsel1.getEndDT());
		obsel1.setEndDT("Tot");
		assertEquals("Tot",obsel1.getEndDT());
	}
	
	@Test
	public void testGetBegin() {
		assertEquals(1000,obsel1.getBegin());
		assertEquals(2000,obsel2.getBegin());
		assertEquals(5000,obsel3.getBegin());
		assertEquals(7000,obsel4.getBegin());
		assertEquals(12000,obsel5.getBegin());
		assertEquals(-1,obsel6.getBegin());
	}

	@Test
	public void testGetEnd() {
		assertEquals(1000,obsel1.getEnd());
		assertEquals(4000,obsel2.getEnd());
		assertEquals(5000,obsel3.getEnd());
		assertEquals(7000,obsel4.getEnd());
		assertEquals(14000,obsel5.getEnd());
		assertEquals(-1,obsel6.getEnd());
	}

	@Test
	public void testGetBeginDT() {
		assertEquals("2010-04-28T18:09:01+00:00",obsel1.getBeginDT());
		assertEquals("2010-04-28T18:09:02+00:00",obsel2.getBeginDT());
		assertEquals("2010-04-28T18:09:05+00:00",obsel3.getBeginDT());
		assertEquals("2010-04-28T18:09:07+00:00",obsel4.getBeginDT());
		assertEquals(null,obsel5.getBeginDT());
		assertEquals("2010-04-28T18:09:12+00:00",obsel6.getBeginDT());
	}

	@Test
	public void testGetEndDT() {
		assertEquals("2010-04-28T18:09:01+00:00",obsel1.getEndDT());
		assertEquals("2010-04-28T18:09:04+00:00",obsel2.getEndDT());
		assertEquals("2010-04-28T18:09:05+00:00",obsel3.getEndDT());
		assertEquals("2010-04-28T18:09:07Z",obsel4.getEndDT());
		assertEquals(null,obsel5.getEndDT());
		assertEquals("2010-04-28T18:09:14+00:00",obsel6.getEndDT());
	}

	@Test
	public void testSetObselType() {
		assertEquals(emptyFac.createObselType(type("OpenChat")),obsel1.getObselType());
		obsel1.setObselType(emptyFac.createObselType(type("CloseChat")));
		assertEquals(emptyFac.createObselType(type("CloseChat")),obsel1.getObselType());
		obsel1.setObselType(emptyFac.createObselType(type("AbstractMsg")));
		assertEquals(emptyFac.createObselType(type("AbstractMsg")),obsel1.getObselType());
	}
	
	@Test
	public void testGetObselType() {
		assertEquals(emptyFac.createObselType(type("OpenChat")),obsel1.getObselType());
		assertEquals(emptyFac.createObselType(type("SendMsg")),obsel2.getObselType());
		assertEquals(emptyFac.createObselType(type("RecvMsg")),obsel3.getObselType());
		assertEquals(emptyFac.createObselType(type("CloseChat")),obsel4.getObselType());
		assertEquals(emptyFac.createObselType(type("SendMsg")),obsel5.getObselType());
//		assertEquals(emptyFac.createObselType("http://example.com/Type1"),obsel6.getObselType());
		try {
			obsel6.getObselType();
			fail("Should fail");
		} catch(KtbsResourceNotFoundException e) {
			
		} catch(Exception e) {
			fail("Unexpected Exception");
		}
	}

	
	private static String type(String localType) {
		return "http://localhost:8001/base1/model1/" + localType;
	}

	@Test
	public void testSetSubject() {
		assertEquals("béa", obsel1.getSubject());
		obsel1.setSubject("tyty");
		assertEquals("tyty", obsel1.getSubject());
	}
	
	@Test
	public void testGetSubject() {
		assertEquals("béa",obsel1.getSubject());
		assertEquals("béa",obsel2.getSubject());
		assertEquals("béa",obsel3.getSubject());
		assertEquals("béa",obsel4.getSubject());
		assertEquals("Tonton Nestor",obsel5.getSubject());
		assertEquals(null,obsel6.getSubject());
	}

	@Test
	public void testListAttributes() {
		trace.addObsel(obsel5);
		trace.addObsel(obsel6);
		
		// needs the model to be loaded in order to know if it is an attribute
		loadInHolder("base1/model1/", "model1.ttl", TraceModel.class);
		
		assertEquals(1,KtbsUtils.count(obsel1.listAttributes()));
		testAttributeStatement(obsel1.listAttributes().next(), newAttType("channel"), "#my-channel");
		
		assertEquals(1,KtbsUtils.count(obsel2.listAttributes()));
		assertContains(obsel2.listAttributes(), newAttType("message"), "hello world");
		
		assertEquals(2,KtbsUtils.count(obsel3.listAttributes()));
		assertContains(obsel3.listAttributes(), newAttType("from"), "world");
		assertContains(obsel3.listAttributes(), newAttType("message"), "hello yourself");
		
		assertEquals(0,KtbsUtils.count(obsel4.listAttributes()));
		assertEquals(1,KtbsUtils.count(obsel5.listAttributes()));
		assertContains(obsel5.listAttributes(), newAttType("message"), "Salut les vieux");
		
		try {
			obsel6.listAttributes().hasNext();
			fail("Should fail since the obsel type is not valid");
		} catch(KtbsResourceNotFoundException e) {
			
		} catch(Exception e) {
			fail("Unexpected exception");
		}
		
	}
	
	private void assertContains(Iterator<RelationStatement> relss, RelationType type, Obsel obsel, boolean outgoing)  {
		for(RelationStatement rel:KtbsUtils.toLinkedList(relss)) {
			if(type.equals(rel.getRelation()) && obsel.equals((outgoing?rel.getToObsel():rel.getFromObsel())));
				return;
		}
		fail("Not contained");
	}
	
	private void assertContains(Iterator<AttributeStatement> atts, AttributeType type, Object value)  {
		for(AttributeStatement att:KtbsUtils.toLinkedList(atts)) {
			if(type.equals(att.getAttributeType()) && value.equals(att.getValue()))
				return;
		}
		fail("Not contained");
	}
	
	private void testAttributeStatement(AttributeStatement att, AttributeType type, Object value)  {
		assertEquals(type, att.getAttributeType());
		assertEquals(value, att.getValue());
	}

	private AttributeType newAttType(String string) {
		return emptyFac.createAttributeType("http://localhost:8001/base1/model1/" + string);
	}
	
	private String attType(String string) {
		return "http://localhost:8001/base1/model1/" + string;
	}

	@Test
	public void testAddAttribute() {
		Obsel obsel7 = loadInHolder("base1/t01/obs7", "obsel7.ttl", Obsel.class);
		
		assertEquals(0, KtbsUtils.count(obsel7.listAttributes()));
		
		obsel7.addAttribute(newAttType("from"), "Nestor");
		assertEquals(1, KtbsUtils.count(obsel7.listAttributes()));
		assertEquals("Nestor", obsel7.getAttributeValue(newAttType("from")));

		obsel7.addAttribute(newAttType("message"), "Hel");
		assertEquals(2, KtbsUtils.count(obsel7.listAttributes()));
		assertEquals("Hel", obsel7.getAttributeValue(newAttType("message")));

		try {
			obsel1.addAttribute(newAttType("from"), "Nestor");
			fail("Should fail");
		} catch(DomainException e) {
			
		}
	}
	@Test
	public void testGetAttributeValue() {
		assertEquals("#my-channel", obsel1.getAttributeValue(emptyFac.createAttributeType(attType("channel"))));
		assertEquals(null, obsel1.getAttributeValue(emptyFac.createAttributeType(attType("channl"))));
		assertEquals("hello world", obsel2.getAttributeValue(emptyFac.createAttributeType(attType("message"))));
		assertEquals("world", obsel3.getAttributeValue(emptyFac.createAttributeType(attType("from"))));
		assertEquals("hello yourself", obsel3.getAttributeValue(emptyFac.createAttributeType(attType("message"))));
		assertEquals(null, obsel5.getAttributeValue(emptyFac.createAttributeType(attType("attperso"))));
		assertEquals(null, obsel5.getAttributeValue(emptyFac.createAttributeType("http://www.example.com/prop")));
	}

	@Test
	public void testAddOutgoingRelation() {
		Obsel obsel8 = loadInHolder("base1/t01/obs8", "obsel8.ttl", Obsel.class);
		
		assertEquals(0, KtbsUtils.count(obsel8.listOutgoingRelations()));
		
		RelationType relation = holder.getResource("http://localhost:8001/base1/model1/onChannel", RelationType.class);
		Obsel obsel = trace.getObsel("http://localhost:8001/base1/t01/obs1");
		obsel8.addOutgoingRelation(relation, obsel);
		
		assertEquals(1, KtbsUtils.count(obsel8.listOutgoingRelations()));
		assertEquals(obsel8.getTargetObsel(relation),obsel);
		
		try {
			obsel1.addOutgoingRelation(holder.getResource("http://localhost:8001/base1/model1/closes", RelationType.class), obsel);
			fail("Should fail");
		} catch(DomainException e) {
			
		}
	}

	@Test
	public void testAddIncomingRelation() {
		Trace t02 = loadInHolder("base1/t02/", "t02.ttl", StoredTrace.class);
		Obsel openChat = t02.getObsel("http://localhost:8001/base1/t02/obs1");
		Obsel obsel8 = t02.getObsel("http://localhost:8001/base1/t02/obs8");
		RelationType relation = holder.getResource("http://localhost:8001/base1/model1/onChannel", RelationType.class);
		
		assertEquals(3, KtbsUtils.count(openChat.listIncomingRelations()));
		openChat.addIncomingRelation(obsel8, relation);
		assertEquals(4, KtbsUtils.count(openChat.listIncomingRelations()));
		
		try {
			obsel8.addIncomingRelation(openChat, relation);
			fail("Should fail");
		} catch(RangeException e) {
			
		}
	}

	@Test
	public void testListIncomingRelations() {
		trace.addObsel(obsel6);
		
		assertEquals(4,KtbsUtils.count(obsel1.listIncomingRelations()));
		assertContains(obsel1.listIncomingRelations(), newRelType("onChannel"), newObsel("91eda250f267fa93e4ece8f3ed659139"),false);
		assertContains(obsel1.listIncomingRelations(), newRelType("onChannel"), newObsel("a08667b20cfe4079d02f2f5ad9239575"),false);
		assertContains(obsel1.listIncomingRelations(), newRelType("onChannel"), newObsel("017885b093580cee5e01573953fbd26f"),false);
		
		assertEquals(0,KtbsUtils.count(obsel2.listIncomingRelations()));
		
		assertEquals(0,KtbsUtils.count(obsel3.listIncomingRelations()));
		
		assertEquals(0,KtbsUtils.count(obsel4.listIncomingRelations()));
		
		try {
			obsel6.listIncomingRelations().hasNext();
		} catch(KtbsResourceNotFoundException e) {
			fail("Should never need to pass through the obsel type in t" +
					"he relation selector, since there is no incoming relation.");
		} catch(Exception e) {
			fail("Unexpected exception");
		}
	}

	@Test
	public void testListOutgoingRelations() {
		trace.addObsel(obsel5);
		trace.addObsel(obsel6);
		
		assertEquals(0,KtbsUtils.count(obsel1.listOutgoingRelations()));
		
		assertEquals(1,KtbsUtils.count(obsel2.listOutgoingRelations()));
		assertContains(obsel2.listOutgoingRelations(), newRelType("onChannel"), newObsel("obs1"),true);
		
		assertEquals(1,KtbsUtils.count(obsel3.listOutgoingRelations()));
		assertContains(obsel3.listOutgoingRelations(), newRelType("onChannel"), newObsel("obs1"),true);
		
		assertEquals(1,KtbsUtils.count(obsel4.listOutgoingRelations()));
		assertContains(obsel4.listOutgoingRelations(), newRelType("onChannel"), newObsel("obs1"),true);
		
		try {
			Iterator<RelationStatement> listOutgoingRelations = obsel6.listOutgoingRelations();
			listOutgoingRelations.hasNext();
			fail("Should fail since the obsel type is not valid");
			
		} catch(KtbsResourceNotFoundException e) {
			
		} catch(Exception e) {
			fail("Unexpected exception");
		}
	}


	private Obsel newObsel(String string) {
		return emptyFac.createObsel("http://localhost:8001/base1/t01/"+string);
	}
	
	
	private RelationType newRelType(String string) {
		return emptyFac.createRelationType("http://localhost:8001/base1/model1/"+string);
	}

	
	@Test
	public void testGetTargetObsel() {
		RelationType onChannel = holder.getResource("http://localhost:8001/base1/model1/onChannel", RelationType.class);
		RelationType closes = holder.getResource("http://localhost:8001/base1/model1/closes", RelationType.class);

		assertNull(obsel1.getTargetObsel(onChannel));
		assertNull(obsel1.getTargetObsel(closes));
		assertEquals(obsel1, obsel2.getTargetObsel(onChannel));
		assertEquals(obsel1, obsel3.getTargetObsel(onChannel));
		assertEquals(obsel1, obsel4.getTargetObsel(onChannel));
	}

	
	@Test
	public void testSetSourceObsel() {
		RelationType onChannel = holder.getResource("http://localhost:8001/base1/model1/onChannel", RelationType.class);
		
		Collection<Obsel> sourceObsels = new LinkedList<Obsel>();
		sourceObsels.add(obsel2);
		sourceObsels.add(obsel3);
		sourceObsels.add(obsel4);
		sourceObsels.contains(obsel1.getTargetObsel(onChannel));
	}
	
	@Test
	public void testGetSourceObsel() {
		Trace filtered1 = loadInHolder("base1/filtered1/", "filtered1-obsels.ttl", ComputedTrace.class);
		Obsel o = filtered1.getObsel("http://localhost:8001/base1/filtered1/obs1");
		Obsel source = o.getSourceObsel();
		assertEquals("http://localhost:8001/base1/t01/obs1", source.getURI());
	}
	
	@Test
	public void testSetEndDTAsDate() {
		try {
			assertEquals(sdf.parse("2010-04-28 20:09:01"),obsel1.getEndDTAsDate());
			Date d = new Date();
			obsel1.setEndDTAsDate(d);
			assertEquals(d,obsel1.getEndDTAsDate());
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testSetBeginDTAsDate() {
		try {
			assertEquals(sdf.parse("2010-04-28 20:09:01"),obsel1.getBeginDTAsDate());
			Date d = new Date();
			obsel1.setBeginDTAsDate(d);
			assertEquals(d,obsel1.getBeginDTAsDate());
		} catch (ParseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	@Test
	public void testGetBeginDTAsDate() {
		try {
			assertEquals(sdf.parse("2010-04-28 20:09:01"),obsel1.getBeginDTAsDate());
			assertEquals(sdf.parse("2010-04-28 20:09:02"),obsel2.getBeginDTAsDate());
			assertEquals(sdf.parse("2010-04-28 20:09:05"),obsel3.getBeginDTAsDate());
			assertEquals(sdf.parse("2010-04-28 20:09:07"),obsel4.getBeginDTAsDate());
			assertNull(obsel5.getBeginDTAsDate());
			assertEquals(sdf.parse("2010-04-28 20:09:12"),obsel6.getBeginDTAsDate());
		} catch (ParseException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
	}

	@Test
	public void testGetEndDTAsDate() {
		try {
			assertEquals(sdf.parse("2010-04-28 20:09:01"),obsel1.getEndDTAsDate());
			assertEquals(sdf.parse("2010-04-28 20:09:04"),obsel2.getEndDTAsDate());
			assertEquals(sdf.parse("2010-04-28 20:09:05"),obsel3.getEndDTAsDate());
			assertEquals(sdf.parse("2010-04-28 20:09:07"),obsel4.getEndDTAsDate());
			assertNull(obsel5.getEndDTAsDate());
			assertEquals(sdf.parse("2010-04-28 20:09:14"),obsel6.getEndDTAsDate());
		} catch (ParseException e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		}
	}

}

