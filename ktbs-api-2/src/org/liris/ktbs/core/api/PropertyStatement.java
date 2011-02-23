package org.liris.ktbs.core.api;

/**
 * A property statement describing a KTBS resource.
 * 
 * @author Damien Cram
 */
public interface PropertyStatement {
	
	/**
	 * Give the uri of the subject in this statement.
	 * 
	 * @return the uri of the subject
	 */
	public String getResourceUri();
	
	/**
	 * Give the uri of the predicate in this statement.
	 * 
	 * @return the uri of the predicate.
	 */
	public String getPropertyName();
	
	/**
	 * Give the object defined in this statement.
	 * 
	 * @return the object of this statement as a KtbsResource if it 
	 * is a KTBS resource, or as a mapped Java object if it is a literal
	 */
	public Object getPropertyValue();
}
