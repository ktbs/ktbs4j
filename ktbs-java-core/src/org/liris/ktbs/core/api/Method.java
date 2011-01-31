package org.liris.ktbs.core.api;

import org.liris.ktbs.core.BuiltinMethod;
import org.liris.ktbs.core.KtbsConstants;


/**
 * A KTBS method.
 * 
 * @author Damien Cram
 *
 */
public interface Method extends KtbsResource, ResourceWithParameters {
	
	/**
	 * The built-in method named "ktbs:filter" in the KTBS.
	 */
	public static final Method FILTER = new BuiltinMethod(KtbsConstants.FILTER); 

	/**
	 * The built-in method named "ktbs:fusion" in the KTBS.
	 */
	public static final Method FUSION = new BuiltinMethod(KtbsConstants.FUSION); 

	/**
	 * The built-in method named "ktbs:fusion" in the KTBS.
	 */
	public static final Method SCRIPT_PYTHON = new BuiltinMethod(KtbsConstants.SCRIPT_PYTHON); 
	
	/**
	 * Give the value of the property "ktbs:inherits" as a string.
	 * 
	 * @return the value of the property "ktbs:inherits" as a string
	 */
	public String getInherits();
	
	/**
	 * Remove any already existing value for the property "ktbs:inherits"
	 * and set a new one.
	 * 
	 * @param methodURI the uri of the new inherited method
	 */
	public void setInherits(String methodURI);
	
	/**
	 * Give the value of the "ktbs:etag" property.
	 * 
	 * @return the value of the "ktbs:etag" property
	 */
	public String getETag();

}
