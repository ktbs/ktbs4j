package org.liris.ktbs.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface Obsel extends KtbsResource {
	
	
	public Date getBegin();
	public Date getEnd();

	public String getTypeURI();

	public String getSubject();
	
	public void addOutgoingRelation(Relation relation);
	public void addIncomingRelation(Relation relation);
	public Collection<Relation> getIncomingRelations();
	public Collection<Relation> getOutgoingRelations();

	public Map<String,Serializable> getAttributes();
	public Serializable getAttributeValue(String attributeName);
	
	public Trace getTrace();
	public String getTraceURI();
	
	public Obsel getTargetObsel(String relationName);
	public Obsel getSourceObsel(String relationName);
}
