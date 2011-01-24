package org.liris.ktbs.core;

import org.liris.ktbs.core.api.KtbsResource;

/**
 * An exception that is thrown when the user tries to change the state of
 * a KTBS resource that is not modifiable.
 * 
 * @author Damien Cram
 *
 */
public class ReadOnlyObjectException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private KtbsResource resource;

	public ReadOnlyObjectException(KtbsResource resource) {
		super();
		this.resource = resource;
	}
	
	@Override
	public String getMessage() {
		return "The KTBS resource object \""+resource.getURI()+"\" has a read-only access.";
	}
}
