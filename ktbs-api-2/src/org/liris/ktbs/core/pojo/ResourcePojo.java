package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.core.api.PropertyStatement;

public abstract class ResourcePojo extends UriResource {

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
}
