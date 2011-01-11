package org.liris.ktbs.rdf.resource.test;

import java.io.FileInputStream;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;
import org.liris.ktbs.rdf.KtbsJenaResourceHolderImpl;

import com.ibm.icu.text.SimpleDateFormat;

public class AbstractKtbsJenaTestCase extends TestCase {

	protected KtbsJenaResourceHolder holder;
	protected EmptyResourceFactory emptyFac = EmptyResourceFactory.getInstance();
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	
	@Before
	protected void setUp() throws Exception {
		holder = new KtbsJenaResourceHolderImpl();
		
		loadInHolder(
				"base1/model1/", 
				"model1.ttl", 
				TraceModel.class);
	}
	
	protected <T extends KtbsResource> T loadInHolder(String relativeURI, String fileLocalName, Class<T> clazz) {
		T t;
		try {
			FileInputStream stream = new FileInputStream("turtle/" + fileLocalName);
			t = holder.loadResourceFromStream(
					"http://localhost:8001/" + relativeURI, 
					stream, 
					JenaConstants.JENA_SYNTAX_TURTLE, 
					clazz);
			stream.close();
			return t;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
			return null;
		}
	}
}
