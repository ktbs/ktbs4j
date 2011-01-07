package org.liris.ktbs.rdf;

public class KtbsResourceInstanciationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private Class<?> clazz;
	private String message;

	public KtbsResourceInstanciationException(Class<?> clazz, String message) {
		super();
		this.clazz = clazz;
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message + "["+clazz+"]";
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
}
