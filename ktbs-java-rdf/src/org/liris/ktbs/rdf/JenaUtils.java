package org.liris.ktbs.rdf;

import java.io.StringWriter;

import org.liris.ktbs.core.JenaConstants;

import com.hp.hpl.jena.rdf.model.Model;

public class JenaUtils {
	
	public static String toTurtleString(Model rdfModel) {
		StringWriter writer = new StringWriter();
		rdfModel.write(writer, JenaConstants.JENA_SYNTAX_TURTLE, "");
		return writer.toString();
	}

}
