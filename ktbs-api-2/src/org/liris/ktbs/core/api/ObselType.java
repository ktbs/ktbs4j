package org.liris.ktbs.core.api;

import java.util.Collection;
import java.util.Iterator;

import org.liris.ktbs.core.api.share.ContainedResource;
import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.Mode;

/**
 * A KTBS obsel type.
 * 
 * @author Damien Cram
 *
 */
public interface ObselType extends KtbsResource, ContainedResource {
	
	/**
	 * Give the trace model in which this obsel type is defined.
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
	 */
	public boolean hasSuperObselType(ObselType type, Mode mode);

	
	/**
	 * Give the first super obsel type asserted found for this obsel if any.
	 * 
	 * @return the asserted super obsel type if any, null otherwise
	 */
	public ObselType getSuperObselType();

	/**
	 * Give the collection of super obsel type asserted for this obsel.
	 * 
	 * @return the collection of asserted super obsel type
	 */
	public Collection<ObselType> getSuperObselTypes();
	
	
	/**
	 * Add a new super obsel typeto this obsel.
	 * 
	 * @param type the new super obsel type
	 */
	public void addSuperObselType(ObselType type);
	
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
