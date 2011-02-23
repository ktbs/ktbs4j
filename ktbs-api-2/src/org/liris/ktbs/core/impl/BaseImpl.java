package org.liris.ktbs.core.impl;

import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;

public class BaseImpl extends ResourceContainerImpl<KtbsResource> implements  Base {

	protected BaseImpl(String uri) {
		super(uri);
	}

	private String owner;
	
	@Override
	public String getOwner() {
		return owner;
	}

	private ResourceCollectionDelegate<TraceModel> traceModelContainerDelegate = new ResourceCollectionDelegate<TraceModel>(manager);
	private ResourceCollectionDelegate<StoredTrace> storedTraceContainerDelegate = new ResourceCollectionDelegate<StoredTrace>(manager);
	private ResourceCollectionDelegate<ComputedTrace> computedTraceContainerDelegate = new ResourceCollectionDelegate<ComputedTrace>(manager);
	private ResourceCollectionDelegate<Method> methodContainerDelegate = new ResourceCollectionDelegate<Method>(manager);
	
	@Override
	public Iterator<TraceModel> listTraceModels() {
		return traceModelContainerDelegate.list();
	}

	@Override
	public Iterator<StoredTrace> listStoredTraces() {
		return storedTraceContainerDelegate.list();
	}

	@Override
	public Iterator<ComputedTrace> listComputedTraces() {
		return computedTraceContainerDelegate.list();
	}

	@Override
	public Iterator<Method> listMethods() {
		return methodContainerDelegate.list();
	}

	@Override
	public StoredTrace newStoredTrace(String traceLocalName, TraceModel model,
			String origin) {
		StoredTrace storedTrace = manager.newStoredTrace(this, traceLocalName, model, origin);
		storedTraceContainerDelegate.add(storedTrace);
		addContainedResource(storedTrace);
		return storedTrace;
	}

	@Override
	public ComputedTrace newComputedTrace(String traceLocalName,
			TraceModel model, Method method, Set<Trace> sources) {
		ComputedTrace computedTrace = manager.newComputedTrace(this, traceLocalName, method, sources);
		computedTraceContainerDelegate.add(computedTrace);
		addContainedResource(computedTrace);
		return computedTrace;
	}

	@Override
	public Method newMethod(String methodLocalName, String inheritedMethod) {
		Method method = manager.newMethod(this, methodLocalName, inheritedMethod);
		methodContainerDelegate.add(method);
		addContainedResource(method);
		return method;
	}

	@Override
	public TraceModel newTraceModel(String modelLocalName) {
		TraceModel traceModel = manager.newTraceModel(this, modelLocalName);
		traceModelContainerDelegate.add(traceModel);
		addContainedResource(traceModel);
		return traceModel;
	}

	@Override
	public StoredTrace getStoredTrace(String traceURI) {
		return storedTraceContainerDelegate.get(traceURI);
	}

	@Override
	public ComputedTrace getComputedTrace(String traceURI) {
		return computedTraceContainerDelegate.get(traceURI);
	}

	@Override
	public Trace getTrace(String traceURI) {
		Trace t = getStoredTrace(traceURI);
		if(t == null)
			return getComputedTrace(traceURI);
		else
			return t;
	}

	@Override
	public TraceModel getTraceModel(String traceModelURI) {
		return traceModelContainerDelegate.get(traceModelURI);
	}

	@Override
	public Method getMethod(String methodURI) {
		return methodContainerDelegate.get(methodURI);
	}

}
