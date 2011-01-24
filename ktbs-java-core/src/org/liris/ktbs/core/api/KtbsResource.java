package org.liris.ktbs.core.api;

import java.util.Iterator;


/**
 * A generic KTBS resource.
 * 
 * @author Damien Cram
 *
 */
public interface KtbsResource extends Comparable<KtbsResource> {
	
	/**
	 * Give the immutable URI of this KTBS resource.
	 * 
	 * @return the uri of the resource
	 */
	public String getURI();
	
	/**
	 * Give the value of the value of the first rdfs:label 
	 * property found for this resource.
	 * 
	 * @return the first label found, null if none
	 */
	public String getLabel();
	
	/**
	 * Removes all rdfs:label properties that already exist 
	 * on this resource and set a new rdfs:label value.
	 * 
	 * @param label the new label value
	 */
	public void setLabel(String label);

	
	/**
	 * Get the value of the rdf:type property of this resource.
	 * 
	 * @return the rdf:type value as a string, null if none
	 */
	public String getResourceType();
	
	/**
	 * List all statements about this resource.
	 * 
	 * @return an iterator on all statements about this resource
	 * @see KtbsResource#listKtbsStatements()
	 * @see KtbsResource#listNonKtbsStatements()
	 */
	public Iterator<KtbsStatement> listAllStatements();
	
	/**
	 * List all statements of this resource that have a KTBS-reserved 
	 * predicate name (predicate name in namespace ktbs, rdf, and rdfs).
	 * 
	 * @return an iterator on all KTBS statements
	 * @see KtbsResource#listNonKtbsStatements()
	 */
	public Iterator<KtbsStatement> listKtbsStatements();
	
	/**
	 * List all statements that have a predicate name 
	 * that is not reserved by the KTBS.
	 * 
	 * @return an iterator on all non-KTBS statements
	 * @see KtbsResource#listKtbsStatements()
	 */
	public Iterator<KtbsStatement> listNonKtbsStatements();
	
	/**
	 * Return all values of a given property that are set on this resource.
	 * 
	 * @param propertyName the name of the property
	 * @return the values of the property in an array
	 */
	public Object[] getPropertyValues(String propertyName);

	/**
	 * Add a new property to this resource.
	 * 
	 * @param propertyName the uri of the property
	 * @param value the value of the property
	 */
	public void addProperty(String propertyName, Object value);
	
	/**
	 * Remove all triples with a specified predicate name from this resource
	 * 
	 * @param propertyName the name of the property to remove
	 */
	public void removeProperty(String propertyName);
}
