package org.liris.ktbs.core;

/**
 * An exception thrown whenever a KTBS resource referenced by it URI is 
 * required by a process and could not be found in the 
 * {@link ResourceRepository} at hand.
 * 
 * @author Damien Cram
 * @see ResourceRepository
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String resourceURI;

	public ResourceNotFoundException(String resourceURI) {
		super();
		this.resourceURI = resourceURI;
	}
	
	@Override
	public String getMessage() {
		return "Could not find the resource \""+resourceURI+"\".";
	}

}
