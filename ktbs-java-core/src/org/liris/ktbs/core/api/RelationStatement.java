package org.liris.ktbs.core.api;


/**
 * An inter-obsel relation between two {@link Obsel} inside a KTBS.
 * 
 * @author Damien Cram
 * @see KtbsStatement
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
