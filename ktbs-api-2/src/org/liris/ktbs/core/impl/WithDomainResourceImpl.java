package org.liris.ktbs.core.impl;

import java.util.Iterator;

import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.WithDomainResource;

class WithDomainResourceImpl<T extends KtbsResource> extends ResourceCollectionDelegate<T> implements WithDomainResource<T> {
	
	WithDomainResourceImpl(ResourceProvider manager) {
		super(manager);
	}

	@Override
	public void addDomain(T domain) {
		add(domain);
	}

	@Override
	public Iterator<T> listDomains() {
		return list();
	}

	@Override
	public T getDomain() {
		return get();
	}
}
