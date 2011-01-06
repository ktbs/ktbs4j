package org.liris.ktbs.core;

public class KtbsResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String resourceURI;

	public KtbsResourceNotFoundException(String resourceURI) {
		super();
		this.resourceURI = resourceURI;
	}
	
	@Override
	public String getMessage() {
		return "Could not find the resource \""+resourceURI+"\".";
	}

}
