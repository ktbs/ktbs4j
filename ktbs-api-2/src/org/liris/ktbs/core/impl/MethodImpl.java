package org.liris.ktbs.core.impl;

import java.util.Iterator;

import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.MethodParameter;
import org.liris.ktbs.core.api.TransformationResource;

public class MethodImpl extends ResourceImpl implements Method {

	public MethodImpl(String uri) {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	/*
	 * The delegate for transformation resource aspect
	 */
	private TransformationResource transformationResourceDelegate = new TransformationResourceImpl();

	@Override
	public Iterator<MethodParameter> listMethodParameters() {
		return transformationResourceDelegate.listMethodParameters();
	}

	@Override
	public void setMethodParameter(String key, String value) {
		transformationResourceDelegate.setMethodParameter(key, value);
	}

	@Override
	public String getMethodParameter(String key) {
		return transformationResourceDelegate.getMethodParameter(key);
	}

	@Override
	public void removeMethodParameter(String key) {
		transformationResourceDelegate.removeMethodParameter(key);
	}

	private String inheritedMethodUri;
	
	@Override
	public Method getInheritedMethod() {
		return (Method) manager.getKtbsResource(inheritedMethodUri);
	}

	@Override
	public void setInheritedMethod(Method method) {
		this.inheritedMethodUri = method.getUri();
	}

	private String etag;
	
	@Override
	public void setEtag(String etag) {
		this.etag = etag;
	};
	
	@Override
	public String getETag() {
		return etag;
	}
}
