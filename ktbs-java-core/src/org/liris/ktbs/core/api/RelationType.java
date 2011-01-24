package org.liris.ktbs.core.api;

import java.util.Iterator;

/**
 * A KTBS relation type.
 * 
 * @author Damien Cram
 *
 */
public interface RelationType extends KtbsResource {
	/**
	 * Get the declared domain for this relation type
	 * @return the first domain found in the underlying RDF triples,
	 * null if none.
	 */
	public ObselType getDomain();
	
	/**
	 * Get the declared range for this relation type
	 * @return the first range found in the underlying RDF triples,
	 * null if none.
	 */
	public ObselType getRange();

	/**
	 * Get all the ranges declared for this relation type
	 * @return an iterator on the domains
	 */
	public Iterator<ObselType> listRanges();

	/**
	 * Get all the domain declared for this relation type
	 * @return an iterator on the domains
	 */
	public Iterator<ObselType> listDomains();
	
	/**
	 * Give the super relation type that is 
	 * defined for this relation type.
	 * 
	 * @return the super relation type of this relation type if 
	 * any, null otherwise
	 */
	public RelationType getSuperRelationType();
	
	/**
	 * Do inference and return all obsel types that are
	 * accepted as a target obsel for this relation type.
	 * 
	 * @return all domains accepted in an array
	 */
	public ObselType[] getDomainsInferred();

	/**
	 * Do inference and return all obsel types that are
	 * accepted as a source obsel for this relation type.
	 * 
	 * @return all ranges accepted in an array
	 */
	public ObselType[] getRangesInferred();

	/**
	 * Add a declared range to the relation type.
	 * @param range
	 */
	public void addRange(ObselType range);
	
	/**
	 * Add a declared domain to the relation type.
	 * @param domain
	 */
	public void addDomain(ObselType domain);
	
	/**
	 * Remove any already defined value for the super relation type and set a new one.
	 * 
	 * @param superRelationType the new super relation type
	 */
	public void setSuperRelationType(RelationType superRelationType);
}
