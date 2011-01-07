package org.liris.ktbs.core.empty;

import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;

public class EmptyRelationType extends EmptyResource implements RelationType {

	EmptyRelationType(String uri) {
		super(uri);
	}

	@Override
	public ObselType getDomain() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public ObselType getRange() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public RelationType getSuperRelationType() {
		throw new UnsupportedOperationException(MESSAGE);
	}
}