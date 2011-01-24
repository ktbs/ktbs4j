package org.liris.ktbs.core.api;


/**
 * A KTBS "attribute=value" statement for KTBS obsels.
 * 
 * @author Damien Cram
 *
 */
public interface AttributeStatement {
	
	/**
	 * Give the attribute type of this statement.
	 * 
	 * @return the attribute type
	 */
	public AttributeType getAttributeType();
	
	/**
	 * Give the value of the attribute statement as a Java object 
	 * whose class is decided by the  Java/XSD data type mapping
	 * defined in the Jena library.
	 * 
	 * @return the value of this statement
	 */
	public Object getValue();
}
