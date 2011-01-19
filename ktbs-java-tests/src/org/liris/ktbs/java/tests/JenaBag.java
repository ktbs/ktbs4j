package org.liris.ktbs.java.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.liris.ktbs.rdf.JenaUtils;

import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class JenaBag {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		Resource toto = model.getResource("http://localhost:8001/toto");
		toto.addLiteral(model.getProperty("http://localhost:8001/hasName"), "Cram");
		Bag seq = model.createBag("http://localhost:8001/names");
		seq.add("Damien");
		seq.add("Guillaume");
		seq.add("Marie");

		Property hasFirstNames = model.getProperty("http://localhost:8001/hasFirstNames");
		toto.addProperty(hasFirstNames, seq);
		printModel(model);

		Model model2 = ModelFactory.createDefaultModel();
		model2.read(new FileInputStream("rdf-collections.ttl"), null, "TURTLE");
		System.out.println("------------------------------------------------");
		printModel(model2);

		Iterable<?> firstNames = (Iterable<?>) JenaUtils.asJavaObject(toto.getProperty(hasFirstNames).getObject());
		for(Object name: firstNames)
			System.out.println("\t" + name);
		
		System.out.println("------------------------------------------------");
		Resource myResource = model2.getResource("http://www.example.com/myResource");
		Resource r = myResource.getPropertyResourceValue(model2.getProperty("http://www.example.com/myCollectionProperty"));

		Object asJavaObject = JenaUtils.asJavaObject(r);
		Iterable<?> iterable = (Iterable<?>) asJavaObject;
		for(Object value:iterable)
			System.out.println("\t" + value);
	}

	private static void printModel(Model model2) {
		StmtIterator it = model2.listStatements();
		while (it.hasNext()) {
			Statement statement = (Statement) it.next();
			System.out.println(statement);
		}
	}
}
