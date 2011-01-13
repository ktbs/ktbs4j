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
	
	
	public StoredTrace createStoredTrace(String traceURI, TraceModel model);
	public ComputedTrace createComputedTrace(String traceURI, TraceModel model, Method method, Collection<Trace> sources);
	public Method createMethod(String methodURI, String inheritedMethod);
	public TraceModel createTraceModel(String modelURI);

	
//	public void addStoredTrace(StoredTrace trace);
	public StoredTrace getStoredTrace(String uri);

//	public void addComputedTrace(ComputedTrace trace);
	public ComputedTrace getComputedTrace(String uri);
	public Trace getTrace(String uri);

//	public void addTraceModel(TraceModel traceModel);
	public TraceModel getTraceModel(String uri);
	
//	public void addMethod(Method method);
	public Method getMethod(String uri);
	
}
