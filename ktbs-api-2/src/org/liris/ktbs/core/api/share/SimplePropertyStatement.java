package org.liris.ktbs.core.api.share;

import org.liris.ktbs.core.api.PropertyStatement;

public class SimplePropertyStatement implements PropertyStatement {
	private final Object value;
	private final String propertyName;

	public SimplePropertyStatement(Object value, String propertyName) {
		this.value = value;
		this.propertyName = propertyName;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String getProperty() {
		return propertyName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PropertyStatement) {
			PropertyStatement stmt= (PropertyStatement) obj;
			return stmt.getProperty().equals(propertyName)
					&& stmt.getValue().equals(value);
		} else
			return false;
	}
	
}