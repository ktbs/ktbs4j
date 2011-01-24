package org.liris.ktbs.core;

import org.liris.ktbs.core.api.ObselType;

/**
 * An exception that is thrown when the domain of an attribute 
 * type or a relation type is violated.
 * 
 * @author Damien Cram
 * @see RangeException
 */
@SuppressWarnings("serial")
public class DomainException extends InvalidDomainOrRangeException {

	public DomainException(ObselType[] domainOrRange, ObselType actual) {
		super(domainOrRange, actual);
	}

	@Override
	protected boolean getDomain() {
		return true;
	}

	public ObselType[] getExpectedDomain() {
		return domainOrRange;
	}
}
