package org.liris.ktbs.core.api.share;

import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.RelationType;


/**
 * An inter-obsel relation between two obsels inside a KTBS.
 * 
 * @author Damien Cram
 * @see AttributePair
 */
public interface RelationStatement {
	
	/**
	 * Give the source obsel of this relation.
	 * 
	 * @return the source obsel
	 */
	public Obsel getFromObsel();
	
	/**
	 * Give the relation type of this relation.
	 * 
	 * @return the relation type
	 */
	public RelationType getRelation();
	
	/**
	 * Give the target obsel of this relation.
	 * 
	 * @return the target obsel
	 */
	public Obsel getToObsel();
}
