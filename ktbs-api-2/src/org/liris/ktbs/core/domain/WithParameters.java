package org.liris.ktbs.core.domain;

import java.util.Set;



public interface WithParameters {

	public abstract Set<MethodParameter> getMethodParameters();

	public abstract void setMethodParameters(
			Set<MethodParameter> methodParameters);

}