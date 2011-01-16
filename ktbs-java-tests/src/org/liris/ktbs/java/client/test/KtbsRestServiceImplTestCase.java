package org.liris.ktbs.java.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.client.KtbsClientApplication;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.client.KtbsResponseStatus;
import org.liris.ktbs.client.KtbsRestService;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ResourceLoadException;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.resource.RdfResourceRepository;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsRestServiceImplTestCase {

	private ResourceRepository repository;
	private KtbsRestService service;
	
	private String rootUri = "http://localhost:8001/";
	private String emptyBaseUri = "http://localhost:8001/empty-base/";
	private String base1Uri = "http://localhost:8001/base1/";
	private String t01Uri = "http://localhost:8001/base1/t01/";
	private String t01ObselsUri = "http://localhost:8001/base1/t01/@obsels";
	private String t01InfoUri = "http://localhost:8001/base1/t01/@about";
	private String obsel1Uri = "http://localhost:8001/base1/t01/obs1";
	private String model1Uri = "http://localhost:8001/base1/model1/";
	private String countUri = "http://localhost:8001/base1/count/";
	private String count1Uri = "http://localhost:8001/base1/count1/";
	private String count2Uri = "http://localhost:8001/base1/count2/";
	private String filtered1Uri = "http://localhost:8001/base1/filtered1/";
	private String filtered2Uri = "http://localhost:8001/base1/filtered2/";
	private String fusioned1Uri = "http://localhost:8001/base1/fusioned1/";
	private String helloWorldUri = "http://localhost:8001/base1/helloworld/";
	private String helloWorld1Uri = "http://localhost:8001/base1/helloworld1/";
	
	@Before
	public void setUp() throws Exception {
		service = KtbsClientApplication.getInstance().getRestService("http://localhost:8001/");
		repository = new RdfResourceRepository();
	}
	
	
	@Test
	public void testRetrieve() {
		KtbsResponse response = service.retrieve(rootUri);
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, response.getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(base1Uri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(t01ObselsUri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(t01InfoUri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(obsel1Uri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(count1Uri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(count2Uri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(countUri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(filtered1Uri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(filtered2Uri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(fusioned1Uri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(helloWorld1Uri).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, service.retrieve(helloWorldUri).getKtbsStatus());
	}
	
	private KtbsResource retrieveAsKtbsResource(String uri) {
		KtbsResponse retrieve = service.retrieve(uri);
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, retrieve.getKtbsStatus());
		try {
			repository.loadResource(retrieve.getBody(), KtbsUtils.getJenaSyntax(retrieve.getMimeType()));
			return repository.getResource(uri);
		} catch(ResourceLoadException e) {
			fail(e.getMessage());
			return null;
		}
	}
	
	
	@Test
	public void testCreateObsel() {
		
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("http://localhost:8001/base1/model1/message", "Salut tout le monde");
		
		KtbsResponse response = service.createObsel(
				"http://localhost:8001/base1/t01/", 
				"http://localhost:8001/base1/t01/obs1", 
				"http://localhost:8001/base1/model/SendMsg",
				"Damien",
				"2010-04-28T18:09:01+00:00",
				null,
				null,
				new BigInteger("1000"),
				attributes
				);
		
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		assertEquals("http://localhost:8001/base1/t01/obs1", response.getHTTPLocation());
	}
	
	
	@Test
	public void testCreate() throws FileNotFoundException, IOException {
		KtbsResponse response = service.createBase("http://localhost:8001/", "http://localhost:8001/base2/");
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		assertEquals("http://localhost:8001/base2/", response.getHTTPLocation());
		
		
		response = service.createTraceModel("http://localhost:8001/base2/", "http://localhost:8001/base2/model2/");
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		assertEquals("http://localhost:8001/base2/model2/", response.getHTTPLocation());

		response = service.createTraceModel("http://localhost:8001/base2/", "http://localhost:8001/base2/model3/");
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		assertEquals("http://localhost:8001/base2/model3/", response.getHTTPLocation());

		
		response = service.createStoredTrace("http://localhost:8001/base2/", "http://localhost:8001/base2/trace2/", "http://localhost:8001/base2/model2/", "Le premier de l'an");
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		assertEquals("http://localhost:8001/base2/trace2/", response.getHTTPLocation());
		
		response = service.createStoredTrace("http://localhost:8001/base2/", "http://localhost:8001/base2/trace3/", "http://localhost:8001/base2/model2/", "Le premier de l'an");
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		assertEquals("http://localhost:8001/base2/trace3/", response.getHTTPLocation());
		
		Map<String, String> parameters = new HashMap<String, String>();
		String script=IOUtils.toString(new FileInputStream("python/python1.py"), "UTF-8");
		parameters.put("script", script);
		response = service.createMethod(
				"http://localhost:8001/base2/", 
				"http://localhost:8001/base2/method2/", 
				KtbsConstants.SCRIPT_PYTHON,
				parameters);
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		assertEquals("http://localhost:8001/base2/method2/", response.getHTTPLocation());

		response = service.createComputedTrace(
				"http://localhost:8001/base2/", 
				"http://localhost:8001/base2/comp-trace1/", 
				"http://localhost:8001/base2/method2/", 
				Arrays.asList(new String[]{"http://localhost:8001/base2/trace2/","http://localhost:8001/base2/trace3/"}));
		
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		assertEquals("http://localhost:8001/base2/comp-trace1/", response.getHTTPLocation());
	}
	
	
	@Test
	public void testUpdate() throws FileNotFoundException, ResourceLoadException {
		/*
		 * Base empty before starting the test
		 */
		
		repository.loadResource(new FileInputStream("turtle/gra_model1.ttl"), JenaConstants.TURTLE);
		TraceModel tm = repository.getResource(model1Uri, TraceModel.class);
		
		KtbsResponse response = service.createBase(rootUri, base1Uri);
		assertCreateSucceeded(response, base1Uri);
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		
		response = service.createTraceModel(base1Uri, model1Uri);
		assertCreateSucceeded(response, model1Uri);
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());

		response = service.update(tm, getETag(tm));
		assertEquals(KtbsResponseStatus.RESOURCE_UPDATED, response.getKtbsStatus());
		
		
		
//		response = service.retrieve(base1Uri);
//		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, response.getKtbsStatus());
//		
//		repository.loadResource(response.getBody(), JenaConstants.TURTLE);
//		Base b = (Base) repository.getResource(base1Uri);
//		assertEquals(base1Uri, b.getURI());
//		b.setLabel("Toto tutu");
//		response = service.update(b, getETag(b));
//		assertEquals(KtbsResponseStatus.RESOURCE_UPDATED, response.getKtbsStatus());
		
	}

	
	private String getETag(KtbsResource resource) {
		KtbsResponse retrieve = service.retrieve(resource.getURI());
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, retrieve.getKtbsStatus());
		String etag = retrieve.getHTTPETag();
		assertNotNull(etag);
		return etag;
	}
	
	private void assertCreateSucceeded(KtbsResponse request, String location) {
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, request.getKtbsStatus());
		assertEquals(location, request.getHTTPLocation());
	}


	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
