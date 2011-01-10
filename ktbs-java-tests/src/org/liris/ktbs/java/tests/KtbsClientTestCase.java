package org.liris.ktbs.java.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.client.KtbsClientApplication;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.client.KtbsResponseStatus;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.RelationStatement;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.rdf.InvalidDeserializationRequest;

import com.ibm.icu.util.Calendar;

public class KtbsClientTestCase {
	private static final String ktbsLocalPath = "bin"+File.separator+"ktbs-full";
	private static final String KTBS_PROPERTIES_FILE = "ktbs-tests.properties";
	private static final String PROP_ROOT_URI = "ktbs.root.uri";
	private static final String PROP_KTBS_INSTALL_PATH = "ktbs.install.path";

	private static String KTBS_INSTALL_PATH;

	private static String rootURI;
	private KtbsClientApplication app;

	private static Properties properties;

	private static Collection<String> obselTypes = new LinkedList<String>();


	private static Log log = LogFactory.getLog(KtbsClientTestCase.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		properties = new Properties();
		properties.load(KtbsClientTestCase.class.getClassLoader().getResourceAsStream(KTBS_PROPERTIES_FILE));
		KTBS_INSTALL_PATH = properties.getProperty(PROP_KTBS_INSTALL_PATH);
		rootURI = properties.getProperty(PROP_ROOT_URI);

		obselTypes.add("http://localhost:8001/base1/model1/SendMsg");
		obselTypes.add("http://localhost:8001/base1/model1/OpenChat");
		obselTypes.add("http://localhost:8001/base1/model1/CloseChat");
		obselTypes.add("http://localhost:8001/base1/model1/RecvMsg");
	}

	private KtbsClient client;

	@Before
	public void setUp() throws Exception {
		
		log.info("Populating the KTBS");
		Process populateKTBS = new ProcessBuilder(KTBS_INSTALL_PATH+File.separator+"tests" + File.separator+"populate-ktbs").start();
		populateKTBS.waitFor();
		log.info("KTBS populated");


		log.info("Populating the trace t01");
		Process populateT01 = new ProcessBuilder(KTBS_INSTALL_PATH+File.separator+"tests" + File.separator+"populate-t01").start();
		populateT01.waitFor();
		log.info("Trace t01 populated");

		log.info("Root URI: " + rootURI);

		app=KtbsClientApplication.getInstance();
		client = app.getKtbsClient(rootURI);
		client.startSession();
	}

	@After
	public void tearDown() throws Exception {
		client.closeSession();
	}

	@Test
	public void testGetRoot() {
		KtbsResponse response = client.getKtbsRoot();
		assertResponseSucceeded(response, true);
		assertNull(response.getHTTPETag());
		KtbsRoot root = (KtbsRoot) response.getBodyAsKtbsResource();
		assertEquals(rootURI,root.getURI());
		assertTrue(root.getBaseURIs().size()>=1);
		assertTrue(root.getBaseURIs().contains("http://localhost:8001/base1/"));
	}

	@Test
	public void testCreateBase() {
		KtbsResponse response1 = client.getKtbsRoot();
		assertResponseSucceeded(response1, true);
		KtbsRoot root1 = (KtbsRoot) response1.getBodyAsKtbsResource();


		String label = "Mon label de la base créée";


		int nb = new Random().nextInt();
		while(client.getBase("base"+nb).executedWithSuccess()){nb++;}

		String nomBase = "base"+nb;
		/*
		 *  TODO KTBS Bug, the KTBS treats absolute paths as relative path 
		 *  when it creates the resources. Consequently, the URI of created resource is 
		 *  http://localhost:8001/http://localhost:8001/baseCreated/ instead of http://localhost:8001/baseCreated/.
		 *  
		 *  TODO Jena Bug, a good way to evercome this issue is to use
		 *  relative paths as expected, but unformatunately, there is a Jena bug:
		 *  the support for base Uri in serialization is not supported.
		 */
		KtbsResponse response = client.createBase(nomBase, label);
		assertResponseSucceeded(response, false);

		assertNotNull(response.getHTTPLocation());
		String location = response.getHTTPLocation();
		try {
			/*
			 * TODO KTBS bug : why does this return a double slash at the end :
			 * "http://localhost:8001/base2//" instead of "http://localhost:8001/base2/"
			 * 
			 * FIXME return normalized URI in getHTTPLocation().
			 */
			assertEquals(rootURI+nomBase+"/", new URI(location).normalize().toString());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		response = client.getBase(nomBase);
		assertResponseSucceeded(response, true);
		Base base2 = (Base) response.getBodyAsKtbsResource();
		assertEquals(base2.getLabel(), label);
		assertEquals(base2.getURI(), rootURI+nomBase+"/");


		KtbsResponse response2 = client.getKtbsRoot();
		assertResponseSucceeded(response2, true);
		KtbsRoot root2 = (KtbsRoot) response2.getBodyAsKtbsResource();


		assertEquals(root1.getBaseURIs().size()+1, root2.getBaseURIs().size());
		assertTrue(root2.getBaseURIs().contains(rootURI+nomBase+"/"));

	}

	@Test
	public void testGetBase() {

		KtbsResponse response = client.getBase("base1");
		assertResponseSucceeded(response, true);
		assertEquals(response.getKtbsStatus(),KtbsResponseStatus.RESOURCE_RETRIEVED);
		//		assertNotNull(response.getHTTPETag());

		KtbsResponse response2 = client.getBase("base2");
		assertResponseFailed(response2);
		KtbsResponse response3 = client.getBase("base1/tyd");
		assertResponseFailed(response3);
		KtbsResponse response4 = client.getBase(rootURI+"base1/");
		assertResponseFailed(response4);
	}

	private static void assertResponseSucceeded(KtbsResponse response, boolean shouldHaveBody) {
		assertNotNull(response);
		assertTrue(response.executedWithSuccess());
		if(shouldHaveBody)
			assertNotNull(response.getBodyAsKtbsResource());
		else
			assertNull(response.getBodyAsKtbsResource());
		assertNotNull(response.getHTTPResponse());
	}

	private static void assertResponseFailed(KtbsResponse response) {
		assertNotNull(response);
		assertFalse(response.executedWithSuccess());
		assertEquals(response.getKtbsStatus(),KtbsResponseStatus.SERVER_EXCEPTION);
		assertNull(response.getBodyAsKtbsResource());
		assertNotNull(response.getHTTPResponse());
	}

	@Test
	public void testGetBaseFromURI() {
		KtbsResponse response = client.getBaseFromURI(rootURI+"base1/");
		assertResponseSucceeded(response, true);
	}

	@Test
	public void testCreateTrace() {

		String base1URI = "http://localhost:8001/base1/";
		KtbsResponse response1 = client.getBaseFromURI(base1URI);
		assertResponseSucceeded(response1, true);
		Base base = (Base) response1.getBodyAsKtbsResource();

		String label = "Ma trace préférée";

		int nb = new Random().nextInt();
		while(client.getTraceInfo("base1","trace"+nb).executedWithSuccess()){nb++;}

		String nomTrace = "trace"+nb;
		Date origin = new Date();
		String traceModel1URI = "http://localhost:8001/base1/model1/";
		KtbsResponse response = client.createTrace(base1URI, nomTrace, traceModel1URI, origin, label);
		assertResponseSucceeded(response, false);

		assertNotNull(response.getHTTPLocation());
		String location = response.getHTTPLocation();
		String traceURI = base1URI+nomTrace+"/";
		try {
			/*
			 * TODO KTBS bug : why does this return a double slash at the end :
			 * "http://localhost:8001/base2//" instead of "http://localhost:8001/base2/"
			 * 
			 * FIXME return normalized URI in getHTTPLocation().
			 */
			assertEquals(traceURI, new URI(location).normalize().toString());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		response = client.getTraceInfo(traceURI);
		assertResponseSucceeded(response, true);
		Trace trace = (Trace) response.getBodyAsKtbsResource();
		assertEquals(trace.getLabel(), label);
		assertEquals(trace.getBaseURI(), base1URI);
		assertNull(trace.getBase());
		assertEquals(trace.getTraceModelUri(),traceModel1URI);
		assertEquals(trace.listObsels().size(),0);
		assertEquals(trace.listObselURIs().size(),0);
		assertEquals(origin,trace.getOrigin());
		assertEquals(trace.getURI(), traceURI);

		KtbsResponse response2 = client.getBase("base1");
		assertResponseSucceeded(response2, true);
		Base base2 = (Base) response2.getBodyAsKtbsResource();

		assertEquals(base.getTraceURIs().size()+1, base2.getTraceURIs().size());
		assertTrue(base2.getTraceURIs().contains(traceURI));
	}


	@Test
	public void testGetTraceObselsString() {
		KtbsResponse responseObsels = client.getTraceObsels(rootURI+"base1/t01/");
		assertResponseSucceeded(responseObsels, true);
		assertNotNull(responseObsels.getHTTPETag());
		Trace trace = (Trace)responseObsels.getBodyAsKtbsResource();
		assertNull(trace.getOrigin());
		assertNull(trace.getTraceModelUri());
		assertNull(trace.getBaseURI());
		assertEquals(rootURI+"base1/t01/",trace.getURI());


		/*
		 * Cannot perform a reliable check on the number of obsels 
		 * since the population of the KTBS is performed multiple times 
		 * per TestCase (once for each method).
		 */
		assertTrue(trace.listObsels().size()==trace.listObselURIs().size());
		assertTrue("Size: " + trace.listObsels().size() + " Trace URI: " + trace.getURI(),trace.listObsels().size()>=4);
		for(Obsel obsel:trace.listObsels()) {
			assertTrue(obselTypes.contains(obsel.getObselType()));
			assertEquals(rootURI+"base1/t01/", obsel.getTraceURI());
			assertNotNull(obsel.getBegin());
			assertNotNull(obsel.getBeginDT());
			assertNotNull(obsel.getEnd());
			assertNotNull(obsel.getEndDT());
			if(!obsel.getObselType().equals("http://localhost:8001/base1/model1/OpenChat")) {
				// Checks the relation onChannel
				assertEquals(1,obsel.listOutgoingRelations().size());
				Obsel targetObsel = obsel.getTargetObsel("http://localhost:8001/base1/model1/onChannel");
				assertNotNull(targetObsel);
				assertEquals(rootURI+"base1/t01/",targetObsel.getTraceURI());
			}
		}

		KtbsResponse responseInfo = client.getTraceInfo(rootURI+"base1/t01/");
		assertResponseSucceeded(responseInfo, true);
		assertNotNull(responseInfo.getHTTPETag());

		// ETags must be different for the about and obsels aspect
		assertFalse(responseObsels.getHTTPETag().equals(responseInfo.getHTTPETag()));


		KtbsResponse response = client.getTraceObsels(rootURI+"base1////t01//////");
		assertResponseSucceeded(response, true);
		trace = (Trace)response.getBodyAsKtbsResource();
		assertEquals(rootURI+"base1/t01/",trace.getURI());

		response = client.getTraceObsels(rootURI+"base1/t01/@obsels");
		assertResponseSucceeded(response, true);
		trace = (Trace)response.getBodyAsKtbsResource();
		assertEquals(rootURI+"base1/t01/",trace.getURI());

		response = client.getTraceObsels(rootURI+"base1/t01/@about");
		assertResponseSucceeded(response, true);
		trace = (Trace)response.getBodyAsKtbsResource();
		assertEquals(rootURI+"base1/t01/",trace.getURI());

		response = client.getTraceObsels(rootURI+"base1/t01");
		assertResponseFailed(response);

		response = client.getTraceObsels(rootURI+"base2/t01");
		assertResponseFailed(response);

		response = client.getTraceObsels(rootURI+"base1/t02");
		assertResponseFailed(response);

		response = client.getTraceObsels(rootURI+"base1/t01@obsels");
		assertResponseFailed(response);

		response = client.getTraceObsels(rootURI+"base1/t01@about");
		assertResponseFailed(response);
	}

	@Test
	public void testGetTraceInfoString() {
		KtbsResponse response = client.getTraceInfo(rootURI+"base1/t01/");
		assertResponseSucceeded(response, true);
		assertNotNull(response.getHTTPETag());
		Trace trace = (Trace)response.getBodyAsKtbsResource();
		assertEquals(0,trace.listObsels().size());
		assertEquals(0,trace.listObselURIs().size());
		assertEquals(rootURI+"base1/t01/",trace.getURI());

		response = client.getTraceInfo(rootURI+"base1/t01/@obsels");
		assertResponseSucceeded(response, true);
		assertNotNull(response.getHTTPETag());

		response = client.getTraceInfo(rootURI+"base1/t01/@about");
		assertResponseSucceeded(response, true);
		assertNotNull(response.getHTTPETag());


		//		try {
		/*
		 * TODO to be uncomment after checking the date format on the Internet
		 * 	assertEquals(((Trace)response.getBodyAsKtbsResource()).getOrigin(), DATE_FORMAT.parse("2010-04-28T18:09:00"));
		 */
		assertEquals(trace.getTraceModelUri(), rootURI+"base1/model1/");
		//		} catch (ParseException e) {
		//			fail(e.getMessage());
		//		}

		KtbsResponse response2 = client.getTraceInfo(rootURI+"base2/trace2/");
		assertResponseFailed(response2);


		KtbsResponse response3 = client.getTraceInfo(rootURI+"base1/t01");
		assertResponseFailed(response3);

		KtbsResponse response4 = client.getTraceInfo(rootURI+"base1/t01@obsels");
		assertResponseFailed(response4);

		KtbsResponse response5 = client.getTraceInfo(rootURI+"base1/t01@about");
		assertResponseFailed(response5);

		try {
			client.getTraceInfo("http://localhost:8002/"+"t01/");
			client.getTraceInfo("http://localhost:8001/root1/"+"t01/");
			assertFalse("Should throw an exception",true);
		} catch(IllegalStateException e) {
		}
	}

	@Test
	public void testGetTraceInfoStringString() {
		KtbsResponse response = client.getTraceInfo("base1", "t01");
		assertResponseSucceeded(response, true);
		assertNotNull(response.getHTTPETag());

		response = client.getTraceInfo("base1", "t01/");
		assertResponseSucceeded(response, true);
		assertNotNull(response.getHTTPETag());


		response = client.getTraceInfo("base1/", "t01");
		assertResponseSucceeded(response, true);
		assertNotNull(response.getHTTPETag());

		response = client.getTraceInfo("base1/////", "t01////");
		assertResponseSucceeded(response, true);
		assertNotNull(response.getHTTPETag());

		response = client.getTraceInfo("base2", "t01@obsels");
		assertResponseFailed(response);

		response = client.getTraceInfo("base2", "t01@about");
		assertResponseFailed(response);


		response = client.getTraceInfo("base2", "t01");
		assertResponseFailed(response);

		response = client.getTraceInfo("base1", "t02");
		assertResponseFailed(response);

		response = client.getTraceInfo("base1", "t02");
		assertResponseFailed(response);
	}

	@Test
	public void testGetObselStringString() {
		KtbsResponse response = client.getObsel("http://localhost:8001/base1/t01/","obs1");		
		assertResponseSucceeded(response, true);

		response = client.getObsel("http://localhost:8001/base1/t01","obs1");		
		assertResponseFailed(response);

		response = client.getObsel("http://localhost:8001/base1/t01/","obs1/");		
		assertResponseFailed(response);

		response = client.getObsel("http://localhost:8001/////base1/////t01///","obs1");		
		assertResponseSucceeded(response, true);
	}

	@Test
	public void testGetObselStringStringString() {
		KtbsResponse response = client.getObsel("base1","t01","obs1");		
		assertResponseSucceeded(response, true);

		response = client.getObsel("base1///","t01////","obs1////");		
		assertResponseFailed(response);

		response = client.getObsel("base1///","t01////","obs1");		
		assertResponseSucceeded(response, true);

		response = client.getObsel("http://localhost:8001/base1","t01","obs1");		
		assertResponseFailed(response);

		response = client.getObsel("base1","t01","obs1/");		
		assertResponseFailed(response);

		response = client.getObsel("base1","t01","obs2");		
		assertResponseFailed(response);

		response = client.getObsel("http://localhost:8001/////base1/////t01///","obs1");		
		assertResponseSucceeded(response, true);
	}

	@Test
	public void testGetObselString() {
		KtbsResponse response = client.getTraceObsels("http://localhost:8001/base1/", "t01");		
		Trace trace = (Trace) response.getBodyAsKtbsResource();
		for(String obselURI:trace.listObselURIs()) {
			response = client.getObsel(obselURI);
			assertResponseSucceeded(response, true);
			assertNotNull(response.getHTTPETag());
			Obsel obsel = (Obsel) response.getBodyAsKtbsResource();
			assertEquals("http://localhost:8001/base1/t01/", obsel.getTraceURI());
			assertNull(obsel.getTrace());
			assertTrue(obsel.getBegin()!=-1l);
			assertTrue(obsel.getEnd()!=-1l);
			assertNotNull(obsel.getBeginDT());
			assertNotNull(obsel.getEndDT());
			assertNotNull(obsel.getSubject());
			for(RelationStatement relation:obsel.listOutgoingRelations()){
				assertNotNull(relation.getToObselURI());
				assertNull(relation.getToObsel());
			}
			for(RelationStatement relation:obsel.listIncomingRelations()) {
				assertNotNull(relation.getFromObselURI());
				assertNull(relation.getFromObsel());
			}
		}
		for(String obselURI:trace.listObselURIs()) {
			response = client.getObsel(obselURI+"/");
			assertResponseFailed(response);

			response = client.getObsel(obselURI+"///////");
			assertResponseFailed(response);
		}
	}

	@Test
	public void testGetTraceObselsStringString() {
		KtbsResponse response = client.getTraceObsels("http://localhost:8001/base1/", "t01");		
		assertResponseSucceeded(response, true);

		response = client.getTraceObsels("http://localhost:8001///base1////", "t01//");		
		assertResponseSucceeded(response, true);

		response = client.getTraceObsels("http://localhost:8001/base1", "t01");		
		assertResponseFailed(response);

		response = client.getTraceObsels("http://localhost:8001/base1/", "tr01");		
		assertResponseFailed(response);

		response = client.getTraceObsels("http://localhost:8001/base2/", "t01");		
		assertResponseFailed(response);
	}

	@Test
	public void testGetTraceObselsStringStringString() {
		KtbsResponse response = client.getTraceObsels("http://localhost:8001/","base1", "t01");		
		assertResponseSucceeded(response, true);

		response = client.getTraceObsels("http://localhost:8001/","base1", "t01@obsels");		
		assertResponseFailed(response);

		response = client.getTraceObsels("http://localhost:8001/","base1", "t01@about");		
		assertResponseFailed(response);

		response = client.getTraceObsels("http://localhost:8001/","base1/////", "t01///");		
		assertResponseSucceeded(response, true);

		response = client.getTraceObsels("http://localhost:8001/","base2", "t01");		
		assertResponseFailed(response);

		response = client.getTraceObsels("http://localhost:8001/","base1", "tr01");		
		assertResponseFailed(response);
	}

	@Test
	public void testCreateObsel() {
		String trace1URI = "http://localhost:8001/base1/t01/";
		KtbsResponse response1 = client.getTraceObsels(trace1URI);
		assertResponseSucceeded(response1, true);
		Trace traceObsels1 = (Trace) response1.getBodyAsKtbsResource();
		KtbsResponse response2 = client.getTraceInfo(trace1URI);
		assertResponseSucceeded(response2, true);
		Trace traceInfo1 = (Trace) response2.getBodyAsKtbsResource();


		String label = "Mon obsel préférée";

		int nb = new Random().nextInt();
		String obselName = "obsel"+nb;
		while(client.getObsel("base1","t01", obselName).executedWithSuccess()){obselName = "obsel"+nb++;}

		String nomObsel = obselName;

		Map<String, Object> attributes = new HashMap<String, Object>();
		String attributeValue = "BBC 2";
		String attributeName = "http://localhost:8001/base1/model1/channel";
		attributes.put(attributeName, attributeValue);

		String subject = "Damien Cram";
		String typeURI = "http://localhost:8001/base1/model1/OpenChat";
		long relativeStart = 12000;
		long relativeEnd = 13000;

		KtbsResponse response = client.createObsel(
				trace1URI, 
				nomObsel, 
				subject, 
				label, 
				typeURI, 
				null, 
				null, 
				relativeStart, 
				relativeEnd, 
				attributes);

		assertResponseSucceeded(response, false);

		assertNotNull(response.getHTTPLocation());
		String location = response.getHTTPLocation();
		String obsel1URI = trace1URI+nomObsel;
		try {
			/*
			 * TODO KTBS bug : why does this return a double slash at the end :
			 * "http://localhost:8001/base2//" instead of "http://localhost:8001/base2/"
			 * 
			 * FIXME return normalized URI in getHTTPLocation().
			 */
			assertEquals(obsel1URI, new URI(location).normalize().toString());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		response = client.getObsel(obsel1URI);
		assertResponseSucceeded(response, true);
		Obsel obsel = (Obsel) response.getBodyAsKtbsResource();
		assertEquals(obsel.getLabel(), label);
		assertEquals(obsel.getTraceURI(), trace1URI);
		assertEquals(1, obsel.listAttributes().size());
		assertEquals(obsel.getObselType(),typeURI);
		assertEquals(obsel.listIncomingRelations().size(),0);
		assertEquals(obsel.listOutgoingRelations().size(),0);
		assertTrue(obsel.getBegin()!=-1l);
		assertTrue(obsel.getEnd()!=-1l);
		assertNotNull(obsel.getBeginDT());
		assertEquals(traceInfo1.getOrigin().getTime()+relativeStart, obsel.getBeginDT().getTime());
		assertEquals(traceInfo1.getOrigin().getTime()+relativeEnd, obsel.getEndDT().getTime());
		assertNotNull(obsel.getEndDT());
		assertEquals(attributeValue,obsel.getAttributeValue(attributeName));
		assertEquals(obsel1URI, obsel.getURI());

		KtbsResponse response3 = client.getTraceInfo(trace1URI);
		assertResponseSucceeded(response3, true);
		Trace trace2Info = (Trace) response2.getBodyAsKtbsResource();
		KtbsResponse response4 = client.getTraceObsels(trace1URI);
		assertResponseSucceeded(response4, true);
		Trace trace2obsels = (Trace) response4.getBodyAsKtbsResource();
		assertEquals(true, trace2Info.isCompliantWithModel());
		assertEquals(traceObsels1.listObselURIs().size()+1, trace2obsels.listObselURIs().size());
		assertEquals(traceObsels1.listObsels().size()+1, trace2obsels.listObsels().size());
		assertTrue(trace2obsels.listObselURIs().contains(obsel1URI));
		assertFalse(traceObsels1.listObselURIs().contains(obsel1URI));
		assertTrue(trace2obsels.listObselURIs().containsAll(traceObsels1.listObselURIs()));

		while(client.getObsel("base1","t01", obselName).executedWithSuccess()){obselName="obsel"+nb++;}

		Map<String, Object> attributes2 = new HashMap<String, Object>();
		String attMessage = "http://localhost:8001/base1/model1/message";
		String message = "Hello Girl";
		attributes2.put(attMessage, message);
		String typeObs2 = "http://localhost:8001/base1/model1/SendMsg";


		String relationURi = "http://localhost:8001/base1/model1/onChannel";
		response = client.createObsel(
				"http://localhost:8001/base1/t01/", 
				obselName, 
				"dam", 
				"Mon 6ième observé", 
				typeObs2, 
				null, 
				null, 
				14000l, 
				14000l, 
				attributes2,
				relationURi,
				obsel1URI
		);

		assertResponseSucceeded(response, false);
		String obsel2URI = trace1URI+obselName;


		response = client.getObsel(obsel2URI);
		assertResponseSucceeded(response, true);
		Obsel obsel2 = (Obsel) response.getBodyAsKtbsResource();

		response = client.getObsel(obsel1URI);
		assertResponseSucceeded(response, true);
		Obsel obsel1 = (Obsel) response.getBodyAsKtbsResource();

		assertEquals(1,obsel2.listOutgoingRelations().size());
		assertEquals(0,obsel2.listIncomingRelations().size());
		assertEquals(0,obsel1.listOutgoingRelations().size());
		/*
		 * There is no simple way to retrieve incoming relations from the KTBS Server.
		 */
		assertEquals(0,obsel1.listIncomingRelations().size());


		RelationStatement relation = obsel2.listOutgoingRelations().iterator().next();
		assertNull(relation.getFromObsel());
		assertNull(relation.getToObsel());
		assertNotNull(relation.getFromObselURI());
		assertNotNull(relation.getToObselURI());
		assertEquals(obsel2URI, relation.getFromObselURI());
		assertEquals(obsel1URI, relation.getToObselURI());
		assertEquals(relationURi, relation.getRelationName());


		response = client.getTraceInfo(trace1URI);
		assertResponseSucceeded(response, true);
		Trace trace = (Trace) response.getBodyAsKtbsResource();
		assertEquals(true, trace.isCompliantWithModel());


		//test d'ajout d'observé avec temps absolu
		nb = new Random().nextInt();
		obselName = "obsel"+nb;
		while(client.getObsel("base1","t01", obselName).executedWithSuccess()){obselName = "obsel"+nb++;}

		nomObsel = obselName;

		attributes = new HashMap<String, Object>();
		attributeValue = "BBC 3";
		attributeName = "http://localhost:8001/base1/model1/message";
		attributes.put(attributeName, attributeValue);

		subject = "Damien Cram";
		typeURI = "http://localhost:8001/base1/model1/OpenChat";
		relativeStart = 20000;
		relativeEnd = 21000;

		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(traceInfo1.getOrigin().getTime()+relativeStart);
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(traceInfo1.getOrigin().getTime()+relativeEnd);


		response = client.createObsel(
				trace1URI, 
				nomObsel, 
				subject, 
				label, 
				typeURI, 
				start.getTime(), 
				end.getTime(), 
				-1, 
				-1, 
				attributes);

		assertResponseSucceeded(response, false);

		response = client.getObsel(trace1URI+obselName);

		assertResponseSucceeded(response, true);
		obsel = (Obsel) response.getBodyAsKtbsResource();
		assertEquals(obsel.getLabel(), label);
		assertEquals(obsel.getTraceURI(), trace1URI);
		assertEquals(1, obsel.listAttributes().size());
		assertEquals(obsel.getObselType(),typeURI);
		assertEquals(obsel.listIncomingRelations().size(),0);
		assertEquals(obsel.listOutgoingRelations().size(),0);


		assertNotNull(obsel.getBeginDT());
		assertEquals(start.getTime().getTime(), obsel.getBeginDT().getTime());

		assertNotNull(obsel.getEndDT());
		assertEquals(relativeStart,obsel.getBegin());

		assertEquals(relativeEnd,obsel.getEnd());
		assertEquals(end.getTime().getTime(), obsel.getEndDT().getTime());

		assertEquals(attributeValue,obsel.getAttributeValue(attributeName));
	}

	@Test
	public void testGetKtbsResource() {
		KtbsResponse response;
		response = client.getKtbsResource("http://localhost:8001/", KtbsRoot.class);
		assertResponseSucceeded(response, true);

		response = client.getKtbsResource("http://localhost:8001/base1/", Base.class);
		assertResponseSucceeded(response, true);

		response = client.getKtbsResource("http://localhost:8001/base1/t01/", Trace.class);
		assertResponseSucceeded(response, true);

		response = client.getKtbsResource("http://localhost:8001/base1/t01/@obsels", Trace.class);
		assertResponseSucceeded(response, true);

		response = client.getKtbsResource("http://localhost:8001/base1/t01/@about", Trace.class);
		assertResponseSucceeded(response, true);

		response = client.getKtbsResource("http://localhost:8001/base1/t01/obs1", Obsel.class);
		assertResponseSucceeded(response, true);

		try {
			response = client.getKtbsResource("http://localhozsst:8001/", KtbsRoot.class);
			fail("Should raise an illegal state exception because the root uri is invalid");
		} catch(IllegalStateException e){}

		response = client.getKtbsResource("http://localhost:8001/basse1/", Base.class);
		assertResponseFailed(response);

		response = client.getKtbsResource("http://localhost:8001/base1", Base.class);
		assertResponseFailed(response);

		response = client.getKtbsResource("http://localhost:8001/base1/tde01/", Trace.class);
		assertResponseFailed(response);

		response = client.getKtbsResource("http://localhost:8001/base1/t01", Trace.class);
		assertResponseFailed(response);

		response = client.getKtbsResource("http://localhost:8001/base1/t01/obs1/", Obsel.class);
		assertResponseFailed(response);

		response = client.getKtbsResource("http://localhost:8001/base1/t01/obs290970", Obsel.class);
		assertResponseFailed(response);

		response = client.getKtbsResource("http://localhost:8001/base1/t01/obs1", Trace.class);
		assertResponseFailed(response);
		assertEquals(HttpStatus.SC_NOT_FOUND,response.getHTTPResponse().getStatusLine().getStatusCode());

		try {
			response = client.getKtbsResource("http://localhost:8001/base1/t01/obs1", Base.class);
			fail("Should have failed");
		} catch(InvalidDeserializationRequest e) {}

		try {
			response = client.getKtbsResource("http://localhost:8001/base1/t01/obs1", KtbsRoot.class);
			fail("Should have failed");
		} catch(InvalidDeserializationRequest e) {}

		try {
			response = client.getKtbsResource("http://localhost:8001/base1/t01/", Obsel.class);
		} catch(InvalidDeserializationRequest e) {
			fail("Should not fail with Obsel");
		}

		try {
			response = client.getKtbsResource("http://localhost:8001/base1/t01/", Base.class);
			fail("Should have failed");
		} catch(InvalidDeserializationRequest e) {}

		try {
			response = client.getKtbsResource("http://localhost:8001/base1/t01/", KtbsRoot.class);
			fail("Should have failed");
		} catch(InvalidDeserializationRequest e) {}

		response = client.getKtbsResource("http://localhost:8001/base1/", Trace.class);
		// The server cannot even find the resource http://localhost:8001/base1/@obsels
		assertResponseFailed(response);
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getHTTPResponse().getStatusLine().getStatusCode());


		try {
			response = client.getKtbsResource("http://localhost:8001/base1/", Obsel.class);
		} catch(InvalidDeserializationRequest e) {
			fail("Should not fail with Obsel");
		}
		try {
			response = client.getKtbsResource("http://localhost:8001/base1/", KtbsRoot.class);
			fail("Should have failed");
		} catch(InvalidDeserializationRequest e) {}

		response = client.getKtbsResource("http://localhost:8001/", Trace.class);
		// The server cannot even find the resource http://localhost:8001/@obsels
		assertResponseFailed(response);
		assertEquals(HttpStatus.SC_NOT_FOUND, response.getHTTPResponse().getStatusLine().getStatusCode());

		try {
			response = client.getKtbsResource("http://localhost:8001/", Obsel.class);
		} catch(InvalidDeserializationRequest e) {
			fail("Should not fail with Obsel");
		}
		try {
			response = client.getKtbsResource("http://localhost:8001/", Base.class);
			fail("Should have failed");
		} catch(InvalidDeserializationRequest e) {}

	}

	@Test
	public void testCreateTraceModel() {

		String label = "Mon label de modèle de trace";


		KtbsResponse response = client.getBase("base1");
		assertResponseSucceeded(response, true);
		Base base = (Base) response.getBodyAsKtbsResource();
		String tmName; 
		do 
			tmName = "tm" + new Random().nextInt();
		while(base.getTraceModelURIs().contains(rootURI+"base1/"+tmName+"/")) ;

		KtbsResponse responseTM = client.createTraceModel("base1", tmName, label);
		assertResponseSucceeded(responseTM, false);

		assertNotNull(responseTM.getHTTPLocation());
		String location = responseTM.getHTTPLocation();
		try {
			/*
			 * TODO KTBS bug : why does this return a double slash at the end :
			 * "http://localhost:8001/base2//" instead of "http://localhost:8001/base2/"
			 * 
			 * FIXME return normalized URI in getHTTPLocation().
			 */
			assertEquals(rootURI+"base1/" + tmName+"/", new URI(location).normalize().toString());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}

		response = client.getBase("base1");
		assertResponseSucceeded(response, true);
		Base base2 = (Base)response.getBodyAsKtbsResource();
		assertEquals(base.getTraceModelURIs().size()+1,base2.getTraceModelURIs().size());
		assertTrue(base2.getTraceModelURIs().contains(rootURI+"base1/" + tmName+"/"));
	}


	@Test
	public void testPutTraceObsels() {
		KtbsResponse responseGet = client.getTraceObsels("http://localhost:8001/base1/t01/");
		assertResponseSucceeded(responseGet, true);
		String etagObsels = responseGet.getHTTPETag();
		assertNotNull(etagObsels);
		Trace traceObsels = (Trace) responseGet.getBodyAsKtbsResource();

		responseGet = client.getTraceInfo("http://localhost:8001/base1/t01/");
		assertResponseSucceeded(responseGet, true);
		String etagInfo = responseGet.getHTTPETag();
		assertNotNull(etagInfo);

		assertFalse(etagObsels.equals(etagInfo));


		Collection<Obsel> obsels = new LinkedList<Obsel>();
		String messageAtt = "http://localhost:8001/base1/model1/channel";
		String newSubject = "Nouveau sujet";
		String newLabel = "Nouvelle étiquette";
		/*
		 * TODO Ktbs Bug : if no absolute URI is specified, the KTBS will interprete 
		 * it as a relative URI. So I must provide an absolute URI for the attribute 
		 * name of newAttribute, otherwise the call obsel.getAttributeValue("newAttribute")
		 * would return null.
		 * 
		 */
		String newAttribute = "http://localhost:8001/base1/model1/another-attribute";
		String newAttributeValue = "attribute value";
		String newMessageAttValue = "#my-channel-modified23";
		int cnt = 0;
		String modifiedObselURI = "";
		Map<String, String> etagsByObselURI = new TreeMap<String, String>();

		for(Obsel obsel:traceObsels.listObsels()) {
			KtbsResponse response = client.getObsel(obsel.getURI());
			assertResponseSucceeded(response,true);
			assertNotNull(response.getHTTPETag());
			etagsByObselURI.put(obsel.getURI(), response.getHTTPETag());


			Object message = obsel.getAttributeValue(messageAtt);
			if(message!= null && cnt == 0) {
				modifiedObselURI = obsel.getURI();
				cnt++;
				Map<String, Object> attributes  = new HashMap<String, Object>(obsel.listAttributes());
				attributes.remove(messageAtt);
				attributes.put(messageAtt, newMessageAttValue);
				attributes.put(newAttribute, newAttributeValue);

				Obsel newObsel = KtbsResourceFactory.createObsel(
						obsel.getURI(), 
						obsel.getTraceURI(), 
						newSubject, 
						obsel.getBeginDT(), 
						obsel.getEndDT(), 
						obsel.getBegin(), 
						obsel.getEnd(), 
						obsel.getObselType(), 
						attributes, 
						newLabel
				);
				obsels.add(newObsel);
			} else {
				obsels.add(obsel);
			}
		}

		KtbsResponse putResponse = client.putTraceObsels("http://localhost:8001/base1/t01/@obsels", obsels, etagInfo);
		assertResponseFailed(putResponse);

		putResponse = client.putTraceObsels("http://localhost:8001/base1/t01/@obsels", obsels, etagObsels);
		assertResponseSucceeded(putResponse, false);

		responseGet = client.getTraceObsels("http://localhost:8001/base1/t01/");
		assertResponseSucceeded(responseGet, true);
		String etagObsels2 = responseGet.getHTTPETag();
		assertNotNull(etagObsels2);
		Trace traceObsel2 = (Trace)responseGet.getBodyAsKtbsResource();

		responseGet = client.getTraceInfo("http://localhost:8001/base1/t01/");
		assertResponseSucceeded(responseGet, true);
		String etagInfo2 = responseGet.getHTTPETag();
		assertNotNull(etagInfo2);

		assertTrue(etagObsels2!=etagObsels);

		/*
		 * 		Would sometimes fail when the whole test case is executed due to side effect of creating obsels
		 *  	that are not compliant with the model. 
		 *  	TODO Ktbs bug : the KTBS seems to recaulcate the etag of a trace@obsels after a put, but not 
		 *  	after a POST with a new obsel...
		 */
		//		assertEquals(etagInfo2, etagInfo);

		for(Obsel o:traceObsel2.listObsels()) {
			String uri = o.getURI();
			KtbsResponse response = client.getObsel(uri);
			assertResponseSucceeded(response, true);
			String etag = response.getHTTPETag();

			assertNotNull(etag);
			/*
			 * TODO Ktbs Bug : the KTBS assigns the same etag to all the obsels of a same trace.
			 */
			assertEquals(etagObsels2, etag);
			//			String oldEtag = etagsByObselURI.get(uri);
			//			if(uri.equals(modifiedObselURI))
			//				assertTrue(etag!=oldEtag);
			//			else
			//				assertEquals(oldEtag, etag);

			if(uri.equals(modifiedObselURI)) {
				Obsel obsel = (Obsel)response.getBodyAsKtbsResource();
				assertNotNull(obsel.getAttributeValue(newAttribute));
				assertEquals(newAttributeValue,obsel.getAttributeValue(newAttribute));
				assertNotNull(obsel.getAttributeValue(messageAtt));
				assertEquals(newMessageAttValue,obsel.getAttributeValue(messageAtt));
				assertNotNull(obsel.getLabel());
				assertEquals(newLabel,obsel.getLabel());
				assertNotNull(obsel.getSubject());
				assertEquals(newSubject,obsel.getSubject());
			}
		}
	}

	@Test
	public void testCreateAndPutKtbsResourceStringReader() {
		try {
			FileReader reader;
			File file;
			KtbsResponse response;

			// gets the initial state of the root before creating new resources
			response = client.getKtbsRoot();
			assertResponseSucceeded(response, true);
			KtbsRoot root = (KtbsRoot)response.getBodyAsKtbsResource();
			int initialBaseNumber = root.getBaseURIs().size();

			// create ma-base
			file = new File("turtle/ma-base.ttl");
			assertTrue(file.exists());
			reader = new FileReader(file);
			response = client.createKtbsResource("http://localhost:8001/", reader);
			assertResponseSucceeded(response, false);
			assertEquals("http://localhost:8001/ma-base/", new URI(response.getHTTPLocation()).normalize().toString());

			response = client.getKtbsRoot();
			assertResponseSucceeded(response, true);
			assertEquals(initialBaseNumber+1,((KtbsRoot)response.getBodyAsKtbsResource()).getBaseURIs().size());
			assertTrue(((KtbsRoot)response.getBodyAsKtbsResource()).getBaseURIs().contains("http://localhost:8001/base1/"));
			assertTrue(((KtbsRoot)response.getBodyAsKtbsResource()).getBaseURIs().contains("http://localhost:8001/ma-base/"));
			response = client.getBase("ma-base");
			assertResponseSucceeded(response, true);
			assertEquals("Ma base n°2 que j'ai créée",response.getBodyAsKtbsResource().getLabel());


			// create model2
			file = new File("turtle/model2.ttl");
			assertTrue(file.exists());
			reader = new FileReader(file);
			response = client.createKtbsResource("http://localhost:8001/ma-base/", reader);
			assertResponseSucceeded(response, false);
			assertEquals("http://localhost:8001/ma-base/model2/", new URI(response.getHTTPLocation()).normalize().toString());

			response = client.getBase("ma-base");
			assertResponseSucceeded(response, true);
			assertEquals(1,((Base)response.getBodyAsKtbsResource()).getTraceModelURIs().size());
			assertTrue(((Base)response.getBodyAsKtbsResource()).getTraceModelURIs().contains("http://localhost:8001/ma-base/model2/"));

			assertResponseSucceeded(response, true);
			// populate model2, needs the etag
			response = client.getETag("http://localhost:8001/ma-base/model2/");
			assertResponseSucceeded(response, false);
			assertNotNull(response.getHTTPETag());
			String etag = response.getHTTPETag();
			
			file = new File("turtle/populate_model2.ttl");
			assertTrue(file.exists());
			reader = new FileReader(file);
			response = client.putKtbsResource("http://localhost:8001/ma-base/model2/", reader, etag);
			assertResponseSucceeded(response, false);

			// create trace2
			file = new File("turtle/trace2.ttl");
			assertTrue(file.exists());
			reader = new FileReader(file);
			response = client.createKtbsResource("http://localhost:8001/ma-base/", reader);
			assertResponseSucceeded(response, false);

			response = client.getBase("ma-base");
			assertResponseSucceeded(response, true);
			assertEquals(1,((Base)response.getBodyAsKtbsResource()).getTraceURIs().size());
			assertTrue(((Base)response.getBodyAsKtbsResource()).getTraceURIs().contains("http://localhost:8001/ma-base/trace2/"));
			response = client.getTraceInfo("ma-base", "trace2");
			assertResponseSucceeded(response, true);
			assertEquals("La trace de la base n°2",response.getBodyAsKtbsResource().getLabel());

			// populate trace2
			// observe2
			file = new File("turtle/observe2.ttl");
			assertTrue(file.exists());
			reader = new FileReader(file);
			response = client.createKtbsResource("http://localhost:8001/ma-base/trace2/", reader);
			assertResponseSucceeded(response, false);
			assertEquals("http://localhost:8001/ma-base/trace2/aliment2", new URI(response.getHTTPLocation()).normalize().toString());

			response = client.getTraceObsels("http://localhost:8001/ma-base/trace2/");
			assertResponseSucceeded(response, true);
			Trace traceObsels = (Trace) response.getBodyAsKtbsResource();
			response = client.getTraceInfo("http://localhost:8001/ma-base/trace2/");
			assertResponseSucceeded(response, true);
			Trace traceInfo = (Trace) response.getBodyAsKtbsResource();
			assertEquals(1, traceObsels.listObsels().size());
			response = client.getObsel("http://localhost:8001/ma-base/trace2/aliment2");
			assertResponseSucceeded(response, true);
			Obsel obsel = (Obsel) response.getBodyAsKtbsResource();
			assertEquals("http://localhost:8001/ma-base/model2/Aliment", obsel.getObselType());
			assertEquals("Les tomates du marché", obsel.getLabel());
			assertEquals("Damien", obsel.getSubject());
			assertEquals("http://localhost:8001/ma-base/trace2/", obsel.getTraceURI());
			assertEquals(2, obsel.listAttributes().size());
			assertNotNull(obsel.getAttributeValue("http://localhost:8001/ma-base/model2/nom"));
			assertEquals("Tomates",obsel.getAttributeValue("http://localhost:8001/ma-base/model2/nom"));
			assertNotNull(obsel.getAttributeValue("http://localhost:8001/ma-base/model2/type"));
			assertEquals("FRUIT",obsel.getAttributeValue("http://localhost:8001/ma-base/model2/type"));
			assertEquals(1000, obsel.getBegin());
			assertEquals(25000, obsel.getEnd());
			Date origin = traceInfo.getOrigin();
			assertEquals(new Date(origin.getTime()+1000), obsel.getBeginDT());
			assertEquals(new Date(origin.getTime()+25000), obsel.getEndDT());
			assertEquals(0, obsel.listIncomingRelations().size());
			assertEquals(0, obsel.listOutgoingRelations().size());


			// observe3
			file = new File("turtle/observe3.ttl");
			assertTrue(file.exists());
			reader = new FileReader(file);
			response = client.createKtbsResource("http://localhost:8001/ma-base/trace2/", reader);
			assertResponseSucceeded(response, false);
			assertEquals("http://localhost:8001/ma-base/trace2/aliment1", new URI(response.getHTTPLocation()).normalize().toString());

			response = client.getTraceObsels("http://localhost:8001/ma-base/trace2/");
			assertResponseSucceeded(response, true);
			traceObsels = (Trace) response.getBodyAsKtbsResource();
			assertEquals(2, traceObsels.listObsels().size());
			response = client.getObsel("http://localhost:8001/ma-base/trace2/aliment1");
			assertResponseSucceeded(response, true);
			obsel = (Obsel) response.getBodyAsKtbsResource();
			assertEquals("http://localhost:8001/ma-base/model2/Aliment", obsel.getObselType());
			assertEquals("Saint-Nectaire", obsel.getLabel());
			assertEquals("Damien", obsel.getSubject());
			assertEquals("http://localhost:8001/ma-base/trace2/", obsel.getTraceURI());
			assertEquals(2, obsel.listAttributes().size());
			assertNotNull(obsel.getAttributeValue("http://localhost:8001/ma-base/model2/nom"));
			assertEquals("Saint-Nectaire",obsel.getAttributeValue("http://localhost:8001/ma-base/model2/nom"));
			assertNotNull(obsel.getAttributeValue("http://localhost:8001/ma-base/model2/type"));
			assertEquals("PRODUIT LAITIER",obsel.getAttributeValue("http://localhost:8001/ma-base/model2/type"));
			assertEquals(30000, obsel.getBegin());
			assertEquals(90000, obsel.getEnd());
			assertEquals(new Date(origin.getTime()+30000), obsel.getBeginDT());
			assertEquals(new Date(origin.getTime()+90000), obsel.getEndDT());
			assertEquals(0, obsel.listIncomingRelations().size());
			assertEquals(0, obsel.listOutgoingRelations().size());


			// observe4
			file = new File("turtle/observe4.ttl");
			assertTrue(file.exists());
			reader = new FileReader(file);
			response = client.createKtbsResource("http://localhost:8001/ma-base/trace2/", reader);
			assertResponseSucceeded(response, false);
			String uri = new URI(response.getHTTPLocation()).normalize().toString();

			response = client.getTraceObsels("http://localhost:8001/ma-base/trace2/");
			assertResponseSucceeded(response, true);
			traceObsels = (Trace) response.getBodyAsKtbsResource();
			assertEquals(3, traceObsels.listObsels().size());
			response = client.getObsel(uri);
			assertResponseSucceeded(response, true);
			obsel = (Obsel) response.getBodyAsKtbsResource();
			assertEquals("http://localhost:8001/ma-base/model2/Manger", obsel.getObselType());
			assertEquals("Je mange", obsel.getLabel());
			assertEquals("Damien", obsel.getSubject());
			assertEquals("http://localhost:8001/ma-base/trace2/", obsel.getTraceURI());
			assertEquals(1, obsel.listAttributes().size());
			assertNotNull(obsel.getAttributeValue("http://localhost:8001/ma-base/model2/vitesse"));
			assertEquals("très rapidement",obsel.getAttributeValue("http://localhost:8001/ma-base/model2/vitesse"));
			assertEquals(1000, obsel.getBegin());
			assertEquals(100000, obsel.getEnd());
			assertEquals(new Date(origin.getTime()+1000), obsel.getBeginDT());
			assertEquals(new Date(origin.getTime()+100000), obsel.getEndDT());
			assertEquals(0, obsel.listIncomingRelations().size());
			assertEquals(2, obsel.listOutgoingRelations().size());

			Collection<String> expectedRelationTargets = new LinkedList<String>();
			expectedRelationTargets.add("http://localhost:8001/ma-base/trace2/aliment1");
			expectedRelationTargets.add("http://localhost:8001/ma-base/trace2/aliment2");
			for(RelationStatement relation:obsel.listOutgoingRelations()) {
				assertNull(relation.getToObsel());
				assertNull(relation.getFromObsel());
				assertNotNull(relation.getToObselURI());
				assertNotNull(relation.getFromObselURI());
				assertEquals("http://localhost:8001/ma-base/model2/plat", relation.getRelationName());
				assertEquals(uri, relation.getFromObselURI());
				expectedRelationTargets.remove(relation.getToObselURI());
			}

			assertEquals(0, expectedRelationTargets.size());



			response = client.getTraceInfo("http://localhost:8001/ma-base/trace2/");
			assertResponseSucceeded(response, true);
			traceObsels = (Trace)response.getBodyAsKtbsResource();
			assertTrue(traceObsels.isCompliantWithModel());
			assertEquals("http://localhost:8001/ma-base/model2/",traceObsels.getTraceModelUri());

		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (URISyntaxException e) {
			fail(e.getMessage());
		}


	}

	//	@Test
	public void testCreateKtbsResourceStringInputStream() {
		fail("Not yet implemented");
	}

}
