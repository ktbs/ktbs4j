package org.liris.ktbs.core.api;

import org.liris.ktbs.core.BuiltinMethod;
import org.liris.ktbs.core.KtbsConstants;



public interface Method extends KtbsResource, ResourceWithParameters {
	
	public static final Method FILTER = new BuiltinMethod(KtbsConstants.FILTER); 
	public static final Method FUSION = new BuiltinMethod(KtbsConstants.FUSION); 
	
	public String getInherits();
	public void setInherits(String methodURI);
	public String getETag();

}
