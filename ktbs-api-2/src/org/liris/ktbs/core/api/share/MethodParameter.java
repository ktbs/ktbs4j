package org.liris.ktbs.core.api.share;

import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Method;

/**
 * A parameter for KTBS resources that handle "key=value" 
 * parameter definitions as values of the property ktbs:hasParameter.
 * 
 * @author Damien Cram
 * @see TransformationResource
 * @see ComputedTrace
 * @see Method
 *
 */
public interface MethodParameter {
	
	/**
	 * The name (key) of the parameter.
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * The value of the parameter as a string.
	 * 
	 * @return the value
	 */
	public String getValue();
}
