package org.liris.ktbs.core.domain;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;

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
}