package org.liris.ktbs.dao;

import org.liris.ktbs.core.api.KtbsResource;

public interface ResourceDao<T extends KtbsResource> {
	public T get(String uri);
	public boolean create(T resource);
	public boolean save(T resource);
	public boolean delete(String uri);
}
