package org.liris.ktbs.dao;

import org.liris.ktbs.domain.interfaces.IKtbsResource;


public interface CachingDao extends ResourceDao {
	/**
	 * 
	 * @param <T>
	 * @param uri
	 * @param cls
	 * @param fromServer
	 * @return
	 */
	public <T extends IKtbsResource> T get(String uri, Class<T> cls, boolean fromServer);

	/**
	 * 
	 */
	public void clearCache();
	
	/**
	 * 
	 * @param resource
	 */
	public void removeFromCache(IKtbsResource resource);
	
	/**
	 * 
	 * @param resourceUri
	 */
	public void removeFromCache(String resourceUri);

	/**.
	 * @param absoluteResourceUri
	 * @param ignoreCache
	 * @return
	 */
	public IKtbsResource get(String uri, boolean ignoreCache);
}

