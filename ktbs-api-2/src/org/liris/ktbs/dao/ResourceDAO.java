package org.liris.ktbs.dao;

import org.liris.ktbs.core.api.share.KtbsResource;

public interface ResourceDAO {
	public KtbsResource get(String uri);
	public boolean create(KtbsResource resource);
	public boolean save(KtbsResource resource);
	public boolean delete(String uri);
}
