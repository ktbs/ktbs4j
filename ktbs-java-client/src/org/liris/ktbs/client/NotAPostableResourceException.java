package org.liris.ktbs.client;

import org.liris.ktbs.core.api.KtbsResource;

@SuppressWarnings("serial")
public class NotAPostableResourceException extends RuntimeException {

	private KtbsResource resource;

	public NotAPostableResourceException(KtbsResource resource) {
		super();
		this.resource = resource;
	}
	
	
	@Override
	public String getMessage() {
		return "The resource \""+resource.getURI()+"\" is not a postable resource.";
	}
	
	public KtbsResource getResource() {
		return resource;
	}
	
}
