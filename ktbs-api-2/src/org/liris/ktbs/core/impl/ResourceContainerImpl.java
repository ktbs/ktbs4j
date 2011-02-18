package org.liris.ktbs.core.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.ResourceContainer;
import org.liris.ktbs.utils.KtbsUtils;

public abstract class ResourceContainerImpl<T extends KtbsResource> extends ResourceImpl implements ResourceContainer<T> {
	
	protected ResourceContainerImpl(String uri) {
		super(uri);
	}

	private Set<String> resourceURIs = new HashSet<String>();
	
	@Override
	public T get(String childURI) {
		String childAbsoluteURI = KtbsUtils.resolveAbsoluteChildURI(uri, childURI);
		return (T)manager.getKtbsResource(childAbsoluteURI);
	}

	@Override
	public boolean delete(String resourceURI) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Iterator<T> listResources() {
		return new ResIt<T>(manager, resourceURIs);
	}
	
	protected void addContainedResource(T resource) {
		resourceURIs.add(resource.getURI());
	}
}
