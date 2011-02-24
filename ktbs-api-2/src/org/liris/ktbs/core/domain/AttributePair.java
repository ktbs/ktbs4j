package org.liris.ktbs.core.domain;

public class AttributePair {
	
	private AttributeType attributeType;
	private Object value;
	
	public AttributeType getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}
	public Object getValue() {
		return value;
	}
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
			AttributePair att = (AttributePair) obj;
			return att.getAttributeType().equals(attributeType) && value.equals(att.getValue());
		}
		return false;
	}
}
