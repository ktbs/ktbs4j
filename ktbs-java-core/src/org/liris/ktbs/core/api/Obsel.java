package org.liris.ktbs.core.api;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;

import org.liris.ktbs.core.ResourceNotFoundException;

public interface Obsel extends KtbsResource {
	public Trace getTrace();
	
	public BigInteger getBegin();
	public void setBegin(BigInteger begin);

	public BigInteger getEnd();
	public void setEnd(BigInteger end);

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
	 * List all obsels that are set as the target of a relation going out of this obsel.
	 * 
	 * @param relationType the relation
	 * @return an iterator on all the target obsels of this relation
	 */
	public Iterator<Obsel> listTargetObsels(RelationType relationType);
	
	/**
	 * List all obsels that are set as the source of an incoming relation.
	 * 
	 * @param relationType the relation
	 * @return an iterator on all the source obsels of the relation
	 */
	public Iterator<Obsel> listSourceObsels(RelationType relationType);
	
	/**
	 * Get the first obsel encountered that has an outgoing relation
	 * pointing to this obsel
	 * 
	 * @param relationType the relation type of the relation
	 * @return the first source obsel encountered for that relation, null if none
	 */
	public Obsel getSourceObsel(RelationType relationType);
}
