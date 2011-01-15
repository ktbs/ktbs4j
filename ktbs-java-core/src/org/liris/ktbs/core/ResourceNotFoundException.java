package org.liris.ktbs.core;

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
