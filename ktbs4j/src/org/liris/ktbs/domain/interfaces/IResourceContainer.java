package org.liris.ktbs.domain.interfaces;

import java.util.Iterator;

public interface IResourceContainer<T extends IKtbsResource> extends Iterable<T> {

	public <U extends T> U get(String resourceURI, Class<U> cls);
	
	public T get(String resourceURI);

	public boolean delete(String resourceURI);

	public Iterator<T> listResources();

}