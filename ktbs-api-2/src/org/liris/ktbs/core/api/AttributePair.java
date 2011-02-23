package org.liris.ktbs.core.api;



/**
 * A KTBS "attribute=value" statement for KTBS obsels.
 * 
 * @author Damien Cram
 * @see RelationStatement
 *
 */
public interface AttributePair {
	
	/**
	 * Give the attribute type of this statement.
	 * 
	 * @return the attribute type
	 */
	public AttributeType getAttributeType();
	
	/**
	 * Give the value of the attribute statement as a Java object.
	 * 
	 * @return the value of this statement
	 */
	public Object getValue();
}
