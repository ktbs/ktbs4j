package org.liris.ktbs.core.domain;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.utils.KtbsUtils;

public abstract class AbstractResourceFactory implements ResourceFactory {

	@Override
	public <T extends IKtbsResource> T createResource(String parentUri, String localName, boolean leaf, Class<T> cls) {
		if(localName == null)
			return createResource(cls);
		else 			
			return createResource(KtbsUtils.makeChildURI(parentUri, localName, leaf), cls);
	}
}
