package org.liris.ktbs.core.api;

import java.util.Iterator;

import org.liris.ktbs.core.InferenceException;

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
	 * @param mode should use inference or not
	 * @return true if the param obsel type is a super type of 
	 * this obsel type for the requested exploration mode, false otherwise.
	 * @throws InferenceException when there is an issue infering the type hierarchy 
	 * of the obsel type of this obsel
	 */
	public boolean hasSuperType(ObselType type, Mode mode);

	/**
	 * Infer the super types of this obsel type in the trace model.
	 * 
	 * @return an iterator on all inferred super types
	 * @throws InferenceException when there is a cycle in 
	 * the obsel type hierarchy
	 */
	public Iterator<ObselType> inferSuperTypes();
	
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
	 * @throws {@link InferenceException} when the inference of obsel type 
	 * hierarchy of this obsel type failed
	 */
	public Iterator<AttributeType> listAttributes(Mode mode);
	
	/**
	 * List all outgoing relation types that are available for this obsel type.
	 * 
	 * @param mode indicates if the outgoing relation types of obsel super types should
	 * be recursively inferred.
	 * @return an iterator on the outgoing relation types
	 * @throws {@link InferenceException} when the inference of obsel type 
	 * hierarchy of this obsel type failed
	 */
	public Iterator<RelationType> listOutgoingRelations(Mode mode);

	/**
	 * List all incoming relation types types that are available for this obsel type.
	 * 
	 * @param mode indicates if the incoming relation types of the obsel super types should
	 * be recursively inferred.
	 * @return an iterator on the incoming relation types
	 * @throws {@link InferenceException} when the inference of obsel type 
	 * hierarchy of this obsel type failed
	 */
	public Iterator<RelationType> listIncomingRelations(Mode mode);
}
