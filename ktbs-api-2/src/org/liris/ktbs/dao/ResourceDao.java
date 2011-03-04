package org.liris.ktbs.dao;

import java.util.Collection;
import java.util.List;

import org.liris.ktbs.domain.interfaces.IKtbsResource;

public interface ResourceDao {
	
	public <T extends IKtbsResource> T get(String uri, Class<T> cls);
	public <T extends IKtbsResource> T create(T prototype);
	public boolean delete(String uri);
	public <T extends IKtbsResource> ResultSet<T> query(String request, Class<T> cls);
	public boolean save(IKtbsResource resource);
	public boolean save(IKtbsResource resource, boolean cascadeChildren);
	public boolean saveCollection(String uriToSave,
			Collection<? extends IKtbsResource> collection);
	public boolean postCollection(String uriToSave,
			List<? extends IKtbsResource> collection);
}
