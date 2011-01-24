package org.liris.ktbs.core.api;

import java.util.Map;

/**
 * A KTBS stored trace.
 * 
 * @author Damien Cram
 *
 */
public interface StoredTrace extends Trace {
	
	/**
	 * Create a new obsel and add it to this stored trace.
	 * 
	 * @param obselURI the uri of the new obsel
	 * @param type the obsel type of the new obsel
	 * @param attributes the attributes of the new obsel
	 * @return the created obsel
	 */
	public Obsel newObsel(String obselURI, ObselType type, Map<AttributeType, Object> attributes);
	
	/**
	 * Remove any obsel of a given uri contained in this trace.
	 * 
	 * @param obselURI the uri of the obsel to be removed
	 */
	public void removeObsel(String obselURI);

	/**
	 * Give the value of the "ktbs:hasSubject" property.
	 * 
	 * @return the value of the "ktbs:hasSubject" property
	 */
	public String getDefaultSubject();
	
	/**
	 * Remove any existing value of the "ktbs:hasSubject" property and set a new one.
	 * 
	 * @param subject the new value
	 */
	public void setDefaultSubject(String subject);
}
