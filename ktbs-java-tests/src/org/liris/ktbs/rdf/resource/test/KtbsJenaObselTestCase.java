package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;
import java.text.ParseException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;

import com.ibm.icu.text.SimpleDateFormat;

public class KtbsJenaObselTestCase  extends TestCase {

	private final class SimpleAttributeStatement implements AttributeStatement {
		private AttributeType type;
		private Object value;


		private SimpleAttributeStatement(AttributeType type, Object value) {
			super();
			this.type = type;
			this.value = value;
		}

		@Override
		public Object getValue() {
			return value;
		}

		@Override
		public AttributeType getAttributeType() {
			return type;
		}
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	private EmptyResourceFactory emptyFac = EmptyResourceFactory.getInstance();


	private Obsel obsel1;
	private Obsel obsel2;
	private Obsel obsel3;
	private Obsel obsel4;
	private Obsel obsel5;
	private Obsel obsel6;

	@Before
	public void setUp() throws Exception {
		FileInputStream fis = new FileInputStream("turtle/t01.ttl");
		Trace trace = KtbsJenaResourceFactory.getInstance().createStoredTrace(
				"http://localhost:8001/base1/t01/", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();

		obsel1 = trace.getObsel("http://localhost:8001/base1/t01/obs1");
		obsel2 = trace.getObsel("http://localhost:8001/base1/t01/017885b093580cee5e01573953fbd26f");
		obsel3 = trace.getObsel("http://localhost:8001/base1/t01/91eda250f267fa93e4ece8f3ed659139");
		obsel4 = trace.getObsel("http://localhost:8001/base1/t01/a08667b20cfe4079d02f2f5ad9239575");

		fis = new FileInputStream("turtle/obsel5.ttl");
		obsel5 = KtbsJenaResourceFactory.getInstance().createObsel(
				"http://localhost:8001/base1/t01/obs5", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();

		fis = new FileInputStream("turtle/obsel6.ttl");
		obsel6 = KtbsJenaResourceFactory.getInstance().createObsel(
				"http://localhost:8001/base1/t01/obs6", 
				fis, 
				JenaConstants.JENA_SYNTAX_TURTLE);
		fis.close();
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
	public void testGetObselType() {
		assertEquals(emptyFac.createObselType(type("OpenChat")),obsel1.getObselType());
		assertEquals(emptyFac.createObselType(type("SendMsg")),obsel2.getObselType());
		assertEquals(emptyFac.createObselType(type("RecvMsg")),obsel3.getObselType());
		assertEquals(emptyFac.createObselType(type("CloseChat")),obsel4.getObselType());
		assertEquals(emptyFac.createObselType(type("SendMsg")),obsel5.getObselType());
		assertEquals(emptyFac.createObselType("http://example.com/Type1"),obsel6.getObselType());
	}

	private static String type(String localType) {
		return "http://localhost:8001/base1/model1/" + localType;
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
		fail("Cannot be tested before the resource holder is created");
	}

	private String attType(String string) {
		return "http://localhost:8001/base1/model1/" + string;
	}

	@Test
	public void testGetAttributeValue() {
		fail("Cannot be tested before the resource holder is created");

//		assertEquals("#my-channel", obsel1.getAttributeValue(emptyFac.createAttributeType(attType("channel"))));
//		assertEquals(null, obsel1.getAttributeValue(emptyFac.createAttributeType(attType("channl"))));
//		assertEquals("hello world", obsel2.getAttributeValue(emptyFac.createAttributeType(attType("message"))));
//		assertEquals("world", obsel3.getAttributeValue(emptyFac.createAttributeType(attType("from"))));
//		assertEquals("hello yourself", obsel3.getAttributeValue(emptyFac.createAttributeType(attType("message"))));
//		assertEquals("Valeur Perso", obsel5.getAttributeValue(emptyFac.createAttributeType(attType("attperso"))));
//		assertEquals("Ceci n'est pas un attribut", obsel5.getAttributeValue(emptyFac.createAttributeType("http://www.example.com/prop")));
	}

	@Test
	public void testAddOutgoingRelation() {
		try {
			obsel1.addOutgoingRelation(null);
			fail("Should have failed");
		} catch(UnsupportedOperationException e) {

		} catch(Exception e) {
			fail("Unexcepted exception");
		}
	}

	@Test
	public void testAddIncomingRelation() {
		try {
			obsel1.addIncomingRelation(null);
			fail("Should have failed");
		} catch(UnsupportedOperationException e) {

		} catch(Exception e) {
			fail("Unexcepted exception");
		}
	}

	@Test
	public void testListIncomingRelations() {
		fail("Cannot be tested before the resource holder is created");
	}

	@Test
	public void testListOutgoingRelations() {
		fail("Cannot be tested before the resource holder is created");
	}

	@Test
	public void testGetTargetObsel() {
		fail("Cannot be tested before the resource holder is created");
	}

	@Test
	public void testGetSourceObsel() {
		fail("Cannot be tested before the resource holder is created");
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
