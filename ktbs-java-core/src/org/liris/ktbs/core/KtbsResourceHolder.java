package org.liris.ktbs.core;


public interface KtbsResourceHolder {
	
	public boolean exists(String uri);
	public <T extends KtbsResource> T getAfterCheck(String uri, Class<T> clazz);
	public <T extends KtbsResource> T getResource(String uri, Class<T> clazz);
	public  <T extends KtbsResource> boolean existsOfType(String uri, Class<T> class1);
	
	/**
	 * Registers a Ktbs resource to this holder and removes any resource
	 * with the same uri if any.
	 * <p>
	 * The URI of the resource must be provided.
	 * </p>
	 * @param resource
	 * @return the uri of the created resource
	 */
	public <T extends KtbsResource> T putResource(T resource);

	public void removeResource(String obselURI);
}
