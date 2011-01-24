package org.liris.ktbs.core.api;

import java.util.Iterator;

/**
 * A KTBS obsel type.
 * 
 * @author Damien Cram
 *
 */
public interface ObselType extends KtbsResource {
	
	/**
	 * Give the trace model in which this obsel type is defined.
	 * 
	 * <p>
	 * The trace model is found by resolving the parent uri of this
	 * obsel type.
	 * </p>
	 * 
	 * @return the parent trace model
	 */
	public TraceModel getTraceModel();
	
	/**
	 * Tell if an obsel type is a super type of this obsel type.
	 * 
	 * @param type the candidate super obsel type
	 * @param mode should use inferrence or not
	 * @return true if the param obsel type is a super type of 
	 * this obsel type for the requested exploration mode, false otherwise.
	 * 
	 */
	public boolean hasSuperType(ObselType type, Mode mode);
	
	/**
	 * Give the value of the super obsel type asserted for this obsel type.
	 * 
	 * @return the super obsel type asserted if any, null otherwise
	 */
	public ObselType getSuperObselType();
	
	/**
	 * Remove any previously defined super obsel type and set a new one.
	 * 
	 * @param type the new super obsel type
	 */
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
