package org.liris.ktbs.core.domain.interfaces;

import java.util.Set;

public interface IComputedTrace extends ITrace, WithParameters {

	public IMethod getMethod();

	public void setMethod(IMethod method);

	public Set<ITrace> getSourceTraces();

	public void setSourceTraces(Set<ITrace> sourceTraces);

	public Set<IMethodParameter> getMethodParameters();

	public void setMethodParameters(Set<IMethodParameter> methodParameters);

}