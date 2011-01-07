package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;

class SimpleAttributStatement implements AttributeStatement {
	private AttributeType type;
	private Object value;
	
	public SimpleAttributStatement(AttributeType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public AttributeType getAttributeType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AttributeStatement) {
			AttributeStatement as = (AttributeStatement) obj;
			return as.getAttributeType().equals(this.getAttributeType())
						&& as.getValue().equals(this.getValue());
		}
		return false;
	}
}