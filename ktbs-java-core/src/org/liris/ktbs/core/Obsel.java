package org.liris.ktbs.core;

import java.util.Date;
import java.util.Iterator;

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
	public void setBeginDTAsDate(Date date);
	public void setEndDTAsDate(Date date);

	public ObselType getObselType();
	public void setObselType(ObselType type);

	public Obsel getSourceObsel();
	
//	Only for a KTBS server purpose
//	public void setSourceObsel(Obsel obsel);

	public String getSubject();
	public void setSubject(String subject);
	

	/*
	 * Methods related to obsel attribute
	 */
	/**
	 * Returns all attributes of this obsel in the form of an iterator over a collection of {@link AttributeStatement}.
	 * <p>
	 *  The attribute statements returned are the RDF triples whose predicates are defined
	 *  as attribute types in the trace model. If the trace model resource returned by
	 *  {@link #getObselType().getTraceModel()} is not accessible, the method fails with 
	 *  a {@link ResourceNotFoundException}.
	 * </p>
	 * <p>
	 * Attributes defined on this obsel with no associated attribute type defined in the trace model
	 * are still accessible via the raw triple access method {@link KtbsResource#listNonKtbsStatements()}.
	 * </p>
	 * 
	 * @throws ResourceNotFoundException when the obsel cannot access
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
	
	/**
	 * Get the first obsel encountered that is the target of a relation
	 * 
	 * @param relationType the relation type of the relation
	 * @return the first target obsel encountered for that relation, null if none
	 */
	public Obsel getTargetObsel(RelationType relationType);
	
	/**
	 * Get the first obsel encountered that has an outgoing relation
	 * pointing to this obsel
	 * 
	 * @param relationType the relation type of the relation
	 * @return the first source obsel encountered for that relation, null if none
	 */
	public Obsel getSourceObsel(RelationType relationType);
}
