package org.liris.ktbs.core;

@SuppressWarnings("serial")
public class DomainException extends InvalidDomainOrRangeException {

	public DomainException(ObselType domainOrRange, ObselType actual) {
		super(domainOrRange, actual);
	}

	@Override
	protected boolean getDomain() {
		return true;
	}

	public ObselType getExpectedDomain() {
		return domainOrRange;
	}
}
