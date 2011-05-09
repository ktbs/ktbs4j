package org.liris.ktbs.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.service.impl.ObselBuilder;

/**
 * 
 * @author Damien Cram
 *
 */
public interface StoredTraceService extends IRootAwareService {
	
	/**
	 * Creates a new stored trace in a given base with the system 
	 * current time as the origin.
	 * 
	 * @param baseUri
	 * @return
	 */
	public IStoredTrace newStoredTrace(String baseUri, String traceModelUri, String defaultSubject);

	public boolean saveDescription(IStoredTrace trace);
	
	public boolean saveObsels(IStoredTrace trace);
	
	public ObselBuilder newObselBuilder(IStoredTrace trace);

	public IObsel newObsel(
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
	
	public IObsel newObsel(
			IStoredTrace storedTrace,
			String typeUri,
			long begin,
			Set<IAttributePair> attributes
	);
	
	public IObsel newObsel(
			IStoredTrace storedTrace,
			String typeUri,
			long begin
	);

	
	
	public Collection<IObsel> listObsels(IStoredTrace storedTrace, long begin, long end);
	public Collection<IObsel> listObsels(IStoredTrace storedTrace, long minb, long maxb, long mine, long maxe);

	void startBufferedCollect(IStoredTrace trace);

	void postBufferedObsels(IStoredTrace trace);

	public ObselBuilder newObselBuilder(String storedTraceUri);
	
}
