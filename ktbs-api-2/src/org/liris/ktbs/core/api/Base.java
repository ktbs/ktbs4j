package org.liris.ktbs.core.api;

import java.util.Iterator;
import java.util.Set;


/**
 * A KTBS base.
 * 
 * @author Damien Cram
 *
 */
public interface Base extends KtbsResource, ResourceContainer<KtbsResource>  {
	
	/**
	 * Give the owner of this base.
	 * 
	 * @return the user id of this bases's owner
	 */
	public String getOwner();
	
	/**
	 * List all trace models owned by this base.
	 * 
	 * @return an iterator on owned trace models
	 */
	public Iterator<TraceModel> listTraceModels();
	
	/**
	 * List all stored traces owned by this base.
	 * 
	 * @return an iterator on owned stored traces
	 */
	public Iterator<StoredTrace> listStoredTraces();

	/**
	 * List all computed traces owned by this base.
	 * 
	 * @return an iterator on owned computed traces
	 */
	public Iterator<ComputedTrace> listComputedTraces();

	/**
	 * List all methods owned by this base.
	 * 
	 * @return an iterator on owned methods
	 */
	public Iterator<Method> listMethods();
	
	
	/**
	 * Create a new owned stored trace in this base.
	 * 
	 * @param traceLocalName the local name of the new trace
	 * @param model the trace model of the new trace
	 * @param origin the origin of the created trace
	 * @return the created stored trace
	 */
	public StoredTrace newStoredTrace(String traceLocalName, TraceModel model, String origin);
	
	/**
	 * Create a new owned computed trace in this base.
	 * 
	 * @param traceLocalName the local name of the new trace
	 * @param model the trace model of the new trace
	 * @param method the method to use to compute the trace
	 * @param sources the collection of source traces to use 
	 * as input of the transformation process that will compute 
	 * the obsels of the new trace
	 * @return the created computed trace
	 */
	public ComputedTrace newComputedTrace(String traceLocalName, TraceModel model, Method method, Set<Trace> sources);
	
	/**
	 * Create a new owned method in this base.
	 * 
	 * @param methodLocalName the local name of the new method
	 * @param inheritedMethod the value of the "ktbs:inherited" property for the new method
	 * @return the created method
	 */
	public Method newMethod(String methodLocalName, String inheritedMethod);
	
	/**
	 * Create a new owned trace model in this base.
	 * 
	 * @param modelLocalName the local name of the new trace model
	 * @return the created trace model
	 */
	public TraceModel newTraceModel(String modelLocalName);

	/**
	 * Give the stored trace of a given uri owned by this base.
	 * 
	 * @param traceURI the uri (either absolute or relative to the base URI) of the stored trace to retrieve
	 * @return the stored trace of that uri owned by this base if any, null otherwise
	 */
	public StoredTrace getStoredTrace(String traceURI);

	/**
	 * Give the computed trace of a given uri owned by this base.
	 * 
	 * @param traceURI the uri (either absolute or relative to the base URI) of the computed trace to retrieve
	 * @return the computed trace of that uri owned by this base if any, null otherwise
	 */
	public ComputedTrace getComputedTrace(String traceURI);

	/**
	 * Give the trace of a given uri owned by this base.
	 * 
	 * @param traceURI the uri (either absolute or relative to the base URI) of the trace to retrieve
	 * @return the trace of that uri owned by this base if any, null otherwise
	 */
	public Trace getTrace(String traceURI);

	/**
	 * Give the trace model of a given uri owned by this base.
	 * 
	 * @param traceModelURI the uri (either absolute or relative to the base URI) of the trace model to retrieve
	 * @return the trace model of that uri owned by this base if any, null otherwise
	 */
	public TraceModel getTraceModel(String traceModelURI);
	
	/**
	 * Give the method of a given uri owned by this base.
	 * 
	 * @param methodURI the uri (either absolute or relative to the base URI) of the method to retrieve
	 * @return the method of that uri owned by this base if any, null otherwise
	 */
	public Method getMethod(String methodURI);
}
