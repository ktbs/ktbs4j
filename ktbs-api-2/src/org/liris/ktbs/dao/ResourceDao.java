package org.liris.ktbs.dao;

import org.liris.ktbs.core.domain.KtbsResource;

public interface ResourceDao {
	public KtbsResource get(String uri);
	public boolean create(KtbsResource resource);
	public boolean save(KtbsResource resource);
	public boolean delete(String uri);
}
