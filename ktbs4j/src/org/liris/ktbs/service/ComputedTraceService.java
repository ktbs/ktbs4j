package org.liris.ktbs.service;

import java.util.Collection;

import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IObsel;

/**
 * Provides convenient method for handling computed traces, 
 * collecting traces
 * 
 * @author Dino COSMAS
 * @see ResourceService
 */
public interface ComputedTraceService extends IRootAwareService {
	
	/**
	 * 
	 * @param trace
	 * @return
	 */
	public boolean saveDescription(IComputedTrace trace);
	
	
	/**
	 * 
	 * @param storedTrace
	 * @param begin
	 * @param end
	 * @return
	 */
	public Collection<IObsel> listObsels(IComputedTrace computedTrace, long begin, long end);
	
	/**
	 * 
	 * @param storedTrace
	 * @param minb
	 * @param maxb
	 * @param mine
	 * @param maxe
	 * @return
	 */
	public Collection<IObsel> listObsels(IComputedTrace computedTrace, long minb, long maxb, long mine, long maxe);
	
}
