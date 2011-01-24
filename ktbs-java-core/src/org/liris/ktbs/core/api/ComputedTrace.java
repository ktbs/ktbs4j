package org.liris.ktbs.core.api;

import java.util.Iterator;


/**
 * A KTBS computed trace.
 * 
 * @author Damien Cram
 *
 */
public interface ComputedTrace extends Trace, ResourceWithParameters {
	
	/**
	 * The KTBS method used to computed this trace.
	 * 
	 * @return the method if any, null otherwise
	 */
	public Method getMethod();
	
	/**
	 * Replace any existing method with a new one.
	 * 
	 * @param method the new method to use to compute this trace
	 */
	public void setMethod(Method method);
	
	/**
	 * List all traces that are used as input of the transformation 
	 * that produces this computed trace.
	 * 
	 * @return an iterator on the source traces
	 */
	public Iterator<Trace> listSources();
	
	/**
	 * Add a new trace to be used as input of the transformation 
	 * that produces this computed trace.
	 * 
	 * @param sourceTrace the new source trace
	 */
	public void addSourceTrace(Trace sourceTrace);
	
}
