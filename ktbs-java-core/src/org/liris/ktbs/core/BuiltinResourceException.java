package org.liris.ktbs.core;

/**
 * An exception thrown when the user accesses to a method that is 
 * not relevant in the context of a built-in resource.
 * 
 * @author Damien Cram
 * @see BuiltinMethod
 */
@SuppressWarnings("serial")
public class BuiltinResourceException extends RuntimeException {
	
	private String message;

	public BuiltinResourceException(String message) {
		super();
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
