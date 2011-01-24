package org.liris.ktbs.core;

import org.liris.ktbs.core.api.ObselType;

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

