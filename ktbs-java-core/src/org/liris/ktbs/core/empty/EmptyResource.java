package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.AbstractKtbsResource;
import org.liris.ktbs.core.KtbsStatement;

public class EmptyResource extends AbstractKtbsResource {

	protected final String MESSAGE = "This method is not supported (Empty KtbsResource)";
	
	EmptyResource(String uri) {
		super(uri);
	}

	@Override
	public String getLabel() {
		throw new UnsupportedOperationException(MESSAGE);
	}
	

	@Override
	public void setLabel(String label) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<KtbsStatement> listAllStatements() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<KtbsStatement> listKtbsStatements() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<KtbsStatement> listNonKtbsStatements() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Object[] getPropertyValues(String propertyName) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getResourceType() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void addProperty(String propertyName, Object value) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void removeProperty(String propertyName) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}

