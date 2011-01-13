package org.liris.ktbs.rdf.resource;

import java.io.StringWriter;

import org.liris.ktbs.core.JenaConstants;

import com.hp.hpl.jena.rdf.model.Model;

@SuppressWarnings("serial")
public class ResourceLoadException extends Exception {
	private String message;
	private Model rdfModel;
	
	public ResourceLoadException(String message, Model rdfModel) {
		super();
		this.message = message;
		this.rdfModel = rdfModel;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public String getRdfModel() {
		StringWriter writer = new StringWriter();
		rdfModel.write(writer, JenaConstants.JENA_SYNTAX_TURTLE, "");
		return writer.toString();
	}
}
