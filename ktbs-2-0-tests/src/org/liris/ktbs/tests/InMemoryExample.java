package org.liris.ktbs.tests;

import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.service.ResourceService;

public class InMemoryExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceService manager = Ktbs.getMemoryRootClient().getResourceService();
		
		IRoot root = manager.getResource("http://localhost:8001/", IRoot.class);

		IBase base1 = manager.newBase(root.getUri(), "basetoto", "Toto");
		IBase base2 = manager.newBase(root.getUri(), "basetoto2", "Toto2");
		IBase base3 = manager.newBase(root.getUri(), "basetoto3", "Toto3");
	}
}
