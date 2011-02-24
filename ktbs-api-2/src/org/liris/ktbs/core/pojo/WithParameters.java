package org.liris.ktbs.core.pojo;

import java.util.Set;

import org.liris.ktbs.core.api.MethodParameter;

public interface WithParameters {

	public abstract Set<MethodParameter> getMethodParameters();

	public abstract void setMethodParameters(
			Set<MethodParameter> methodParameters);

}