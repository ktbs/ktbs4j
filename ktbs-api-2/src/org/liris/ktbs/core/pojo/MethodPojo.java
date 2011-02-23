package org.liris.ktbs.core.pojo;

import java.util.Set;

import org.liris.ktbs.core.api.MethodParameter;

public class MethodPojo extends ResourcePojo {

	private String etag;
	private String inherits;

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public String getInherits() {
		return inherits;
	}

	public void setInherits(String inherits) {
		this.inherits = inherits;
	}

	private WithParametersPojo withMethodParameterDelegate = new WithParametersPojo();

	public Set<MethodParameter> getMethodParameters() {
		return withMethodParameterDelegate.getMethodParameters();
	}

	public void setMethodParameters(Set<MethodParameter> methodParameters) {
		withMethodParameterDelegate.setMethodParameters(methodParameters);
	}
	
	public void setWithMethodParameterDelegate(
			WithParametersPojo withMethodParameterDelegate) {
		this.withMethodParameterDelegate = withMethodParameterDelegate;
	}
}
