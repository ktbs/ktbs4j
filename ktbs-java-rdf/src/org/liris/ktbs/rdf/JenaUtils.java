package org.liris.ktbs.rdf;

import java.io.StringWriter;

import org.liris.ktbs.core.JenaConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class JenaUtils {
	
	public static String toTurtleString(Model rdfModel) {
		StringWriter writer = new StringWriter();
		rdfModel.write(writer, JenaConstants.TURTLE, "");
		return writer.toString();
	}
	

	/**
	 * Creates a new model with only the statement selected by a selector.
	 * 
	 * @param model
	 * @param selector
	 * @return
	 */
	public static Model filterModel(Model model, Selector selector) {
		Model filteredModel = ModelFactory.createDefaultModel();
		
		filteredModel.add(model);

		StmtIterator it = filteredModel.listStatements();
		
		while(it.hasNext()) {
			Statement s = it.next();
			if(!selector.test(s))
				it.remove();
		}

		return filteredModel;
	}

}
