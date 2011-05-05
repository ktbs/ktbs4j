package org.liris.ktbs.tests;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public class TurtleEscapeSpecialChars {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		Resource r = model.createResource("http://www.example.com/resource1");
		r.addLiteral(model.getProperty("http://www.example.com/prop1"), "Ma valeur 1");
		r.addLiteral(model.getProperty("http://www.example.com/prop2"), 2);
		r.addLiteral(model.getProperty("http://www.example.com/prop3"), "Ma\rvaleur");
		r.addLiteral(model.getProperty("http://www.example.com/prop3"), "Ma\rvaleur");
		
		model.write(System.out, "TURTLE");
	}

}
