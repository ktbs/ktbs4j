package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsResource;

public class EmptyResource implements KtbsResource {

	private String uri;
	protected final String MESSAGE = "This method is not supported (Empty KtbsResource)";
	
	EmptyResource(String uri) {
		super();
		this.uri = uri;
	}

	@Override
	public String getURI() {
		return uri;
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
	public Iterator<String> listAllProperties() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<String> listKtbsProperties() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<String> listNonKtbsProperties() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getPropertyValue(String propertyName) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}

