package org.liris.ktbs.dao;

import org.liris.ktbs.core.ResultSet;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;

public interface ResourceDao {
	public <T extends IKtbsResource> T get(String uri, Class<T> cls);
	public <T extends IKtbsResource> T create(T prototype);
	public boolean save(IKtbsResource resource);
	public boolean delete(String uri);
	public <T extends IKtbsResource> ResultSet<T> query(String request, Class<T> cls);
}
