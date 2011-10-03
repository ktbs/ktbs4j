package org.liris.ktbs.domain.interfaces;

import java.util.Set;

public interface IComputedTrace extends ITrace, WithParameters {

	public IMethod getMethod();

	public void setMethod(IMethod method);

	public Set<ITrace> getSourceTraces();

	public void setSourceTraces(Set<ITrace> sourceTraces);

	public Set<ITrace> getIntermediateSource();

	public void setIntermediateSource(Set<ITrace> sourceTraces);

	public Boolean getIsIntermediateSource();
	
	public void setIsIntermediateSource(Boolean isIntermediateSourceOfComputedTrace);

	public Set<IMethodParameter> getMethodParameters();

	public void setMethodParameters(Set<IMethodParameter> methodParameters);

}