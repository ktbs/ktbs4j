package org.liris.ktbs.core.api;

import java.util.Map;

import org.liris.ktbs.core.api.share.ContainedResource;

/**
 * A KTBS stored trace.
 * 
 * @author Damien Cram
 *
 */
public interface StoredTrace extends Trace, ContainedResource {
	
	/**
	 * Create a new obsel and add it to this stored trace.
	 * 
	 * @param obselLocalName the local name of the new obsel
	 * @param type the obsel type of the new obsel
	 * @param attributes the attributes of the new obsel
	 * @return the created obsel
	 */
	public Obsel newObsel(String obselLocalName, ObselType type, Map<AttributeType, Object> attributes);
	
	/**
	 * Remove any obsel of a given uri contained in this trace.
	 * 
	 * @param obselURI the uri (either absolute or relative to the trace URI) of the obsel to be removed
	 */
	public void removeObsel(String obselURI);

	/**
	 * Give the user that is, by default, the subject of all obsels 
	 * contained in this trace.
	 * 
	 * @return the user id of the subject, null if none
	 */
	public String getDefaultSubject();
	
	/**
	 * Set a new default subject.
	 * 
	 * @param subject the user id of the new subject
	 */
	public void setDefaultSubject(String subject);
}
