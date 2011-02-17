package org.liris.ktbs.core.api;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.api.share.BuiltinMethod;
import org.liris.ktbs.core.api.share.ContainedResource;
import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.TransformationResource;


/**
 * A KTBS method.
 * 
 * @author Damien Cram
 *
 */
public interface Method extends KtbsResource, TransformationResource, ContainedResource {
	
	/**
	 * The built-in method named "ktbs:filter" in the KTBS.
	 */
	public static final Method FILTER = new BuiltinMethod(KtbsConstants.FILTER); 

	/**
	 * The built-in method named "ktbs:fusion" in the KTBS.
	 */
	public static final Method FUSION = new BuiltinMethod(KtbsConstants.FUSION); 

	/**
	 * The built-in method named "ktbs:script-python" in the KTBS.
	 */
	public static final Method SCRIPT_PYTHON = new BuiltinMethod(KtbsConstants.SCRIPT_PYTHON); 

	/**
	 * The built-in method named "ktbs:sparql" in the KTBS.
	 */
	public static final Method SPARQL = new BuiltinMethod(KtbsConstants.SCRIPT_PYTHON); 
	
	/**
	 * Give the inherited method of this method if any.
	 * 
	 * @return the inherited method, null if none
	 */
	public Method getInheritedMethod();
	
	/**
	 * Set a new inherited method for this method.
	 * 
	 * @param method the new inherited method
	 */
	public void setInheritedMethod(Method method);
	
	/**
	 * Give the method ETag value.
	 * 
	 * @return the method etag value as a string, null otherwise
	 */
	public String getETag();

}
