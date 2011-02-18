package org.liris.ktbs.core.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.share.KtbsResource;

class ResourceCollectionDelegate<T extends KtbsResource> {
	
	protected Set<String> set = new HashSet<String>();
	
	private ResourceProvider manager;

	public ResourceCollectionDelegate(ResourceProvider manager) {
		super();
		this.manager = manager;
	}
	
	protected void add(T t) {
		set.add(t.getURI());
	}
	
	protected Iterator<T> list() {
		return new ResIt<T>(manager, set);
	}
	
	protected T get() {
		if(set.isEmpty())
			return null;
		else
			return (T)manager.getKtbsResource(set.iterator().next());
	}
	
	protected T get(String uri) {
		if(set.contains(uri))
			return (T)manager.getKtbsResource(uri);
		else
			return null;
	}

	public void remove(String uri) {
		set.remove(uri);
	}

	public boolean contains(T t) {
		return set.contains(t.getURI());
	}
}
