package org.liris.ktbs.core;

import java.util.Iterator;

public class BuiltinMethod implements Method {

	private String uri;
	
	BuiltinMethod(String uri) {
		super();
		this.uri = uri;
	}

	@Override
	public String getURI() {
		return uri;
	}

	@Override
	public String getLabel() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void setLabel(String label) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public String getResourceType() {
		return KtbsConstants.METHOD;
	}

	@Override
	public Iterator<KtbsStatement> listAllStatements() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Iterator<KtbsStatement> listKtbsStatements() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Iterator<KtbsStatement> listNonKtbsStatements() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Object[] getPropertyValues(String propertyName) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void addProperty(String propertyName, Object value) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void removeProperty(String propertyName) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public int compareTo(KtbsResource o) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Iterator<KtbsParameter> listParameters() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void setParameter(String key, String value) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public KtbsParameter getParameter(String key) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void removeParameter(String key) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public String getInherits() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void setInherits(String methodURI) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public String getETag() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

}