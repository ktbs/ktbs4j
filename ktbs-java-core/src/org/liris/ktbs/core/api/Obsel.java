package org.liris.ktbs.core.api;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;

import org.liris.ktbs.core.DomainException;
import org.liris.ktbs.core.RangeException;
import org.liris.ktbs.core.ResourceNotFoundException;
import org.liris.ktbs.core.TemporalDomainException;

/**
 * A KTBS obsel.
 * 
 * @author Damien Cram
 *
 */
public interface Obsel extends KtbsResource {

	/**
	 * Give the parent trace in which this obsel is contained.
	 * 
	 * @return the trace
	 */
	public Trace getTrace();

	/**
	 * Give the value of the "ktbs:hasBegin" property as a Java {@link BigInteger}.
	 * 
	 * @return the begin date if any, null otherwise
	 */
	public BigInteger getBegin();

	/**
	 * Replace any existing value for the property "ktbs:hasBegin", i.e. the relative begin date, with a new
	 * one.
	 * 
	 * @param begin the new begin date as a Java {@link BigInteger}
	 */
	public void setBegin(BigInteger begin);

	/**
	 * Give the value of the "ktbs:hasEnd" property as a Java {@link BigInteger}.
	 * 
	 * @return the end date if any, null otherwise
	 */
	public BigInteger getEnd();

	/**
	 * Replace any existing value for the property "ktbs:hasEnd", i.e. the relative end date, with a new
	 * one.
	 * 
	 * @param end the new end date as a Java {@link BigInteger}
	 */
	public void setEnd(BigInteger end);

	/**
	 * Give the value of the "ktbs:hasBeginDT" property, i.e. the absolute begin date.
	 * 
	 * @return the begin date if any, null otherwise
	 */
	public String getBeginDT();

	/**
	 * Give the value of the "ktbs:hasEndDT" property, i.e. the absolute end date.
	 * 
	 * @return the end date if any, null otherwise
	 */
	public String getEndDT();

	/**
	 * Replace any existing value for the property "ktbs:hasBeginDT", i.e. the absolute begin date,  with a new
	 * one.
	 * 
	 * @param beginDT the new absolute date
	 */
	public void setBeginDT(String beginDT);

	/**
	 * Replace any existing value for the property "ktbs:hasEndDT", i.e. the absolute end date,  with a new
	 * one.
	 * 
	 * @param endDT the new absolute end date
	 */
	public void setEndDT(String endDT);

	/**
	 * Give the value of the "ktbs:hasBeginDT" property, i.e. the absolute begin date, 
	 * as a Java {@link Date} if possible.
	 * 
	 * @return the absolute begin date if any, null otherwise
	 * @throws TemporalDomainException when the temporal domain of the parent trace does not accept
	 * dates as possible temporal values
	 */
	public Date getBeginDTAsDate();


	/**
	 * Give the value of the "ktbs:hasEndDT" property, i.e. the absolute end date, 
	 * as a Java {@link Date} if possible.
	 * 
	 * @return the absolute end date if any, null otherwise
	 * @throws TemporalDomainException when the temporal domain of the parent trace does not accept
	 * dates as possible temporal values
	 */
	public Date getEndDTAsDate();

	/**
	 * Replace any existing value for the property "ktbs:hasBeginDT", i.e. the absolute begin date,  with a new
	 * one as a Java {@link Date}.
	 * 
	 * @param date the new absolute begin date
	 * @throws TemporalDomainException when the temporal domain of the parent trace does not accept
	 * dates as possible temporal values
	 */
	public void setBeginDTAsDate(Date date);

	/**
	 * Replace any existing value for the property "ktbs:hasEndDT", i.e. the absolute end date,  with a new
	 * one as a Java {@link Date}.
	 * 
	 * @param date the new absolute end date
	 * @throws TemporalDomainException when the temporal domain of the parent trace does not accept
	 * dates as possible temporal values
	 */
	public void setEndDTAsDate(Date date);

	/**
	 * Give the obsel type of this obsel
	 * 
	 * @return the obsel type
	 */
	public ObselType getObselType();

	/**
	 * Set a new obsel type to this obsel.
	 * 
	 * @param type the new obsel type if any, null otherwise
	 */
	public void setObselType(ObselType type);

	/**
	 * Give the obsel that has been used has a source by a transformation processor to produce this obsel.
	 * 
	 * @return the source obsel if any, null otherwise
	 */
	public Obsel getSourceObsel();

	/**
	 * Give the subject of this obsel, i.e. the name/id of the person at the source of this obsel.
	 * 
	 * @return the subject if any defined, null otherwise
	 */
	public String getSubject();

	/**
	 * Replace any already defined subject on this obsel with a new one.
	 * 
	 * @param subject the new subject
	 */
	public void setSubject(String subject);


	/*
	 * Methods related to obsel attribute
	 */
	/**
	 * Returns all attributes of this obsel in the form of an iterator over a collection of {@link AttributeStatement}.
	 * 
	 * <p>
	 *  The attribute statements returned are the RDF triples whose predicates are defined
	 *  as attribute types in the trace model. If the trace model resource returned by
	 *  the method {@link ObselType#getTraceModel()} on the obsel type of this obsel is not accessible, 
	 *  the method fails with a {@link ResourceNotFoundException}.
	 * </p>
	 * 
	 * <p>
	 * Attributes defined on this obsel with no associated attribute type defined in the trace model
	 * are still accessible via the raw triple access method {@link KtbsResource#listNonKtbsStatements()}.
	 * </p>
	 * 
	 * @throws ResourceNotFoundException when the obsel cannot access
	 * the {@link TraceModel} whose obsel type belongs to.
	 */
	public Iterator<AttributeStatement> listAttributes();

	/**
	 * Give the value of a given attribute that is set on this obsel.
	 * 
	 * @param attribute the attribute type
	 * @return the value of that attribute if any attribute of that type is set, null otherwise
	 * @throws ResourceNotFoundException when the obsel cannot access
	 * the {@link TraceModel} whose obsel type belongs to.
	 */
	public Object getAttributeValue(AttributeType attribute);

	/**
	 * Add a new attribute to this obsel and remove any existing 
	 * attribute statement of the same attribute type.
	 * 
	 * @param attribute the attribiute type of the new attribute
	 * @param value the value of the attribute
	 * @throws ResourceNotFoundException when the obsel cannot access
	 * the {@link TraceModel} whose obsel type belongs to.
	 */

	public void addAttribute(AttributeType attribute, Object value);

	/*
	 * Methods related to inter-obsel relations
	 */
	/**
	 * Add a new outgoing relation to this obsel.
	 * 
	 * @param relationType the relation type of the new relation
	 * @param target the target obsel of the new relation
	 * @throws RangeException when the relation type does not 
	 * allow an obsel of that type as a target obsel of this relation
	 * 
	 */
	public void addOutgoingRelation(RelationType relationType, Obsel target);

	/**
	 * Add a new incoming relation to this obsel.
	 * 
	 * @param source the source obsel of the new relation
	 * @param relationType the relation type of the new relation
	 * @throws DomainException when the relation type does not 
	 * allow an obsel of that type as a source obsel of this relation
	 */
	public void addIncomingRelation(Obsel source, RelationType relationType);

	/**
	 * List all incoming relations defined on this obsel.
	 * 
	 * @return an iterator on incoming relation statements
	 *  @throws ResourceNotFoundException when the obsel cannot access
	 * the {@link TraceModel} whose obsel type belongs to.
	 */
	public Iterator<RelationStatement> listIncomingRelations();

	/**
	 * List all outgoing relations defined on this obsel.
	 * 
	 * @return an iterator on outgoing relation statements
	 *  @throws ResourceNotFoundException when the obsel cannot access
	 * the {@link TraceModel} whose obsel type belongs to.
	 */
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
