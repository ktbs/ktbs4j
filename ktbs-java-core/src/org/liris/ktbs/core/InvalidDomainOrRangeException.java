package org.liris.ktbs.core;

@SuppressWarnings("serial")
public abstract class InvalidDomainOrRangeException extends RuntimeException {

	protected ObselType domainOrRange;
	private ObselType actual;
	public InvalidDomainOrRangeException(ObselType domainOrRange,
			ObselType actual) {
		super();
		this.domainOrRange = domainOrRange;
		this.actual = actual;
	}
	
	@Override
	public String getMessage() {
		return "Type \""+actual.getURI()+"\" is no subtype of the expected "+(getDomain()?"domain":"range")+": " +domainOrRange.getURI()+".";
	}
	
	public ObselType getActual() {
		return actual;
	}
	
	protected abstract boolean getDomain();
	
}
