package org.liris.ktbs.core;

import java.util.Iterator;

public interface ObselType extends KtbsResource {
	public TraceModel getTraceModel();
	
	public boolean hasSuperType(ObselType type, Mode mode);
	
	public ObselType getSuperObselType();
	public void setSuperObselType(ObselType type);
	
	/**
	 * List all attribute types that are available for this obsel type.
	 * @param mode indicates if the attribute types of obsel super types should
	 * be recursively inferred.
	 * @return an iterator on the attribute types
	 * @throws {@link InferenceNotAvailableException} when no inference is possible
	 * in the context of use
	 */
	public Iterator<AttributeType> listAttributes(Mode mode);
	
	/**
	 * List all outgoing relation types that are available for this obsel type.
	 * 
	 * @param mode indicates if the outgoing relation types of obsel super types should
	 * be recursively inferred.
	 * @return an iterator on the outgoing relation types
	 * @throws {@link InferenceNotAvailableException} when no inference is possible
	 * in the context of use
	 */
	public Iterator<RelationType> listOutgoingRelations(Mode mode);

	/**
	 * List all incoming relation types types that are available for this obsel type.
	 * 
	 * @param mode indicates if the incoming relation types of the obsel super types should
	 * be recursively inferred.
	 * @return an iterator on the incoming relation types
	 * @throws {@link InferenceNotAvailableException} when no inference is possible
	 * in the context of use
	 */
	public Iterator<RelationType> listIncomingRelations(Mode mode);
}
