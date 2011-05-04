package org.liris.ktbs.domain;

import org.liris.ktbs.domain.interfaces.IKtbsResource;

public interface ResourceFactory {

	/**
	 * Creates an anonym KTBS resource.
	 * 
	 * @param <T>
	 * @param cls
	 * @return
	 */
	public <T extends IKtbsResource> T createResource(Class<T> cls);
	public <T extends IKtbsResource> T createResource(String uri, Class<T> cls);
	public IKtbsResource createResource(String uri);
	public <T extends IKtbsResource> T createResource(String parentUri, String localName, boolean leaf,
			Class<T> cls);
}