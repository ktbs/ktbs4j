package org.liris.ktbs.domain.interfaces;

import java.util.Collection;
import java.util.Set;


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

	public IKtbsResource getParentResource();

	public String getTypeUri();

}