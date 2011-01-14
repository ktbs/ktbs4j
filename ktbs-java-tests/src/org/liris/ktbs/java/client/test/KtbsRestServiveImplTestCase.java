package org.liris.ktbs.java.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.client.KtbsClientApplication;
import org.liris.ktbs.client.KtbsResponse;
import org.liris.ktbs.client.KtbsResponseStatus;
import org.liris.ktbs.client.KtbsRestService;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.ResourceLoadException;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.resource.RDFResourceRepository;

public class KtbsRestServiveImplTestCase {

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
		repository = new RDFResourceRepository();
		
		repository.loadResource(
				new FileInputStream("turtle/ktbsroot.ttl"), 
				JenaConstants.JENA_SYNTAX_TURTLE);
		
		repository.loadResource(
				new FileInputStream("turtle/empty-base.ttl"), 
				JenaConstants.JENA_SYNTAX_TURTLE
		);

		repository.loadResource(
				new FileInputStream("turtle/empty-base1.ttl"), 
				JenaConstants.JENA_SYNTAX_TURTLE
		);

		repository.loadResource(
				new FileInputStream("turtle/empty-model1.ttl"), 
				JenaConstants.JENA_SYNTAX_TURTLE);

		repository.loadResource(
				new FileInputStream("turtle/t01-obsels-and-info.ttl"), 
				JenaConstants.JENA_SYNTAX_TURTLE);

		repository.loadResource(
				new FileInputStream("turtle/postable-count.ttl"), 
				JenaConstants.JENA_SYNTAX_TURTLE);
	}
	
	
	@Test
	public void testRetrieve() {
		KtbsResponse response = service.retrieve(rootUri);
		assertEquals(KtbsResponseStatus.RESOURCE_RETRIEVED, response.getKtbsStatus());
		assertNotNull(response.getHTTPETag());
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
	
	@Test
	public void testCreate() {
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, service.create(repository.getResource(emptyBaseUri, Base.class)).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, service.create(repository.getResource(base1Uri, Base.class)).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, service.create(repository.getResource(model1Uri, TraceModel.class)).getKtbsStatus());
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, service.create(repository.getResource(countUri, Method.class)).getKtbsStatus());
//		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, service.create(repository.getResource(t01Uri, StoredTrace.class)).getKtbsStatus());
	}

	@Test
	public void testUpdate() throws FileNotFoundException {
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, service.create(repository.getResource(base1Uri, Base.class)).getKtbsStatus());
		TraceModel tm = repository.getResource(model1Uri, TraceModel.class);
		KtbsResponse response = service.create(tm);
		assertEquals(KtbsResponseStatus.RESOURCE_CREATED, response.getKtbsStatus());
		response = service.retrieve(model1Uri);
		assertEquals(true, response.executedWithSuccess());
		assertNotNull(response.getHTTPETag());
		String etag = response.getHTTPETag();
		try {
			repository.loadResource(new FileInputStream("turtle/gra_model1.ttl"), JenaConstants.JENA_SYNTAX_TURTLE);
		} catch (ResourceLoadException e) {
			fail("Should not fail");
		}
		tm = repository.getResource(model1Uri, TraceModel.class);
		response = service.update(tm, etag);
	}

	
	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
