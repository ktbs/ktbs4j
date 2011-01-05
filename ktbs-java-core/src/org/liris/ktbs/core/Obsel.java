package org.liris.ktbs.core;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public interface Obsel extends KtbsResource {
	public Trace getTrace();
	
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

	public ObselType getObselType();

	public String getSubject();
	

	/*
	 * Methods related to obsel attribute
	 */
	public Map<AttributeType,Object> listAttributes();
	public Object getAttributeValue(AttributeType attribute);
	

	/*
	 * Methods related to inter-obsel relations
	 */
	public void addOutgoingRelation(Relation relation);
	public void addIncomingRelation(Relation relation);
	public Iterator<Relation> listIncomingRelations();
	public Iterator<Relation> listOutgoingRelations();
	public Obsel getTargetObsel(String relationName);
	public Obsel getSourceObsel(String relationName);
}
