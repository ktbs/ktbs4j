package org.liris.ktbs.core.pojo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.PropertyStatement;
import org.liris.ktbs.core.api.share.SimplePropertyStatement;
import org.liris.ktbs.utils.KtbsUtils;

public class ResourcePojo extends UriResourceImpl {

	private Set<String> labels = new HashSet<String>();
	private Set<PropertyStatement> properties = new HashSet<PropertyStatement>();
	
	public Set<String> getLabels() {
		return labels;
	}
	
	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}
	
	public Set<PropertyStatement> getProperties() {
		return properties;
	}
	
	public void setProperties(Set<PropertyStatement> properties) {
		this.properties = properties;
	}

	private static <T> T getFirstOrNull(Collection<T> c) {
		if(c.isEmpty())
			return null;
		else
			return c.iterator().next();
	}

	public String getLabel() {
		return getFirstOrNull(labels);
	}

	public void addLabel(String label) {
		labels.add(label);
	}

	public Collection<Object> getPropertyValues(String propertyName) {
		Collection<Object> values = new HashSet<Object>();
		for(PropertyStatement stmt:getProperties()) {
			if(stmt.getProperty().equals(propertyName))
				values.add(stmt.getValue());
		}
		return values;
	}

	public Object getPropertyValue(String propertyName) {
		PropertyStatement firstProperty = getFirstOrNull(properties);
		return firstProperty==null?
				null:
					firstProperty.getValue();
	}

	public void addProperty(final String propertyName, final Object value) {
		properties.add(new SimplePropertyStatement(value, propertyName));
	}

	public void removeProperty(String propertyName) {
		Iterator<PropertyStatement> it = getProperties().iterator();
		while (it.hasNext()) {
			PropertyStatement propertyStatement = (PropertyStatement) it.next();
			if(propertyName.equals(propertyStatement.getProperty()))
				it.remove();
		}
	}

	public String getLocalName() {
		return KtbsUtils.resolveLocalName(getUri());
	}

	public String getTypeUri() {
		if (this instanceof ObselPojo) {
			ObselPojo obsel = (ObselPojo) this;
			return obsel.getObselType().getUri();
		} else
			return KtbsUtils.getRDFType(this.getClass());
	}
}
