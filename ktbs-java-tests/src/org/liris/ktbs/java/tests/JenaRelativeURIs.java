package org.liris.ktbs.java.tests;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;

public class JenaRelativeURIs {
	
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		model.add(
				model.getResource("http://mydomain/a/b/c/"),
				model.getProperty("http://mydomain/a/b/d/"),
				model.getResource("http://mydomain/a/b/e/")
		);
		model.add(
				model.getResource("http://mydomain/a/b/c/"),
				model.getProperty("http://mydomain/a/b/d/"),
				model.getResource("http://mydomain/a/b/g/")
		);
	
		RDFWriter writer = model.getWriter("RDF/XML");
		writer.setProperty("relativeURIs", "parent");
		writer.write(model, System.out, "http://mydomain/a/b/c/");
	}
}
