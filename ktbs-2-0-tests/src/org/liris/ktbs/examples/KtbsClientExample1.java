package org.liris.ktbs.examples;

import org.liris.ktbs.client.Ktbs;
import org.liris.ktbs.client.KtbsRootClient;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.service.ResourceService;

public class KtbsClientExample1 {

	public static void main(String[] args) {
		KtbsClientExample1 example1 = new KtbsClientExample1();
		example1.run();
	}

	
	public void run() {
		// Get a KTBS client that uses REST to communicate
		// with a KTBS
		// the ktbs root uri is given in ktbs.root.uri
		KtbsRootClient client = Ktbs.getRestRootClient();
		
		// Display the root uri
		System.out.println(client.getRootUri());
	
		System.out.println("------------------");
		
		// retrieve the base1
		ResourceService resourceService = client.getResourceService();
		IBase base1 = resourceService.getBase("base1");
		// The absolute URI would have worked as well
		// resourceService.getBase("http://lcoalhost:8001/base1/") 
		
		// display the stored traces contained in that base
		for(IStoredTrace storedTrace:base1.getStoredTraces()) {
			System.out.println("------------------");
			System.out.println("Name: " + storedTrace.getLocalName());
			System.out.println("Label: " + storedTrace.getLabel());
			System.out.println("Origin: " + storedTrace.getOrigin());
			System.out.println("Default subject: " + storedTrace.getDefaultSubject());
		}
		
		// iterate over all resources contained the base
		for(IKtbsResource resource:base1) {
			System.out.println("------------------");
			System.out.println("Name: " + resource.getLocalName());
			System.out.println("Type: " + resource.getTypeUri());
			if (resource instanceof ITrace) {
				ITrace trace = (ITrace) resource;
				System.out.println(trace.getUri() + " is a trace.");
				System.out.println("Nb obsels: " + trace.getObsels().size());
			}
		}
		
		
		System.out.println("---------------------------");

		// creates a new base
		// the following creates a base remotely
		IBase base2 = resourceService.getBase(resourceService.newBase("base2", "Damien"));
		
		base2.addLabel("Base créée par l'API Java 2.0");
		
		// save the modification
		resourceService.saveResource(base2, false);
		
		IBase base2remote = resourceService.getBase("base2");
		for(String label:base2remote.getLabels()) 
			System.out.println(label);
	}
}
