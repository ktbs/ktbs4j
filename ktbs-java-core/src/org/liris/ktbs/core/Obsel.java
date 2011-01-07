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
	public void setBegin(long begin);

	/**
	 * 
	 * @return the relative end date in milliseconds, -1 if unset
	 */
	public long getEnd();
	public void setEnd(long end);

	public String getBeginDT();
	public String getEndDT();
	public void setBeginDT(String beginDT);
	public void setEndDT(String endDT);

	public Date getBeginDTAsDate();
	public Date getEndDTAsDate();

	public ObselType getObselType();
	public void setObselType(ObselType type);

	public Obsel getSourceObsel();
	public void setSourceObsel(Obsel obsel);

	public String getSubject();
	public void setSubject(String subject);
	

	/*
	 * Methods related to obsel attribute
	 */
	/**
	 * Returns all attributes of this obsel in the form of {@link AttributeStatement}. The 
	 * attribute statement returned are the RDF triples whose predicates take their name in the 
	 * namespace defined by the trace model of this obsel type, and whose object are literals.
	 * 
	 * <p>
	 * The URI of the trace model is required to interprete the RDF predicate URIs as 
	 * attributes. This method fails with {@link KtbsResourceNotFoundException} when it 
	 * cannot access the trace model URI.
	 * </p>
	 * 
	 * @throws KtbsResourceNotFoundException when the obsel cannot access
	 * the {@link TraceModel} whose obsel type belongs to.
	 */
	public Iterator<AttributeStatement> listAttributes();
	public Object getAttributeValue(AttributeType attribute);
	public void addAttribute(AttributeType attribute, Object value);
	

	/*
	 * Methods related to inter-obsel relations
	 */
	public void addOutgoingRelation(RelationType relationType, Obsel target);
	public void addIncomingRelation(Obsel source, RelationType relationType);
	public Iterator<RelationStatement> listIncomingRelations();
	public Iterator<RelationStatement> listOutgoingRelations();
	public Obsel getTargetObsel(String relationName);
	public Obsel getSourceObsel(String relationName);
}
