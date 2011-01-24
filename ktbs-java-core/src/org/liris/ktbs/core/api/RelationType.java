package org.liris.ktbs.core.api;

import java.util.Iterator;


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
	
	public RelationType getSuperRelationType();
	
	public ObselType[] getDomainsInferred();
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
	
	public void setSuperRelationType(RelationType superRelationType);
}
