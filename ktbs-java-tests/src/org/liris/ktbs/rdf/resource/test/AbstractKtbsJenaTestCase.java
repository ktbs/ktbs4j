package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.RDFResourceRepository;
import org.liris.ktbs.rdf.resource.RDFResourceRepositoryImpl;
import org.liris.ktbs.rdf.resource.ResourceLoadException;

import com.ibm.icu.text.SimpleDateFormat;

public class AbstractKtbsJenaTestCase extends TestCase {

	protected RDFResourceRepository repository;
	protected EmptyResourceFactory emptyFac = EmptyResourceFactory.getInstance();
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	
	@Before
	protected void setUp() throws Exception {
		repository = new RDFResourceRepositoryImpl();
		
		loadInRepo(
				"model1.ttl", 
				TraceModel.class);
	}
	
	protected <T extends KtbsResource> T loadInRepo(String fileLocalName, Class<T> clazz) {
		T t;
		try {
			FileInputStream stream = new FileInputStream("turtle/" + fileLocalName);
			t = clazz.cast(repository.loadResource(
					stream, 
					JenaConstants.JENA_SYNTAX_TURTLE));
			stream.close();
			return t;
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
