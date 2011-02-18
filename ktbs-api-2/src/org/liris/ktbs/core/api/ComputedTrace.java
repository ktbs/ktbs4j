package org.liris.ktbs.core.api;

import java.util.Iterator;

import org.liris.ktbs.core.api.share.TransformationResource;


/**
 * A KTBS computed trace.
 * 
 * @author Damien Cram
 *
 */
public interface ComputedTrace extends Trace, TransformationResource  {
	
	/**
	 * The KTBS method used to computed this trace.
	 * 
	 * @return the method if any, null otherwise
	 */
	public Method getMethod();
	
	/**
	 * Set a new transformation method to compute the obsels of this trace.
	 * 
	 * @param method the new method
	 */
	public void setMethod(Method method);
	
	
	/**
	 * List all traces that are used as input of the transformation 
	 * that produces this computed trace.
	 * 
	 * @return an iterator on the source traces
	 */
	public Iterator<Trace> listSourceTraces();
	
	/**
	 * Add a new trace to be used as input of the transformation 
	 * that produces this computed trace.
	 * 
	 * @param sourceTrace the new source trace
	 */
	public void addSourceTrace(Trace sourceTrace);
	
}
