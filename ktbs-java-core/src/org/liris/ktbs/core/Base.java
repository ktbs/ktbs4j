package org.liris.ktbs.core;

import java.util.Collection;
import java.util.Iterator;

public interface Base extends KtbsResource, ResourceContainer {
	
	public Iterator<TraceModel> listTraceModels();
	public Iterator<StoredTrace> listStoredTraces();
	public Iterator<ComputedTrace> listComputedTraces();
	public Iterator<Method> listMethods();
	public Iterator<Trace> listTraces();
	public Iterator<KtbsResource> listResources();
	
	
	public StoredTrace newStoredTrace(String traceURI, TraceModel model);
	public ComputedTrace newComputedTrace(String traceURI, TraceModel model, Method method, Collection<Trace> sources);
	public Method newMethod(String methodURI, String inheritedMethod);
	public TraceModel newTraceModel(String modelURI);

	public StoredTrace getStoredTrace(String uri);
	public ComputedTrace getComputedTrace(String uri);
	public Trace getTrace(String uri);

	public TraceModel getTraceModel(String uri);
	
	public Method getMethod(String uri);
	
}
