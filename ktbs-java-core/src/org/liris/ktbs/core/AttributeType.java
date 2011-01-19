package org.liris.ktbs.core;

import java.util.Iterator;

public interface AttributeType extends KtbsResource {
	
	/**
	 * Add a declared domain to this obsel type
	 * @param domain
	 */
	public void addDomain(ObselType domain);
	
	/**
	 * Get all the domain declared for this attribute type
	 * @return an iterator on the domains
	 */
	public Iterator<ObselType> listDomains();
	
	/**
	 * Get the declared domain for this obsel type
	 * @return the first domain declared in the underlying RDF triples,
	 * null if none.
	 */
	public ObselType getDomain();
}
