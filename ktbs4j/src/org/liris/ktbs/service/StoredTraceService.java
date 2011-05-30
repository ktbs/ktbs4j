package org.liris.ktbs.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.service.impl.ObselBuilder;

/**
 * Provides convenient method for handling stored traces, 
 * collecting traces, and collecting traces with buffers.
 * 
 * @author Damien Cram
 * @see ResourceService
 */
public interface StoredTraceService extends IRootAwareService {
	
	/**
	 * Creates a new stored trace in a given base with the system 
	 * current time as the origin.
	 * 
	 * @param baseUri
	 * @return
	 */
	public String newStoredTrace(String baseUri, String traceModelUri, String defaultSubject);

	/**
	 * 
	 * @param trace
	 * @return
	 */
	public boolean saveDescription(IStoredTrace trace);
	
	/**
	 * 
	 * @param trace
	 * @return
	 */
	public boolean saveObsels(IStoredTrace trace);
	
	/**
	 * 
	 * @param trace
	 * @return
	 */
	public ObselBuilder newObselBuilder(IStoredTrace trace);

	/**
	 * 
	 * @param storedTrace
	 * @param obselLocalName
	 * @param typeUri
	 * @param beginDT
	 * @param endDT
	 * @param begin
	 * @param end
	 * @param subject
	 * @param attributes
	 * @return
	 */
	public String newObsel(
			IStoredTrace storedTrace,
			String obselLocalName,
			String typeUri,
			String beginDT,
			String endDT,
			BigInteger begin,
			BigInteger end,
			String subject,
			Set<IAttributePair> attributes
			);
	
	/**
	 * 
	 * @param storedTrace
	 * @param typeUri
	 * @param begin
	 * @param attributes
	 * @return
	 */
	public String newObsel(
			IStoredTrace storedTrace,
			String typeUri,
			long begin,
			Set<IAttributePair> attributes
	);
	
	/**
	 * 
	 * @param storedTrace
	 * @param typeUri
	 * @param begin
	 * @return
	 */
	public String newObsel(
			IStoredTrace storedTrace,
			String typeUri,
			long begin
	);

	
	/**
	 * 
	 * @param storedTrace
	 * @param begin
	 * @param end
	 * @return
	 */
	public Collection<IObsel> listObsels(IStoredTrace storedTrace, long begin, long end);
	
	/**
	 * 
	 * @param storedTrace
	 * @param minb
	 * @param maxb
	 * @param mine
	 * @param maxe
	 * @return
	 */
	public Collection<IObsel> listObsels(IStoredTrace storedTrace, long minb, long maxb, long mine, long maxe);

	/**
	 * 
	 * @param trace
	 */
	void startBufferedCollect(IStoredTrace trace);

	/**
	 * 
	 * @param trace
	 */
	void postBufferedObsels(IStoredTrace trace);

	/**
	 * 
	 * @param storedTraceUri
	 * @return
	 */
	public ObselBuilder newObselBuilder(String storedTraceUri);

	/**
	 * 
	 * @param trace
	 * @param typeUri
	 * @param begin
	 * @param attributes
	 * @return
	 */
	public String newObsel(IStoredTrace trace, String typeUri, long begin,
			Object[] attributes);
	
	
}
