package org.liris.ktbs.tests;


import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Before;
import org.liris.ktbs.core.KtbsClient;
import org.liris.ktbs.core.ResourceManager;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IRoot;

public class KtbsClientTestCase extends TestCase {

	ResourceManager manager;
	@Before
	public void setUp() throws Exception {
		try {
			manager = KtbsClient.getDefaultRestManager();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testGetRoot() {
		
		System.out.println("Getting the root");
		IRoot root = manager.getKtbsResource("http://localhost:8001/", IRoot.class);
		IBase base1 = root.get("base1");
		Iterator<IKtbsResource> it = base1.listResources();
		while (it.hasNext()) {
			IKtbsResource iKtbsResource = (IKtbsResource) it.next();
			System.out.println("\t" + iKtbsResource.getUri() + " (+"+iKtbsResource.getTypeUri()+"+)");
		}
	}
}
