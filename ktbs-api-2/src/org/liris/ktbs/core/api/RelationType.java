package org.liris.ktbs.core.api;

import java.util.Iterator;


/**
 * A KTBS relation type.
 * 
 * @author Damien Cram
 *
 */
public interface RelationType extends KtbsResource, WithDomainResource<ObselType>, WithRangeResource<ObselType>  {
	
	/**
	 * Tell if a relation type is a super relation type of this relation type.
	 * 
	 * @param type the candidate super relation type
	 * @param mode should use inference or not
	 * @return true if the param relation type is a super type of 
	 * this relation type for the requested exploration mode, false otherwise.
	 */
	public boolean hasSuperRelationType(RelationType type, Mode mode);
	
	
	/**
	 * Give the first asserted super relation type found for this relation type.
	 * 
	 * @return the first asserted super relation type found for 
	 * this relation type if any, null otherwise
	 */
	public RelationType getSuperRelationType();
	
	/**
	 * Give the collection of asserted super relation types for this relation type.
	 * 
	 * @return the collection of asserted super relation types for this relation type
	 */
	public Iterator<RelationType> listSuperRelationTypes();
	
	/**
	 * Add a super relation type assertion to this relation type.
	 * 
	 * @param superRelationType the super relation type to be added
	 */
	public void addSuperRelationType(RelationType superRelationType);
}
