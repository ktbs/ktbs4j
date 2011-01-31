package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.ResourceLoadException;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.resource.RdfResourceRepository;

import com.ibm.icu.text.SimpleDateFormat;

public class AbstractKtbsRdfResourceTestCase extends TestCase {

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
	
	protected void assertDateEquals(Date date, int y, int month, int day, int h,
			int minute, int s) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		assertEquals(y, c.get(Calendar.YEAR));
		assertEquals(month, c.get(Calendar.MONTH) + 1);
		assertEquals(day, c.get(Calendar.DAY_OF_MONTH));
		assertEquals(h, c.get(Calendar.HOUR_OF_DAY));
		assertEquals(minute, c.get(Calendar.MINUTE));
		assertEquals(s, c.get(Calendar.SECOND));
	}

	
	@Before
	protected void setUp() throws Exception {
		repository = new RdfResourceRepository();
		
		loadInRepo(
				model1,
				"model1.ttl", 
				TraceModel.class);
	}
	
	protected <T extends KtbsResource> T loadInRepo(String uri, String fileLocalName, Class<T> clazz) {
		try {
			FileInputStream stream = new FileInputStream("turtle/" + fileLocalName);
			repository.loadResource(
					stream, 
					JenaConstants.TURTLE);
					
			stream.close();
			return clazz.cast(repository.getResource(uri));
		} catch (ResourceLoadException e) {
			e.printStackTrace(System.err);
			System.out.println(e.getRdfModel());
			System.exit(1);
			return null;
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
			return null;
		}
	}
}
