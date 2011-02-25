package org.liris.ktbs.core.domain.interfaces;

import java.util.Iterator;

public interface IResourceContainer<T extends IKtbsResource> {

	public T get(String resourceURI);

	public boolean delete(String resourceURI);

	public Iterator<T> listResources();

}