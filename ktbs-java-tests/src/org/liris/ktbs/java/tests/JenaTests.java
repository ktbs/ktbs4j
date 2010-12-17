package org.liris.ktbs.java.tests;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class JenaTests {
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		
		Resource res1 = model.createResource("resource1");
		Resource res2 = model.createResource("resource2");
		Property prop1 = model.createProperty("maProperty");

		Resource res3 = model.createResource("resource3");
		Resource res4 = model.createResource("resource4");
		Property prop2 = model.createProperty("maProperty");
		
		model.add(res1, prop1, res2);
		model.add(res3, prop2, res4);
		
		model.write(System.out, "N-TRIPLE");
	}
}
