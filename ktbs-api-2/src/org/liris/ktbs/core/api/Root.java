package org.liris.ktbs.core.api;

import java.util.Iterator;


/**
 * A KTBS root.
 * 
 * @author Damien Cram
 *
 */
public interface Root extends KtbsResource, ResourceContainer<Base> {
	
	/**
	 * List all KTBS bases that are owned by this root.
	 * 
	 * @return an iterator on all owned bases
	 */
	public Iterator<Base> listBases();
	
	/**
	 * Create a new base in this root.
	 * 
	 * @param baseLocalName the local name of the create base
	 * @param owner the user id of the owner of this base
	 */
	public Base newBase(String baseLocalName, String owner);
	
	/**
	 * Give the owned base of a given uri.
	 * 
	 * @param baseURI the uri (either absolute or relative to this 
	 * root) of the owned base to be returned
	 * 
	 * @return the owned base of that uri if any, null otherwise
	 */
	public Base getBase(String baseURI);
}
