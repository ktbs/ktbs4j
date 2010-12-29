package org.liris.ktbs.java.tests;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenaTests {
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		model.add(
				model.getResource("http://mydomain/resource1/"),
				model.getProperty("http://mydomain/property1"),
				model.getResource("http://mydomain/resource2/")
		);
		model.getWriter("TURTLE").write(model, System.out, "http://mydomain/");
	}
}
