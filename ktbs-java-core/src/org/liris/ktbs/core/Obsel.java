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

	public String getBeginDT();
	public String getEndDT();

	public Date getBeginDTAsDate();
	public Date getEndDTAsDate();

	public ObselType getObselType();

	public String getSubject();
	

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
