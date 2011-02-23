package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.core.api.MethodParameter;

public class ComputedTracePojo extends TracePojo {

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
	
	private WithParametersPojo withMethodParameterDelegate = new WithParametersPojo();
	
	public Set<MethodParameter> getMethodParameters() {
		return withMethodParameterDelegate.getMethodParameters();
	}

	public void setMethodParameters(Set<MethodParameter> methodParameters) {
		withMethodParameterDelegate.setMethodParameters(methodParameters);
	}
	
	public void setWithMethodParameterDelegate(
			WithParametersPojo withMethodParameterDelegate) {
		this.withMethodParameterDelegate = withMethodParameterDelegate;
	}
}
