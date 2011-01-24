package org.liris.ktbs.core;

/**
 * An exception that is thrown when deserializing a KTBS resource from a stream
 * failed due to too many different resources defined in the same stream.
 * 
 * @author Damien Cram
 *
 */
@SuppressWarnings("serial")
public class MultipleResourcesInStreamException extends ResourceLoadException {
	
	private String rdfType;
	private int expectedNumber;
	private int actualNumber;

	public MultipleResourcesInStreamException(String model, String rdfType,
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
