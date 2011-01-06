package org.liris.ktbs.core;


public class TemporalDomainException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String dateString;

	public TemporalDomainException(String dateString) {
		super();
		this.dateString = dateString;
	}
	
	public TemporalDomainException(String string,
			Throwable e) {
		super(e);
		this.dateString = string;
	}

	public String getDateString() {
		return dateString;
	}
	
	@Override
	public String getMessage() {
		return "The temporal domain value \""+dateString+"\" cannot be interpreted in the requested format.";
	}
}
