package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.core.api.MethodParameter;

public class WithParametersPojo  {
	
	private Set<MethodParameter> methodParameters = new HashSet<MethodParameter>();

	public Set<MethodParameter> getMethodParameters() {
		return methodParameters;
	}

	public void setMethodParameters(Set<MethodParameter> methodParameters) {
		this.methodParameters = methodParameters;
	}
	
	
}
