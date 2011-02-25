package org.liris.ktbs.core.domain;

import org.liris.ktbs.core.domain.interfaces.IAttributePair;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;

public class AttributePair implements IAttributePair {
	
	private IAttributeType attributeType;
	private Object value;
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IAttributePair#getAttributeType()
	 */
	@Override
	public IAttributeType getAttributeType() {
		return attributeType;
	}
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IAttributePair#setAttributeType(org.liris.ktbs.core.domain.AttributeType)
	 */
	@Override
	public void setAttributeType(IAttributeType attributeType) {
		this.attributeType = attributeType;
	}
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IAttributePair#getValue()
	 */
	@Override
	public Object getValue() {
		return value;
	}
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IAttributePair#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		this.value = value;
	}
	public AttributePair() {
		super();
	}
	public AttributePair(AttributeType attributeType, Object value) {
		super();
		this.attributeType = attributeType;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AttributePair) {
			IAttributePair att = (IAttributePair) obj;
			return att.getAttributeType().equals(attributeType) && value.equals(att.getValue());
		}
		return false;
	}
}
