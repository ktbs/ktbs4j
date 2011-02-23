package org.liris.ktbs.core.api.share;

import org.liris.ktbs.core.api.PropertyStatement;

public class SimplePropertyStatement implements PropertyStatement {
	private final String resourceUri;
	private final Object value;
	private final String propertyName;

	public SimplePropertyStatement(String resourceUri, Object value, String propertyName) {
		this.resourceUri = resourceUri;
		this.value = value;
		this.propertyName = propertyName;
	}

	@Override
	public String getResourceUri() {
		return resourceUri;
	}

	@Override
	public Object getPropertyValue() {
		return value;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PropertyStatement) {
			PropertyStatement stmt= (PropertyStatement) obj;
			return stmt.getResourceUri().equals(resourceUri)
					&& stmt.getPropertyName().equals(propertyName)
					&& stmt.getPropertyValue().equals(value);
		} else
			return false;
	}
	
}