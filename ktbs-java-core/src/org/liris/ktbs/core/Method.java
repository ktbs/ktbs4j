package org.liris.ktbs.core;


public interface Method extends KtbsResource, ResourceWithParameters {
	public String getInherits();
	public String setInherits(String methodURI);
	public String getETag();

}
