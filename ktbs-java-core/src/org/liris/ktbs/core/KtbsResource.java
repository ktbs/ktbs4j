package org.liris.ktbs.core;

import java.util.Iterator;


public interface KtbsResource extends Comparable<KtbsResource> {
	public String getURI();
	
	public String getLabel();
	public void setLabel(String label);

	
	/**
	 * Get the value of the rdf:type property
	 * @return the rdf:type value as a string, null if none
	 */
	public String getResourceType();
	
	public Iterator<KtbsStatement> listAllStatements();
	
	/**
	 * List all properties that are semantically significant for the KTBS, 
	 * i.e. all properties in namespace ktbs, rdf, and rdfs.
	 * 
	 * @return the list of names of all properties in namespaces ktbs, rdf, and rdfs
	 */
	public Iterator<KtbsStatement> listKtbsStatements();
	
	public Iterator<KtbsStatement> listNonKtbsStatements();
	
	public Object[] getPropertyValues(String propertyName);
	public void addProperty(String propertyName, Object value);
	public void removeProperty(String propertyName);
}
