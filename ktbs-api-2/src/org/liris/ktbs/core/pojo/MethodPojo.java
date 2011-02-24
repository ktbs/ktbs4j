package org.liris.ktbs.core.pojo;

import java.util.Set;

import org.liris.ktbs.core.api.MethodParameter;

public class MethodPojo extends ResourcePojo implements WithParameters {

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

	private WithParameters withMethodParameterDelegate = new WithParametersPojo();

	@Override
	public Set<MethodParameter> getMethodParameters() {
		return withMethodParameterDelegate.getMethodParameters();
	}

	@Override
	public void setMethodParameters(Set<MethodParameter> methodParameters) {
		withMethodParameterDelegate.setMethodParameters(methodParameters);
	}
	
	public void setWithMethodParameterDelegate(
			WithParameters withMethodParameterDelegate) {
		this.withMethodParameterDelegate = withMethodParameterDelegate;
	}
}
