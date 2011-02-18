package org.liris.ktbs.core.impl;

import java.util.Iterator;

import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.share.WithDomainResource;

public class AttributeTypeImpl extends ResourceImpl implements AttributeType {

	AttributeTypeImpl(String uri) {
		super(uri);
	}
	
	/*
	 * The delegate for range management
	 */
	private WithRangeString withRangeDelegate = new WithRangeString();

	/*
	 * The delegate for domain management
	 */
	private WithDomainResource<ObselType> withDomainDelegate = new WithDomainResourceImpl<ObselType>(manager);
	
	@Override
	public void addRange(String range) {
		withRangeDelegate.addRange(range);
	}

	@Override
	public Iterator<String> listRanges() {
		return withRangeDelegate.listRanges();
	}

	@Override
	public String getRange() {
		return withRangeDelegate.getRange();
	}

	@Override
	public void addDomain(ObselType domain) {
		withDomainDelegate.addDomain(domain);
	}

	@Override
	public Iterator<ObselType> listDomains() {
		return withDomainDelegate.listDomains();
	}

	@Override
	public ObselType getDomain() {
		return withDomainDelegate.getDomain();
	}
}
