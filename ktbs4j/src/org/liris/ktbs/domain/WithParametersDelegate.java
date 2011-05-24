package org.liris.ktbs.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IMethodParameter;
import org.liris.ktbs.domain.interfaces.WithParameters;


@SuppressWarnings("serial")
public class WithParametersDelegate implements WithParameters, Serializable  {
	
	private Set<IMethodParameter> methodParameters = new HashSet<IMethodParameter>();

	@Override
	public Set<IMethodParameter> getMethodParameters() {
		return methodParameters;
	}

	@Override
	public void setMethodParameters(Set<IMethodParameter> methodParameters) {
		this.methodParameters = methodParameters;
	}
}
