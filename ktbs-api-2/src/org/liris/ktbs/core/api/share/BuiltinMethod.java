package org.liris.ktbs.core.api.share;

import java.util.Collection;
import java.util.Iterator;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.api.Method;

/**
 * A read-only method representing KTBS built-in method and throwing 
 * an exception when invoking methods that are not relevant for such 
 * a method.
 * 
 * 
 * @author Damien Cram
 * @see Method
 * @see BuiltinResourceException
 */
public class BuiltinMethod implements Method {

	private String uri;
	
	public BuiltinMethod(String uri) {
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
	public void setMethodParameter(String key, String value) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}


	@Override
	public void removeMethodParameter(String key) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}



	@Override
	public String getETag() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void addLabel(String label) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public String getTypeUri() {
		return KtbsConstants.METHOD;
	}

	@Override
	public Iterator<PropertyStatement> listProperties() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Object getPropertyValue(String propertyName) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Method getInheritedMethod() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void setInheritedMethod(Method method) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Collection<Object> getPropertyValues(String propertyName) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Iterator<MethodParameter> listMethodParameters() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public String getMethodParameter(String key) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public String getLocalName() {
		return getURI().replaceAll(KtbsConstants.NAMESPACE, "");
	}

	@Override
	public KtbsResource getParentResource() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public void setEtag(String etag) {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}

	@Override
	public Iterator<String> listLabels() {
		throw new BuiltinResourceException(uri + " is a KTBS built-in method");
	}
}
