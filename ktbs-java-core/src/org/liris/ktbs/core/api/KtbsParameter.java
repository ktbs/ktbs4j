package org.liris.ktbs.core.api;

/**
 * A KTBS parameter for KTBS resources that handle "key=value" 
 * parameter definitions as values of the property ktbs:hasParameter.
 * 
 * @author Damien Cram
 * @see ResourceWithParameters
 * @see ComputedTrace
 * @see Method
 *
 */
public interface KtbsParameter {
	
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
