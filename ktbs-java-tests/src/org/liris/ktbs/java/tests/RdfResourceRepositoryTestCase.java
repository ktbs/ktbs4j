package org.liris.ktbs.java.tests;

import java.io.FileInputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.resource.RdfResourceRepository;
import org.liris.ktbs.utils.KtbsUtils;

import com.ibm.icu.text.SimpleDateFormat;

public class RdfResourceRepositoryTestCase extends TestCase {

	protected ResourceRepository repository;
	protected EmptyResourceFactory emptyFac = EmptyResourceFactory.getInstance();
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	
	protected static final String base1 = "http://localhost:8001/base1/";
	protected static final String root = "http://localhost:8001/";
	protected static final String t01 = "http://localhost:8001/base1/t01/";
	protected static final String count1 = "http://localhost:8001/base1/count1/";
	protected static final String count2 = "http://localhost:8001/base1/count2/";
	protected static final String filtered1 = "http://localhost:8001/base1/filtered1/";
	protected static final String filtered2 = "http://localhost:8001/base1/filtered2/";
	protected static final String helloworld1 = "http://localhost:8001/base1/helloworld1/";
	protected static final String helloworldUri = "http://localhost:8001/base1/helloworld/";
	protected static final String model1 = "http://localhost:8001/base1/model1/";
	
	protected String fusioned1uri = "http://localhost:8001/base1/fusioned1/";
	protected String filtered1uri = "http://localhost:8001/base1/filtered1/";
	protected String count1uri = "http://localhost:8001/base1/count1/";
	protected String countUri = "http://localhost:8001/base1/count/";
	
	@Before
	protected void setUp() throws Exception {
		repository = new RdfResourceRepository();
		
		FileInputStream stream = new FileInputStream("turtle/model1.ttl");
	
		
		Base base1 = repository.createBase("http://localhost:8001/base1/");
		
		
		repository.loadResource(
				stream, 
				JenaConstants.TURTLE);
				
		stream.close();
		repository.createStoredTrace(base1.getURI(), "http://localhost:8001/base1/t01/", "http://localhost:8001/base1/model1/", "Nestor");

		stream = new FileInputStream("turtle/traceobsels.ttl");
		repository.loadResource(
				stream, 
				JenaConstants.TURTLE);
		stream.close();
		
		stream = new FileInputStream("turtle/traceabout.ttl");
		repository.loadResource(
				stream, 
				JenaConstants.TURTLE);
		stream.close();
		
		Trace trace1 = (Trace)repository.getResource(t01);
		
		for(Obsel o:KtbsUtils.toIterable(trace1.listObsels())) {
			System.out.println(o.getURI());
		}
	}
	
	
	@Test
	public void testLoadTrace() {
		// Load a trace about from a stream
		// then load obsels
		// TODO to be implemented properly
		
	}
}
