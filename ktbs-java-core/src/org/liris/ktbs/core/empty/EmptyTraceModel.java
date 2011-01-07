package org.liris.ktbs.core.empty;

import java.util.Iterator;

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
	public Iterator<AttributeType> listAttributeTypes() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<RelationType> listRelationTypes() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<ObselType> listObselTypes() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public ObselType getObselType(String obselTypeUri) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public RelationType getRelationType(String relationTypeUri) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public AttributeType getAttributeType(String attributeTypeUri) {
		throw new UnsupportedOperationException(MESSAGE);
	}

}
