package org.liris.ktbs.core.api;

import java.util.Iterator;

/**
 * An interface that defined an attribute or a relation's behavior 
 * regarding the definition of its domain.
 * 
 * @author Damien Cram
 *
 * @param <T> the class of domain definition
 */
public interface WithDomainResource<T> {
	
	/**
	 * Add an asserted domain to this attribute/relation type
	 * 
	 * @param domain the domain element to add
	 */
	public void addDomain(T domain);
	
	/**
	 * Get all the collection of asserted domains for this attribute/relation type
	 * 
	 * @return an iterator on the domains
	 */
	public Iterator<T> listDomains();
	
	/**
	 * Get the first asserted domain found for this attribute/relation type
	 * 
	 * @return the first domain found,  null if none.
	 */
	public T getDomain();
}
