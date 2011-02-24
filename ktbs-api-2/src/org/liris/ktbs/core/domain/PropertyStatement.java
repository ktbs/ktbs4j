package org.liris.ktbs.core.domain;


public class PropertyStatement {
	
	private final Object value;
	private final String propertyName;

	public PropertyStatement(Object value, String propertyName) {
		this.value = value;
		this.propertyName = propertyName;
	}

	public Object getValue() {
		return value;
	}

	public String getProperty() {
		return propertyName;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof PropertyStatement) {
			PropertyStatement stmt= (PropertyStatement) obj;
			return stmt.getProperty().equals(propertyName)
					&& stmt.getValue().equals(value);
		} else
			return false;
	}
}