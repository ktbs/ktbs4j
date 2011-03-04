package org.liris.ktbs.core.domain.interfaces;

import java.util.Iterator;

public interface IResourceContainer<T extends IKtbsResource> extends Iterable<T> {

	public T get(String resourceURI);

	public boolean delete(String resourceURI);

	public Iterator<T> listResources();

}