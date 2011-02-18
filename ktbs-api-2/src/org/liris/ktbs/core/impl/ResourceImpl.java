package org.liris.ktbs.core.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.PropertyStatement;
import org.liris.ktbs.utils.KtbsUtils;

public abstract class ResourceImpl implements KtbsResource {

	protected ResourceProvider manager;
	protected final String uri;

	
	public ResourceImpl(String uri) {
		super();
		this.uri = uri;
	}

	@Override
	public int compareTo(KtbsResource o) {
		return uri.compareTo(o.getURI());
	}

	@Override
	public String getURI() {
		return uri;
	}

	private LinkedList<String> labels = new LinkedList<String>();

	@Override
	public String getLabel() {
		try {
			return labels.getFirst();
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public void addLabel(String label) {
		labels.add(label);
	}

	@Override
	public String getTypeUri() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private Set<PropertyStatement> propertyStatements = new HashSet<PropertyStatement>();

	@Override
	public Iterator<PropertyStatement> listProperties() {
		return propertyStatements.iterator();
	}

	@Override
	public Collection<Object> getPropertyValues(String propertyName) {
		Collection<Object> values = new HashSet<Object>();
		for(PropertyStatement stmt:propertyStatements) {
			if(stmt.getPropertyName().equals(propertyName))
				values.add(stmt.getPropertyValue());
		}
		return values;
	}

	@Override
	public Object getPropertyValue(String propertyName) {
		for(PropertyStatement stmt:propertyStatements) {
			if(stmt.getPropertyName().equals(propertyName))
				return stmt.getPropertyValue();
		}
		return null;
	}

	@Override
	public void addProperty(final String propertyName, final Object value) {
		propertyStatements.add(new PropertyStatement() {
			@Override
			public String getResourceUri() {
				return ResourceImpl.this.uri;
			}

			@Override
			public Object getPropertyValue() {
				return value;
			}

			@Override
			public String getPropertyName() {
				return propertyName;
			}
		});
	}

	@Override
	public void removeProperty(String propertyName) {
		Iterator<PropertyStatement> it = listProperties();
		while (it.hasNext()) {
			PropertyStatement propertyStatement = (PropertyStatement) it.next();
			if(propertyName.equals(propertyStatement.getPropertyName()))
				it.remove();
		}

	}

	@Override
	public String getLocalName() {
		return KtbsUtils.resolveLocalName(uri);
	}

	@Override
	public KtbsResource getParentResource() {
		return manager.getKtbsResource(KtbsUtils.resolveParentURI(uri));
	}
}
