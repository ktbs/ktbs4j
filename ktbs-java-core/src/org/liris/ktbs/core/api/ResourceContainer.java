package org.liris.ktbs.core.api;

/**
 * An interface for a KTBS resource that can contain other KTBS resources.
 * 
 * @author Damien Cram
 *
 */
public interface ResourceContainer {
	
	/**
	 * Give a KTBS resource that is contained in this resource.
	 * 
	 * @param resourceURI the uri of the contained KTBS resource 
	 * @return the KTBS resource of that uri if any, null otherwise
	 */
	public KtbsResource get(String resourceURI);
}
