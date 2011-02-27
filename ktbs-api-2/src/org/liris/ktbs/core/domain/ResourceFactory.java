package org.liris.ktbs.core.domain;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;

public interface ResourceFactory {

	public <T extends IKtbsResource> T createResource(String uri, Class<T> cls);

}