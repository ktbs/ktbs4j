package org.liris.ktbs.dao;

import org.liris.ktbs.core.pojo.ResourcePojo;

public interface ResourceDao {
	public ResourcePojo get(String uri);
	public boolean create(ResourcePojo resource);
	public boolean save(ResourcePojo resource);
	public boolean delete(String uri);
}
