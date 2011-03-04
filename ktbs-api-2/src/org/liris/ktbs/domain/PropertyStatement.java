package org.liris.ktbs.domain;

import org.liris.ktbs.domain.interfaces.IPropertyStatement;


public class PropertyStatement implements IPropertyStatement {
	
	private final Object value;
	private final String propertyName;

	public PropertyStatement(Object value, String propertyName) {
		this.value = value;
		this.propertyName = propertyName;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IPropertyStatement#getValue()
	 */
	@Override
	public Object getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IPropertyStatement#getProperty()
	 */
	@Override
	public String getProperty() {
		return propertyName;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof PropertyStatement) {
			IPropertyStatement stmt= (IPropertyStatement) obj;
			return stmt.getProperty().equals(propertyName)
					&& stmt.getValue().equals(value);
		} else
			return false;
	}
}