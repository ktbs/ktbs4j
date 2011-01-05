package org.liris.ktbs.core;

import java.util.Date;
import java.util.Iterator;

public interface Trace extends KtbsResource, ResourceContainer {
	
	public Iterator<Obsel> listObsels();
	public Iterator<Obsel> listObsels(long begin, long end);
	
	public Iterator<Trace> listSources();
	public Iterator<Trace> listTransformedTraces();
	
	public String getOrigin();
	
	/**
	 * 
	 * @return
	 * @throws TemporalDomainException
	 */
	public Date getOriginAsDate();
	
	public Obsel getObsel(String obselURI);
	
	public void removeObsel(String obselURI);

	public Base getBase();
	public TraceModel getTraceModel();
	public boolean isCompliantWithModel();
}
