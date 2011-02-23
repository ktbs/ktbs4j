package org.liris.ktbs.core.api;

import java.util.Iterator;


/**
 * A set of functionalities that must be implemented by KTBS resources
 * that handle "key=value" transformation pairs.
 * 
 * @author Damien Cram
 * @see Method
 * @see ComputedTrace
 */
public interface TransformationResource {

	/**
	 * List all the parameters defined on this resource.
	 * 
	 * @return an iterator on the parameters
	 */
	public Iterator<MethodParameter> listMethodParameters();
	
	/**
	 * Remove any already existing parameter definition with the same
	 * parameter key/name and set a new one.
	 * 
	 * @param key the name of the new parameter definition
	 * @param value the value of the new parameter definition
	 */
	public void setMethodParameter(String key, String value);
	
	/**
	 * Give the value of a parameter that is defined on this resource.
	 * 
	 * @param key the name of the parameter to retrieve
	 * @return the parameter of that name if any, null otherwise
	 */
	public String getMethodParameter(String key);
	
	/**
	 * Remove any parameter definition of a given name/key.
	 * 
	 * @param key the name of the parameter to remove
	 */
	public void removeMethodParameter(String key);
}
