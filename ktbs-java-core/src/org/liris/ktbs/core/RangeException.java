package org.liris.ktbs.core;

@SuppressWarnings("serial")
public class RangeException extends InvalidDomainOrRangeException {

	public RangeException(ObselType[] domainOrRange, ObselType actual) {
		super(domainOrRange, actual);
	}

	@Override
	protected boolean getDomain() {
		return false;
	}

	public ObselType[] getExpectedRange() {
		return domainOrRange;
	}
}

