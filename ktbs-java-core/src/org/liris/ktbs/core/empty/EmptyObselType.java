package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.Mode;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.TraceModel;

public class EmptyObselType extends EmptyResource implements ObselType {

	EmptyObselType(String uri) {
		super(uri);
	}

	@Override
	public Iterator<AttributeType> listAttributes(Mode mode) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<RelationType> listOutgoingRelations(Mode mode) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public TraceModel getTraceModel() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public ObselType getSuperObselType() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<RelationType> listIncomingRelations(Mode mode) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setSuperObselType(ObselType type) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public boolean hasSuperType(ObselType type, Mode mode) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}
