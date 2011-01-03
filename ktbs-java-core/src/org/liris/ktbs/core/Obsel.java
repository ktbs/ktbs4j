package org.liris.ktbs.core;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface Obsel extends KtbsResource {
	
	/**
	 * 
	 * @return the relative begin date in milliseconds, -1 if unset
	 */
	public long getBegin();

	/**
	 * 
	 * @return the relative end date in milliseconds, -1 if unset
	 */
	public long getEnd();

	public Date getBeginDT();
	public Date getEndDT();

	public String getTypeURI();

	public String getSubject();
	
	public void addOutgoingRelation(Relation relation);
	public void addIncomingRelation(Relation relation);
	public Collection<Relation> getIncomingRelations();
	public Collection<Relation> getOutgoingRelations();

	public Map<String,Object> getAttributes();
	public Object getAttributeValue(String attributeName);
	
	public Trace getTrace();
	public String getTraceURI();
	
	public Obsel getTargetObsel(String relationName);
	public Obsel getSourceObsel(String relationName);
}
