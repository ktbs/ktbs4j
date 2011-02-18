package org.liris.ktbs.core.impl;

import java.util.Iterator;

import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.WithRangeResource;

class WithRangeResourceImpl<T extends KtbsResource> extends ResourceCollectionDelegate<T> implements WithRangeResource<T> {

	WithRangeResourceImpl(ResourceManager manager) {
		super(manager);
	}

	@Override
	public void addRange(T range) {
		add(range);
	}

	@Override
	public Iterator<T> listRanges() {
		return list();
	}

	@Override
	public T getRange() {
		return get();
	}
}
