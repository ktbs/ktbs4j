package org.liris.ktbs.core.domain;

import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IRoot;

public class PojoFactory {

	public <T extends IKtbsResource> T createResource(String uri, Class<T> cls) {
		KtbsResource resource;
		if(IRoot.class.isAssignableFrom(cls))
			resource = new Root();
		else if(IBase.class.isAssignableFrom(cls))
			resource = new Base();
		else
			throw new IllegalStateException("Cannot create a resource of the unknown type: " + cls.getCanonicalName());
		resource.setURI(uri);
		return cls.cast(resource);
	}
}
