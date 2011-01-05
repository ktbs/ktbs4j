package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;

public class EmptyObselType extends EmptyResource implements ObselType {

	EmptyObselType(String uri) {
		super(uri);
	}

	@Override
	public Iterator<AttributeType> listAttributes() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<RelationType> listIncomingRelations() {
		throw new UnsupportedOperationException(MESSAGE);
	}
}
