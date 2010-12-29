package org.liris.ktbs.core;

import java.util.Collection;
import java.util.Date;

public interface Trace extends KtbsResource {
	
	/**
	 * 
	 * @return the unmodifiable collection of all obsels contained in this trace.
	 */
	public Collection<Obsel> getObsels();
	public Collection<String> getObselURIs();
	
	public Date getOrigin();
	
	public void addObsel(Obsel obsel);
	public Obsel getObsel(String obselURI);
	
	public void removeObsel(String obselURI);

	public String getTraceModelUri();

	public Base getBase();
	public String getBaseURI();

	public boolean isCompliantWithModel();
}
