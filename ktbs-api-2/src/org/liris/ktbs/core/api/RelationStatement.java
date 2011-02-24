package org.liris.ktbs.core.api;

import org.liris.ktbs.core.pojo.ObselPojo;
import org.liris.ktbs.core.pojo.RelationTypePojo;



/**
 * An inter-obsel relation between two obsels inside a KTBS.
 * 
 * @author Damien Cram
 * @see AttributeStatement
 */
public interface RelationStatement {
	
	/**
	 * Give the source obsel of this relation.
	 * 
	 * @return the source obsel
	 */
	public ObselPojo getFromObsel();
	
	/**
	 * Give the relation type of this relation.
	 * 
	 * @return the relation type
	 */
	public RelationTypePojo getRelation();
	
	/**
	 * Give the target obsel of this relation.
	 * 
	 * @return the target obsel
	 */
	public ObselPojo getToObsel();
}
