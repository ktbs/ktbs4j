package org.liris.ktbs.service.impl;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException {

	private String uri;
	public ResourceNotFoundException(String uri) {
		this.uri = uri;
	}
	
	public String getUri() {
		return uri;
	}

	@Override
	public String getMessage() {
		return "The resource " + uri + " could not be found.";
	}
}
