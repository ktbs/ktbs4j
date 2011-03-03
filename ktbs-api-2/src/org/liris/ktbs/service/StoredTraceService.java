package org.liris.ktbs.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;

public interface StoredTraceService {
	public boolean saveDescription(IStoredTrace trace);
	public boolean saveObsels(IStoredTrace trace);

	public IObsel newObsel(
			IStoredTrace storedTrace,
			String obselLocalName,
			String typeUri,
			String beginDT,
			String endDT,
			BigInteger begin,
			BigInteger end,
			String subject,
			Map<String, Object> attributes
			);
	
	public IObsel newObsel(
			IStoredTrace storedTrace,
			String typeUri,
			long begin,
			Map<String, Object> attributes
	);
	
	public IObsel newObsel(
			IStoredTrace storedTrace,
			String typeUri,
			long begin
	);

	public IObsel getObsel(String localName);
	public Collection<IObsel> listObsels(long begin, long end);
	public Collection<IObsel> listObsels(long minb, long maxb, long mine, long maxe);
}
