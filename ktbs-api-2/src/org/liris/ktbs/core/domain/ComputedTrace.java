package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;


public class ComputedTrace extends Trace implements WithParameters {

	private Method method;
	
	private Set<Trace> sourceTraces = new HashSet<Trace>();
	
	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Set<Trace> getSourceTraces() {
		return sourceTraces;
	}

	public void setSourceTraces(Set<Trace> sourceTraces) {
		this.sourceTraces = sourceTraces;
	}
	
	private WithParameters withMethodParameterDelegate = new WithParametersDelegate();
	
	@Override
	public Set<MethodParameter> getMethodParameters() {
		return withMethodParameterDelegate.getMethodParameters();
	}

	@Override
	public void setMethodParameters(Set<MethodParameter> methodParameters) {
		withMethodParameterDelegate.setMethodParameters(methodParameters);
	}
	
	public void setWithMethodParameterDelegate(
			WithParameters withMethodParameterDelegate) {
		this.withMethodParameterDelegate = withMethodParameterDelegate;
	}
}
