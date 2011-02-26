package org.liris.ktbs.dao;

import org.liris.ktbs.core.ResultSet;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;

public interface ResourceDao {
	public <T extends IKtbsResource> T get(String uri, Class<T> cls);
	public IKtbsResource get(String uri);
	public boolean create(IKtbsResource resource);
	public boolean save(IKtbsResource resource);
	public boolean delete(String uri);
	public <T extends IKtbsResource> ResultSet<T> query(String request, Class<T> cls);
}
