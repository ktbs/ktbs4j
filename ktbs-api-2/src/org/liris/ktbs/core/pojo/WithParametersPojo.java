package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.core.api.MethodParameter;

public class WithParametersPojo implements WithParameters  {
	
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
