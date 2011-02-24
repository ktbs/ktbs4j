package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.core.api.MethodParameter;

public class ComputedTracePojo extends TracePojo implements WithParameters {

	private MethodPojo method;
	
	private Set<TracePojo> sourceTraces = new HashSet<TracePojo>();
	
	public MethodPojo getMethod() {
		return method;
	}

	public void setMethod(MethodPojo method) {
		this.method = method;
	}

	public Set<TracePojo> getSourceTraces() {
		return sourceTraces;
	}

	public void setSourceTraces(Set<TracePojo> sourceTraces) {
		this.sourceTraces = sourceTraces;
	}
	
	private WithParameters withMethodParameterDelegate = new WithParametersPojo();
	
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
