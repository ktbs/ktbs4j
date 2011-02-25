package org.liris.ktbs.core.domain.interfaces;

import java.util.Collection;
import java.util.Set;

import org.liris.ktbs.core.IUriResource;

public interface IKtbsResource extends IUriResource {

	public Set<String> getLabels();

	public void setLabels(Set<String> labels);

	public Set<IPropertyStatement> getProperties();

	public void setProperties(Set<IPropertyStatement> properties);

	public String getLabel();

	public void addLabel(String label);

	public Collection<Object> getPropertyValues(String propertyName);

	public Object getPropertyValue(String propertyName);

	public void addProperty(final String propertyName, final Object value);

	public void removeProperty(String propertyName);

	public String getLocalName();

	public String getTypeUri();

}