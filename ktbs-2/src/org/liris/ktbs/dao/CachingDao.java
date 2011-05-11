package org.liris.ktbs.dao;

import org.liris.ktbs.domain.interfaces.IKtbsResource;


public interface CachingDao extends ResourceDao {
	public <T extends IKtbsResource> T get(String uri, Class<T> cls, boolean fromServer);
}

