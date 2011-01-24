package org.liris.ktbs.core;

/**
 * An interface for KTBS resource that can be 
 * represented as a RDF string.
 * 
 * @author Damien Cram
 *
 */
public interface SerializableResource {
	
	/**
	 * Build the string representation of this resource, using the proper syntax 
	 * associated to a given MIME type.
	 * 
	 * @param mimeType the input MIME type
	 * @return the string representation of this resource
	 */
	public String toSerializedString(String mimeType);
}
