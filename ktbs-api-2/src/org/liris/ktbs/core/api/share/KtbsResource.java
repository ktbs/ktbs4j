package org.liris.ktbs.core.api.share;

import java.util.Collection;
import java.util.Iterator;



/**
 * A generic KTBS resource.
 * 
 * @author Damien Cram
 *
 */
public interface KtbsResource extends Comparable<KtbsResource> {
	
	/**
	 * Give the local name of the KTBS resource.
	 * 
	 * @return the local name 
	 */
	public String getLocalName();
	
	/**
	 * Give the KTBS resource that contains this resource.
	 * 
	 * @return the parent resource
	 */
	public KtbsResource getParentResource();
	
	/**
	 * Give the immutable URI of this KTBS resource.
	 * 
	 * @return the uri of the resource
	 */
	public String getURI();

	/**
	 * Give the label attached to this resource.
	 * 
	 * @return the first label found, null if no label has been set for this resource
	 */
	public String getLabel();
	
	/**
	 * Add a label to this resource.
	 * 
	 * @param label the new label value
	 */
	public void addLabel(String label);

	
	/**
	 * Get the type URI of this resource.
	 * 
	 * @return the typu URI as a string
	 */
	public String getTypeUri();
	
	/**
	 * List all properties that describe this Ktbs resource.
	 * 
	 * @return an iterator on all property statements describing this resource
	 * @see KtbsResource#listProperties()
	 */
	public Iterator<PropertyStatement> listProperties();
	
	/**
	 * Return all values of a given property that are set on this resource.
	 * 
	 * @param propertyName the name of the property
	 * @return the values of the property in an array
	 */
	public Collection<Object> getPropertyValues(String propertyName);

	/**
	 * Return the first value found for a given property. If there exist several 
	 * property of that name in the resource, there is no guarantee about which 
	 * value is returned.
	 * 
	 * @param propertyName the name of the property
	 * @return the first value found for that property name, null if none
	 */
	public Object getPropertyValue(String propertyName);

	/**
	 * Add a new property to this resource.
	 * 
	 * @param propertyName the uri of the property
	 * @param value the value of the property
	 */
	public void addProperty(String propertyName, Object value);
	
	/**
	 * Remove all properties of a given name
	 * 
	 * @param propertyName the name of the property to remove
	 */
	public void removeProperty(String propertyName);
}
