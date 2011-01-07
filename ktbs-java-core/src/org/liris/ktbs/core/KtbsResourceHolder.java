package org.liris.ktbs.core;


public interface KtbsResourceHolder {
	
	public boolean exists(String uri);
	public <T extends KtbsResource> T getResource(String uri, Class<T> clazz);
	
	
}
