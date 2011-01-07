package org.liris.ktbs.core;

import java.util.Iterator;

public interface Base extends KtbsResource, ResourceContainer {
	
	public KtbsRoot getKtbsRoot();

	
	public Iterator<TraceModel> listTraceModels();
	public Iterator<StoredTrace> listStoredTraces();
	public Iterator<ComputedTrace> listComputedTraces();
	public Iterator<Method> listMethods();
	public Iterator<Trace> listTraces();
	public Iterator<KtbsResource> listResources();
	
	public void addStoredTrace(StoredTrace trace);
	public StoredTrace getStoredTrace(String uri);

	public void addComputedTrace(ComputedTrace trace);
	public ComputedTrace getComputedTrace(String uri);
	public Trace getTrace(String uri);

	public void addTraceModel(TraceModel traceModel);
	public TraceModel getTraceModel(String uri);
	
	public void addMethod(Method method);
	public Method getMethod(String uri);
	
}
