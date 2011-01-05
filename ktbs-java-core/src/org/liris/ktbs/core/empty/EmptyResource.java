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
	public Iterator<KtbsStatement> listAllProperties() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<KtbsStatement> listKtbsProperties() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<KtbsStatement> listNonKtbsProperties() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String[] getPropertyValues(String propertyName) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}

