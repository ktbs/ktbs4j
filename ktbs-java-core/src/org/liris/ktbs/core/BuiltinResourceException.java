package org.liris.ktbs.core;

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
