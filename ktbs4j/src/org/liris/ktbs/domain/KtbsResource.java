package org.liris.ktbs.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IPropertyStatement;
import org.liris.ktbs.utils.KtbsUtils;

@SuppressWarnings("serial")
public class KtbsResource extends UriResource implements IKtbsResource, Serializable {

	private Set<String> labels = new HashSet<String>();
	private Set<IPropertyStatement> properties = new HashSet<IPropertyStatement>();

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#getLabels()
	 */
	@Override
	public Set<String> getLabels() {
		return labels;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#setLabels(java.util.Set)
	 */
	@Override
	public void setLabels(Set<String> labels) {
		this.labels = labels;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#getProperties()
	 */
	@Override
	public Set<IPropertyStatement> getProperties() {
		return properties;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#setProperties(java.util.Set)
	 */
	@Override
	public void setProperties(Set<IPropertyStatement> properties) {
		this.properties = properties;
	}

	private static <T> T getFirstOrNull(Collection<T> c) {
		if(c.isEmpty())
			return null;
		else
			return c.iterator().next();
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#getLabel()
	 */
	@Override
	public String getLabel() {
		return getFirstOrNull(labels);
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#addLabel(java.lang.String)
	 */
	@Override
	public void addLabel(String label) {
		labels.add(label);
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#getPropertyValues(java.lang.String)
	 */
	@Override
	public Collection<Object> getPropertyValues(String propertyName) {
		Collection<Object> values = new HashSet<Object>();
		for(IPropertyStatement stmt:getProperties()) {
			if(stmt.getProperty().equals(propertyName))
				values.add(stmt.getValue());
		}
		return values;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#getPropertyValue(java.lang.String)
	 */
	@Override
	public Object getPropertyValue(String propertyName) {
		IPropertyStatement firstProperty = getFirstOrNull(properties);
		return firstProperty==null?
				null:
					firstProperty.getValue();
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#addProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public void addProperty(final String propertyName, final Object value) {
		properties.add(new PropertyStatement(value, propertyName));
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#removeProperty(java.lang.String)
	 */
	@Override
	public void removeProperty(String propertyName) {
		Iterator<IPropertyStatement> it = getProperties().iterator();
		while (it.hasNext()) {
			IPropertyStatement propertyStatement = it.next();
			if(propertyName.equals(propertyStatement.getProperty()))
				it.remove();
		}
	}



	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IKtbsResource#getTypeUri()
	 */
	@Override
	public String getTypeUri() {
		if (this instanceof IObsel) {
			IObsel obsel = (IObsel) this;
			IObselType obselType = obsel.getObselType();
			return obselType==null?null:obselType.getUri();
		} else
			return KtbsUtils.getRDFType(this.getClass());
	}

	private IKtbsResource parentResource;

	@Override
	public IKtbsResource getParentResource() {
		return parentResource;
	}

	public void setParentResource(IKtbsResource parentResource) {
		this.parentResource = parentResource;
	}

	@Override
	public String getParentUri() {
		IKtbsResource p = getParentResource();
		if(p != null)
			return p.getUri();
		else
			return super.getParentUri();
	}
}
