package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.empty.EmptyResourceFactory;

import com.hp.hpl.jena.rdf.model.Statement;

class AttributStatementImpl implements AttributeStatement {
	private final Statement next;

	AttributStatementImpl(Statement next) {
		this.next = next;
	}

	@Override
	public Object getValue() {
		return next.getObject().asLiteral().getValue();
	}

	@Override
	public AttributeType getAttributeType() {
		return EmptyResourceFactory.getInstance().createAttributeType(next.getPredicate().getURI());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AttributeStatement) {
			AttributeStatement as = (AttributeStatement) obj;
			return as.getAttributeType().equals(this.getAttributeType())
						&& as.getValue().equals(this.getValue());
		}
		return super.equals(obj);
	}
}