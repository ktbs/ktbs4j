package org.liris.ktbs.core.pojo;

public class AttributePairPojo {
	
	private AttributeTypePojo attributeType;
	private Object value;
	
	public AttributeTypePojo getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(AttributeTypePojo attributeType) {
		this.attributeType = attributeType;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public AttributePairPojo() {
		super();
	}
	public AttributePairPojo(AttributeTypePojo attributeType, Object value) {
		super();
		this.attributeType = attributeType;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AttributePairPojo) {
			AttributePairPojo att = (AttributePairPojo) obj;
			return att.getAttributeType().equals(attributeType) && value.equals(att.getValue());
		}
		return false;
	}
}
