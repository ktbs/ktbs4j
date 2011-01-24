package org.liris.ktbs.core.api;

import java.util.Iterator;

/**
 * A KTBS root.
 * 
 * @author Damien Cram
 *
 */
public interface Root extends KtbsResource, ResourceContainer {
	
	/**
	 * List all KTBS bases that are owned by this root.
	 * 
	 * @return an iterator on all owned bases
	 */
	public Iterator<Base> listBases();
	
	/**
	 * Add a new owned base to this root.
	 * 
	 * @param base the new owned base
	 */
	public void addBase(Base base);
	
	/**
	 * Give the owned base of a given uri.
	 * 
	 * @param baseURI the uri of the owned base to be returned
	 * @return the owned base of that uri if any, null otherwise
	 */
	public Base getBase(String baseURI);
}
