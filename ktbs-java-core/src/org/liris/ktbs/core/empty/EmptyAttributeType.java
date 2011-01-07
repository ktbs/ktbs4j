package org.liris.ktbs.core.empty;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.ObselType;

public class EmptyAttributeType extends EmptyResource implements AttributeType {
	EmptyAttributeType(String uri) {
		super(uri);
	}

	@Override
	public ObselType getDomain() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setDomain(ObselType domain) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}
