package org.liris.ktbs.core.api.share;

import java.util.Iterator;

/**
 * An interface for a KTBS resource that can contain other KTBS resources.
 * 
 * @author Damien Cram
 *
 */
public interface ResourceContainer<T extends KtbsResource> {
	
	/**
	 * Give a KTBS resource that is contained in this resource.
	 * 
	 * @param resourceURI the uri (either absolute or relative to the container's uri) 
	 * of the contained KTBS resource 
	 * @return the KTBS resource of that uri if any, null otherwise
	 */
	public T get(String resourceURI);
	
	/**
	 * Remove a KTBS resource from this container.
	 * 
	 * @param resourceURI the uri (either absolute or relative to the container's uri) 
	 * of the contained KTBS resource 
	 * @return true if a KTBS resource has been removed, false otherwise
	 */
	public boolean delete(String resourceURI);
	
	/**
	 * List all resource of this container.
	 * 
	 * @return an iterator on contained resources
	 */
	public Iterator<T> listResources();
	
}
