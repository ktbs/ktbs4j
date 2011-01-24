package org.liris.ktbs.core.api;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;

import org.liris.ktbs.core.TemporalDomainException;

/**
 * A KTBS trace.
 * 
 * @author Damien Cram
 *
 */
public interface Trace extends KtbsResource, ResourceContainer {
	
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
	 * List all transformed traces in the KTBS that 
	 * use this trace as a source.
	 * 
	 * @return an iterator on the transformed traces
	 */
	public Iterator<Trace> listTransformedTraces();
	
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
	 * Remove any existing value for "ktbs:hasOrigin" property and set a 
	 * new one as a Java {@link Date}.
	 * 
	 * @param origin the new origin
	 * @throws TemporalDomainException if the temporal domain of this trace
	 * does not allow to set the origin as a date.
	 * 
	 */
	public void setOriginAsDate(Date origin);
	
	/**
	 * Give the value of the "ktbs:hasOrigin" property 
	 * interpreted as a Java date.
	 * 
	 * @return the origin of this trace if any, null otherwise
	 * @throws org.liris.ktbs.core.TemporalDomainException
	 */
	public Date getOriginAsDate();
	
	/**
	 * Give the contained obsel of a given uri.
	 * 
	 * @param obselURI the uri of the obsel
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
	 * Give the value of the "ktbs:compliesWithModel" property.
	 * 
	 * @return true if the value of "ktbs:compliesWithModel" property 
	 * is "yes", false otherwise
	 */
	public boolean isCompliantWithModel();
}
