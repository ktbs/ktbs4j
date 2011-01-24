package org.liris.ktbs.core;


/**
 * An exception that is thrown when deserializing a KTBS resource from a stream
 * failed.
 * 
 * @author Damien Cram
 *
 */
@SuppressWarnings("serial")
public class ResourceLoadException extends Exception {
	private String message;
	private String rdfModel;
	
	public ResourceLoadException(String message, String rdfModel) {
		super();
		this.message = message;
		this.rdfModel = rdfModel;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public String getRdfModel() {
		return rdfModel;

	}
}
