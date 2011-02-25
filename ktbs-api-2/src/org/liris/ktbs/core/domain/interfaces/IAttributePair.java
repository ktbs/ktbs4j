package org.liris.ktbs.core.domain.interfaces;


public interface IAttributePair {

	public IAttributeType getAttributeType();

	public void setAttributeType(IAttributeType attributeType);

	public Object getValue();

	public void setValue(Object value);

}