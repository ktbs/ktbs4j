package org.liris.ktbs.core;

import org.liris.ktbs.core.api.ObselType;

/**
 * An exception that is thrown when the range of an attribute 
 * type or a relation type is violated.
 * 
 * @author Damien Cram
 * @see RangeException
 */
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

