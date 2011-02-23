package org.liris.ktbs.core.api;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;


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
	 * Set a new absolute begin date for this obsel.
	 * 
	 * @param beginDT the new absolute begin date
	 */
	public void setBeginDT(String beginDT);

	/**
	 * Set a new absolute end date for this obsel.
	 * 
	 * @param endDT the new absolute end date
	 */
	public void setEndDT(String endDT);

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
	 * Give the obsels that has been used has the source obsels by a 
	 * transformation processor to produce this obsel.
	 * 
	 * @return the source obsel set
	 */
	public Iterator<Obsel> listTransformationSourceObsels();

	
	/**
	 * Give the subject of this obsel, i.e. the user id of the subject 
	 * who is at the origin of this obsel.
	 * 
	 * @return the subject if any defined, null otherwise
	 */
	public String getSubject();

	/**
	 * Set a new subject to this obsel.
	 * 
	 * @param subject the user id of the new subject
	 */
	public void setSubject(String subject);


	/**
	 * List all attribute pairs that are defined in this obsel.
	 * 
	 * @return an iterator on this AttributeType/value pairs
	 */
	public Iterator<AttributePair> listAttributes();

	/**
	 * Give the first value found in this obsel that is mapped to a given attribute type.
	 * 
	 * @param attribute the attribute type
	 * @return the value of that attribute if any attribute of that type is set, null otherwise
	 */
	public Object getAttributeValue(AttributeType attribute);

	/**
	 * Give all the values set in this obsel for this attribute type.
	 * 
	 * @param attribute the attribute type
	 * @return the collection of values for that attribute 
	 */
	public Collection<Object> getAttributeValues(AttributeType attribute);

	/**
	 * Search in the obsel type the {@link AttributeType} object of that uri and give 
	 * the first value found in this obsel that is mapped to the AttributeType.
	 * 
	 * @param attributeTypeURI the attribute type uri
	 * @return the value of that attribute if any attribute of that type is set, null otherwise
	 */
	public Object getAttributeValue(String attributeTypeURI);
	
	/**
	 * Search in the obsel type the {@link AttributeType} object of that 
	 * uri and give all the values set in this obsel for that attribute type.
	 * 
	 * @param attributeTypeURI the attribute type uri
	 * @return the collection of values for that attribute 
	 */
	public Collection<Object> getAttributeValues(String attributeTypeURI);
	
	
	/**
	 * Add a new attribute to this obsel. If any attribute of the same type is 
	 * already set to this obsel, the method puts an additionnal AttributeType/value
	 * pair to this obsel and leaves the existing pairs unchanged.
	 * 
	 * @param attribute the attribute type of the new attribute
	 * @param value the value of the attribute
	 */
	public void addAttribute(AttributeType attribute, Object value);

	/**
	 * Search in the obsel type the {@link AttributeType} object of that 
	 * uri and add a new attribute to this obsel. If any attribute of the same type is 
	 * already set to this obsel, the method puts an additionnal AttributeType/value
	 * pair to this obsel and leaves the existing pairs unchanged.
	 * 
	 * @param attributeTypeURI the attribute type of the new attribute
	 * @param value the value of the attribute
	 */
	public void addAttribute(String attributeTypeURI, Object value);
	

	/*
	 * Methods related to inter-obsel relations
	 */
	/**
	 * Add a new outgoing relation to this obsel.
	 * 
	 * @param relationType the relation type of the new relation
	 * @param target the target obsel of the new relation
	 */
	public void addOutgoingRelation(RelationType relationType, Obsel target);

	/**
	 * Add a new incoming relation to this obsel.
	 * 
	 * @param source the source obsel of the new relation
	 * @param relationType the relation type of the new relation
	 */
	public void addIncomingRelation(Obsel source, RelationType relationType);

	/**
	 * List all incoming relations defined on this obsel.
	 * 
	 * @return an iterator on incoming relation statements
	 */
	public Iterator<RelationStatement> listIncomingRelations();

	/**
	 * List all outgoing relations defined on this obsel.
	 * 
	 * @return an iterator on outgoing relation statements
	 */
	public Iterator<RelationStatement> listOutgoingRelations();

	/**
	 * Get the first obsel encountered that is the target of a relation of a given type.
	 * 
	 * @param relationType the relation type of the relation
	 * @return the first target obsel encountered for that relation type, null if none
	 */
	public Obsel getTargetObsel(RelationType relationType);
	
	/**
	 * Search in the obsel type the {@link RelationType} object of that 
	 * uri and get the first obsel encountered that is the target of a relation of that type.
	 * 
	 * @param relationTypeUri the relation type of the relation
	 * @return the first target obsel encountered for that relation type, null if none
	 */
	public Obsel getTargetObsel(String relationTypeUri);

	/**
	 * Get the first obsel encountered that is the source of a relation of a given type.
	 * 
	 * @param relationType the relation type of the relation
	 * @return the first source obsel encountered for that relation type, null if none
	 */
	public Obsel getSourceObsel(RelationType relationType);
	
	/**
	 * Search in the obsel type the {@link RelationType} object of that 
	 * uri and get the first obsel encountered that is the target of a relation of that type.
	 * 
	 * @param relationTypeUri the relation type of the relation
	 * @return the first target obsel encountered for that relation, null if none
	 */
	public Obsel getSourceObsel(String relationTypeUri);
	
	
	/**
	 * Get the collection of obsels that are the targets of a given relation type for this obsel.
	 * 
	 * @param relationType the relation type
	 * @return the collection of target obsels
	 */
	public Collection<Obsel> getTargetObsels(RelationType relationType);
	
	/**
	 * Search in the obsel type the {@link RelationType} object of that 
	 * uri and get the collection of obsels that are the targets of a that given relation type for this obsel.
	 * 
	 * @param relationTypeUri the relation type of the relation
	 * @return the collection of target obsels
	 */
	public Collection<Obsel> getTargetObsels(String relationTypeUri);
	
	/**
	 * Get the collection of obsels that are the sources of a given relation type for this obsel.
	 * 
	 * @param relationType the relation type
	 * @return the collection of source obsels
	 */
	public Collection<Obsel> getSourceObsels(RelationType relationType);
	
	/**
	 * Search in the obsel type the {@link RelationType} object of that 
	 * uri and get the collection of obsels that are the sources of a that 
	 * given relation type for this obsel.
	 * 
	 * @param relationTypeUri the relation type of the relation
	 * @return the collection of source obsels
	 */
	public Collection<Obsel> getSourceObsels(String relationTypeUri);
	
}
