package org.liris.ktbs.core.impl;

import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.share.KtbsResource;

public class ResIt<T extends KtbsResource> implements Iterator<T> {

	private ResourceProvider manager;
	private Iterator<String> uriIterator;
	
	public ResIt(ResourceProvider manager, Set<String> uris) {
		super();
		this.manager = manager;
		this.uriIterator = uris.iterator();
	}

	@Override
	public boolean hasNext() {
		return this.uriIterator.hasNext();
	}

	@Override
	public T next() {
		return (T)manager.getKtbsResource(uriIterator.next());
	}

	@Override
	public void remove() {
		uriIterator.remove();
	}
}
