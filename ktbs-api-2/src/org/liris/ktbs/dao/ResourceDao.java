package org.liris.ktbs.dao;

import org.liris.ktbs.core.domain.KtbsResource;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;

public interface ResourceDao {
	public IKtbsResource get(String uri);
	public boolean create(KtbsResource resource);
	public boolean save(KtbsResource resource);
	public boolean delete(String uri);
}
