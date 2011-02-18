package org.liris.ktbs.core.impl;

import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.share.MethodParameter;
import org.liris.ktbs.core.api.share.TransformationResource;

public class ComputedTraceImpl extends TraceImpl implements ComputedTrace {

	protected ComputedTraceImpl(String uri) {
		super(uri);
	}
	
	/*
	 * The delegate for transformation resource aspect
	 */
	private TransformationResource transformationResourceDelegate = new TransformationResourceImpl();
	private LinkedResourceDelegate<Method> methodDelegate = new LinkedResourceDelegate<Method>(manager);
	private ResourceCollectionDelegate<Trace> sourceTraceDelegate = new ResourceCollectionDelegate<Trace>(manager);
	
	@Override
	public Iterator<MethodParameter> listMethodParameters() {
		return transformationResourceDelegate.listMethodParameters();
	}

	@Override
	public void setMethodParameter(String key, String value) {
		transformationResourceDelegate.setMethodParameter(key, value);
	}

	@Override
	public String getMethodParameter(String key) {
		return transformationResourceDelegate.getMethodParameter(key);
	}

	@Override
	public void removeMethodParameter(String key) {
		transformationResourceDelegate.removeMethodParameter(key);
	}

	@Override
	public Method getMethod() {
		return methodDelegate.get();
	}

	@Override
	public void setMethod(Method method) {
		methodDelegate.set(method);
	}
	
	@Override
	public Iterator<Trace> listSourceTraces() {
		return sourceTraceDelegate.list();
	}

	@Override
	public void addSourceTrace(Trace sourceTrace) {
		sourceTraceDelegate.add(sourceTrace);
	}

	void setSources(Set<String> sourceURIs) {
		sourceTraceDelegate.set(sourceURIs);
	}
}
