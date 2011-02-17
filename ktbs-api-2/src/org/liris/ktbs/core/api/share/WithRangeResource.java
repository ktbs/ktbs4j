package org.liris.ktbs.core.api.share;

import java.util.Collection;

/**
 * An interface that defined an attribute or a relation's behavior 
 * regarding the definition of its range.
 * 
 * @author Damien Cram
 *
 * @param <T> the class of range definition
 */
public interface WithRangeResource<T> {
	
	/**
	 * Add an asserted range to this attribute/relation type
	 * 
	 * @param range the range element to add
	 */
	public void addRange(T range);
	
	/**
	 * Get all the collection of asserted ranges for this attribute/relation type
	 * 
	 * @return an iterator on the ranges
	 */
	public Collection<T> getRanges();
	
	/**
	 * Get the first asserted range found for this attribute/relation type
	 * 
	 * @return the first range found,  null if none.
	 */
	public T getRange();
}
