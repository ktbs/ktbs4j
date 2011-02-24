package org.liris.ktbs.manager;

import java.util.Set;

import org.liris.ktbs.core.pojo.ComputedTracePojo;
import org.liris.ktbs.core.pojo.MethodPojo;
import org.liris.ktbs.core.pojo.StoredTracePojo;
import org.liris.ktbs.core.pojo.TraceModelPojo;
import org.liris.ktbs.core.pojo.TracePojo;

public interface BaseManager {

	/**
	 * Create a new owned stored trace in this base.
	 * 
	 * @param traceLocalName the local name of the new trace
	 * @param model the trace model of the new trace
	 * @param origin the origin of the created trace
	 * @return the created stored trace
	 */
	public StoredTracePojo newStoredTrace(String traceLocalName, TraceModelPojo model, String origin);
	
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
	public ComputedTracePojo newComputedTrace(String traceLocalName, TraceModelPojo model, MethodPojo method, Set<TracePojo> sources);
	
	/**
	 * Create a new owned method in this base.
	 * 
	 * @param methodLocalName the local name of the new method
	 * @param inheritedMethod the value of the "ktbs:inherited" property for the new method
	 * @return the created method
	 */
	public MethodPojo newMethod(String methodLocalName, String inheritedMethod);
	
	/**
	 * Create a new owned trace model in this base.
	 * 
	 * @param modelLocalName the local name of the new trace model
	 * @return the created trace model
	 */
	public TraceModelPojo newTraceModel(String modelLocalName);
}
