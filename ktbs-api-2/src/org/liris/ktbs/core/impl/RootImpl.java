package org.liris.ktbs.core.impl;

import java.util.Iterator;

import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.Root;

public class RootImpl extends ResourceContainerImpl<Base> implements Root{

	protected RootImpl(String uri) {
		super(uri);
	}

	@Override
	public Base get(String resourceURI) {
		return getBase(resourceURI);
	}

	private ResourceCollectionDelegate<Base> baseContainerDelegate = new ResourceCollectionDelegate<Base>(manager);
	
	@Override
	public Iterator<Base> listBases() {
		return baseContainerDelegate.list();
	}

	@Override
	public Base newBase(String baseLocalName, String owner) {
		Base base = manager.newBase(this, baseLocalName, owner);
		baseContainerDelegate.add(base);
		addContainedResource(base);
		return base;
	}

	@Override
	public Base getBase(String baseURI) {
		return baseContainerDelegate.get(baseURI);
	}

}
