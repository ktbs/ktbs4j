package org.liris.ktbs.core.empty;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.TraceModel;

public class EmptyTraceModel extends EmptyResourceContainer implements
TraceModel {

	EmptyTraceModel(String uri) {
		super(uri);
	}

	@Override
	public AttributeType listAttributeTypes() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public RelationType listRelationTypes() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public ObselType listObselTypes() {
		throw new UnsupportedOperationException(MESSAGE);
	}
}
