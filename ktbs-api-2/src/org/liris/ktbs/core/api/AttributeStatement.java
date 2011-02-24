package org.liris.ktbs.core.api;

import org.liris.ktbs.core.pojo.AttributeTypePojo;



/**
 * A KTBS "attribute=value" statement for KTBS obsels.
 * 
 * @author Damien Cram
 * @see RelationStatement
 *
 */
public interface AttributeStatement extends PropertyStatement {
	
	/**
	 * Give the attribute type of this statement.
	 * 
	 * @return the attribute type
	 */
	public AttributeTypePojo getAttributeType();
}
