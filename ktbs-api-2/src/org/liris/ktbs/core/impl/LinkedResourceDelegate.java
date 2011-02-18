package org.liris.ktbs.core.impl;

import org.liris.ktbs.core.api.share.KtbsResource;

public class LinkedResourceDelegate<T extends KtbsResource> {
	
	private ResourceManager manager;
	private String resourceURI;
	
	public LinkedResourceDelegate(ResourceManager manager) {
		super();
		this.manager = manager;
	}
	
	void set(T t) {
		this.resourceURI = t.getURI();
	}
	
	T get() {
		return (T) manager.getKtbsResource(resourceURI);
	}
}
