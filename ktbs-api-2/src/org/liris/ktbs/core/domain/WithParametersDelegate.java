package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;


public class WithParametersDelegate implements WithParameters  {
	
	private Set<MethodParameter> methodParameters = new HashSet<MethodParameter>();

	@Override
	public Set<MethodParameter> getMethodParameters() {
		return methodParameters;
	}

	@Override
	public void setMethodParameters(Set<MethodParameter> methodParameters) {
		this.methodParameters = methodParameters;
	}
}
