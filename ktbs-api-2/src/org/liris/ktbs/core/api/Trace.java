package org.liris.ktbs.core.api;

import java.math.BigInteger;
import java.util.Iterator;


/**
 * A KTBS trace.
 * 
 * @author Damien Cram
 *
 */
public interface Trace extends KtbsResource, ResourceContainer<Obsel> {
	
	/**
	 * List all obsels contained in this trace.
	 * 
	 * @return an iterator on contained obsels
	 */
	public Iterator<Obsel> listObsels();

	/**
	 * List all obsels contained in this trace that are contained in a temporal window.
	 * 
	 * @param begin the lower bound of the temporal window as a relative obsel date
	 * @param end the upper bound of the temporal window as a relative obsel date
	 * @return an iterator on contained obsel in that window
	 */
	public Iterator<Obsel> listObsels(BigInteger begin, BigInteger end);

	/**
	 * List all obsels contained in this trace that are contained in a temporal window.
	 * 
	 * @param begin the lower bound of the temporal window as a relative obsel date
	 * @param end the upper bound of the temporal window as a relative obsel date
	 * @return an iterator on contained obsel in that window
	 */
	public Iterator<Obsel> listObsels(long begin, long end);
	
	/**
	 * List all transformed traces in the KTBS that 
	 * use this trace as a source.
	 * 
	 * @return an iterator on the transformed traces
	 */
	public Iterator<ComputedTrace> listTransformedTraces();
	
	/**
	 * Give the value of the "ktbs:hasOrigin" property.
	 * 
	 * @return the value of the "ktbs:hasOrigin" property
	 */
	public String getOrigin();
	
	/**
	 * Remove any existing value for "ktbs:hasOrigin" property and set a new one.
	 * 
	 * @param origin the new origin
	 */
	public void setOrigin(String origin);
	
	/**
	 * Give the contained obsel of a given uri.
	 * 
	 * @param obselURI the uri (either relative to the trace name or absolute) of the obsel
	 * @return the contained obsel of that uri if any, null otherwise
	 * 
	 */
	public Obsel getObsel(String obselURI);
	
	/**
	 * Give the trace model of this trace.
	 * 
	 * @return the trace model of this trace
	 */
	public TraceModel getTraceModel();
	
	/**
	 * Remove any trace model defined on this trace and set a new one.
	 * 
	 * @param traceModel the new trace model
	 */
	public void setTraceModel(TraceModel traceModel);
	
	/**
	 * Tell if this trace is compliant with its trace model
	 * 
	 * @return "yes" if compliant, "no" if not, or "unknown"
	 */
	public String getCompliantWithModel();
}
