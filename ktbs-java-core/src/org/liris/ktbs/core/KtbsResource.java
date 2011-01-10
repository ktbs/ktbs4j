package org.liris.ktbs.core;

import java.util.Iterator;


public interface KtbsResource extends Comparable<KtbsResource> {
	public String getURI();
	public String getLabel();
	public void setLabel(String label);

	public String getType();
	
	public Iterator<KtbsStatement> listAllProperties();
	
	/**
	 * List all properties that are semantically significant for the KTBS, 
	 * i.e. all properties in namespace ktbs, rdf, and rdfs.
	 * 
	 * @return the list of names of all properties in namespaces ktbs, rdf, and rdfs
	 */
	public Iterator<KtbsStatement> listKtbsProperties();
	
	public Iterator<KtbsStatement> listNonKtbsProperties();
	public String[] getPropertyValues(String propertyName);
}
