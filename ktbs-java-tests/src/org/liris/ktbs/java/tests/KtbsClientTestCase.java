package org.liris.ktbs.java.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
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
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

public class KtbsClientTestCase {
	private static final String ktbsLocalPath = "bin"+File.separator+"ktbs-full";
	private static final String KTBS_PROPERTIES_FILE = "ktbs-tests.properties";
	private static final String PROP_ROOT_URI = "ktbs.root.uri";
	private static final String PROP_KTBS_INSTALL_PATH = "ktbs.install.path";

	// TODO to be modified after chechking on the Internet for the time zone.
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

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

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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

	//	@Test
	public void testAddObselsToTrace() {
		fail("Not yet implemented");
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
		assertEquals(trace.getObsels().size(),0);
		assertEquals(trace.getObselURIs().size(),0);
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
		assertTrue(trace.getObsels().size()==trace.getObselURIs().size());
		assertTrue(trace.getObsels().size()>=4);
		for(Obsel obsel:trace.getObsels()) {
			assertTrue(obselTypes.contains(obsel.getTypeURI()));
			assertEquals(rootURI+"base1/t01/", obsel.getTraceURI());
			assertNotNull(obsel.getBegin());
			assertNotNull(obsel.getBeginDT());
			assertNotNull(obsel.getEnd());
			assertNotNull(obsel.getEndDT());
			if(!obsel.getTypeURI().equals("http://localhost:8001/base1/model1/OpenChat")) {
				// Checks the relation onChannel
				assertEquals(1,obsel.getOutgoingRelations().size());
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
		assertEquals(0,trace.getObsels().size());
		assertEquals(0,trace.getObselURIs().size());
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

	//	@Test
	public void testDeleteBase() {
		fail("Not yet implemented");
	}

	//	@Test
	public void testDeleteBaseFromURI() {
		fail("Not yet implemented");
	}

	//	@Test
	public void testDeleteTraceStringString() {
		fail("Not yet implemented");
	}

	//	@Test
	public void testDeleteTraceString() {
		fail("Not yet implemented");
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
		for(String obselURI:trace.getObselURIs()) {
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
			for(Relation relation:obsel.getOutgoingRelations()){
				assertNotNull(relation.getToObselURI());
				assertNull(relation.getToObsel());
			}
			for(Relation relation:obsel.getIncomingRelations()) {
				assertNotNull(relation.getFromObselURI());
				assertNull(relation.getFromObsel());
			}
		}
		for(String obselURI:trace.getObselURIs()) {
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

		Map<String, Serializable> attributes = new HashMap<String, Serializable>();
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
		assertEquals(1, obsel.getAttributes().size());
		assertEquals(obsel.getTypeURI(),typeURI);
		assertEquals(obsel.getIncomingRelations().size(),0);
		assertEquals(obsel.getOutgoingRelations().size(),0);
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
		assertEquals(traceObsels1.getObselURIs().size()+1, trace2obsels.getObselURIs().size());
		assertEquals(traceObsels1.getObsels().size()+1, trace2obsels.getObsels().size());
		assertTrue(trace2obsels.getObselURIs().contains(obsel1URI));
		assertFalse(traceObsels1.getObselURIs().contains(obsel1URI));
		assertTrue(trace2obsels.getObselURIs().containsAll(traceObsels1.getObselURIs()));
		
		
		while(client.getObsel("base1","t01", obselName).executedWithSuccess()){obselName="obsel"+nb++;}

		Map<String, Serializable> attributes2 = new HashMap<String, Serializable>();
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
		
		assertEquals(1,obsel2.getOutgoingRelations().size());
		assertEquals(0,obsel2.getIncomingRelations().size());
		assertEquals(0,obsel1.getOutgoingRelations().size());
		/*
		 * There is no simple way to retrieve incoming relations from the KTBS Server.
		 */
		assertEquals(0,obsel1.getIncomingRelations().size());

		
		Relation relation = obsel2.getOutgoingRelations().iterator().next();
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

		attributes = new HashMap<String, Serializable>();
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
		assertEquals(1, obsel.getAttributes().size());
		assertEquals(obsel.getTypeURI(),typeURI);
		assertEquals(obsel.getIncomingRelations().size(),0);
		assertEquals(obsel.getOutgoingRelations().size(),0);
		
		
		assertNotNull(obsel.getBeginDT());
		assertEquals(start.getTime().getTime(), obsel.getBeginDT().getTime());
		
		assertNotNull(obsel.getEndDT());
		assertEquals(relativeStart,obsel.getBegin());
		
		assertEquals(relativeEnd,obsel.getEnd());
		assertEquals(end.getTime().getTime(), obsel.getEndDT().getTime());
		
		assertEquals(attributeValue,obsel.getAttributeValue(attributeName));
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

	//	@Test
	public void testPutTraceObselsTraceString() {
		fail("Not yet implemented");
	}

	//	@Test
	public void testPutTraceObselsStringCollectionOfObselString() {
		fail("Not yet implemented");
	}

	//	@Test
	public void testPutTraceInfo() {
		fail("Not yet implemented");
	}

	//	@Test
	public void testCreateKtbsResourceStringReader() {
		fail("Not yet implemented");
	}

	//	@Test
	public void testCreateKtbsResourceStringInputStream() {
		fail("Not yet implemented");
	}

	//	@Test
	public void testCreateKtbsResourceStringFile() {
		fail("Not yet implemented");
	}

}
