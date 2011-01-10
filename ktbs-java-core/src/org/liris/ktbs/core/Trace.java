package org.liris.ktbs.core;

import java.util.Date;
import java.util.Iterator;

public interface Trace extends KtbsResource, ResourceContainer {
	
	public Iterator<Obsel> listObsels();
	public Iterator<Obsel> listObsels(long begin, long end);
	
	
	public Iterator<Trace> listTransformedTraces();
	
	public String getOrigin();
	public void setOrigin(String origin);
	public void setOriginAsDate(Date origin);
	
	/**
	 * 
	 * @return
	 * @throws TemporalDomainException
	 */
	public Date getOriginAsDate();
	
	public Obsel getObsel(String obselURI);
	

	public Base getBase();
	public TraceModel getTraceModel();
	public void setTraceModel(TraceModel traceModel);
	public boolean isCompliantWithModel();
	public void setCompliantWithModel(boolean compliant);
}
