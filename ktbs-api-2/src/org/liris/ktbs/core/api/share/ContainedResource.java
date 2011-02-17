package org.liris.ktbs.core.api.share;

/**
 * An interface for KTBS resources that can be contained into another KTBS resource.
 * 
 * @author Damien Cram
 *
 */
public interface ContainedResource {

	/**
	 * Give the local name of the KTBS resource.
	 * 
	 * @return the local name 
	 */
	public String getLocalName();
	
	/**
	 * Give the KTBS resource that contains this resource.
	 * 
	 * @return the parent resource
	 */
	public KtbsResource getParentResource();
}
