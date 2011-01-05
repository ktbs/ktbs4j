package org.liris.ktbs.core.empty;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ResourceContainer;

public class EmptyResourceContainer extends EmptyResource implements
		ResourceContainer {

	EmptyResourceContainer(String uri) {
		super(uri);
	}

	@Override
	public KtbsResource get(String resourceURI) {
		throw new UnsupportedOperationException(MESSAGE);
	}

}
