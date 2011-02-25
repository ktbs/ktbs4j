package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.core.domain.interfaces.IMethodParameter;
import org.liris.ktbs.core.domain.interfaces.WithParameters;


public class WithParametersDelegate implements WithParameters  {
	
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
