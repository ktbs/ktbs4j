package org.liris.ktbs.rdf.resource;

import com.hp.hpl.jena.rdf.model.Model;

@SuppressWarnings("serial")
public class MultipleResourcesInStreamException extends ResourceLoadException {
	
	private String rdfType;
	private int expectedNumber;
	private int actualNumber;

	public MultipleResourcesInStreamException(Model model, String rdfType,
			int expectedNumber, int actualNumber) {
		super("",model);
		this.rdfType = rdfType;
		this.expectedNumber = expectedNumber;
		this.actualNumber = actualNumber;
	}
	
	@Override
	public String getMessage() {
		return "Loading failed. I expected "+expectedNumber+" resource of type \""+rdfType+"\", but there are "+actualNumber+".";
	}

	public int getExpectedNumber() {
		return expectedNumber;
	}
	
	public int getActualNumber() {
		return actualNumber;
	}
	
	public String getRdfType() {
		return rdfType;
	}
}
