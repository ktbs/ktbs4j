package org.liris.ktbs.core.domain.interfaces;

import java.util.Set;



public interface WithParameters {

	public abstract Set<IMethodParameter> getMethodParameters();

	public abstract void setMethodParameters(
			Set<IMethodParameter> methodParameters);

}