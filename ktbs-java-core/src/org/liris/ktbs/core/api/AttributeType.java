package org.liris.ktbs.core.api;

import java.util.Iterator;

/**
 * A KTBS attribute type.
 * 
 * @author Damien Cram
 *
 */
public interface AttributeType extends KtbsResource {
	
	/**
	 * Add a declared domain to this attribute type
	 * @param domain
	 */
	public void addDomain(ObselType domain);
	
	/**
	 * Get all the domain declared for this attribute type
	 * @return an iterator on the domains
	 */
	public Iterator<ObselType> listDomains();
	
	/**
	 * Get the declared domain for this attribute type
	 * @return the first domain found in the underlying RDF triples,
	 * null if none.
	 */
	public ObselType getDomain();
}
