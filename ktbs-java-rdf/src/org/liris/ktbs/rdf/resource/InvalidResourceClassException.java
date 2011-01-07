package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResource;

public class InvalidResourceClassException extends Exception {
	private static final long serialVersionUID = 1L;

	private KtbsResource resource;

	InvalidResourceClassException(KtbsResource resource) {
		super();
		this.resource = resource;
	}
	
	@Override
	public String getMessage() {
		return "Cannot be modified with a resource of type \""+(resource==null?null:resource.getClass())+"\". KtbsJena* object required.";
	}
}
