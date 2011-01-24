package org.liris.ktbs.core.api;

/**
 * A wrapper for underlying RDF triples (subject-predicate-object) that 
 * define KTBS resources.
 * 
 * @author Damien Cram
 * @see RelationStatement
 * @see AttributeStatement
 */
public interface KtbsStatement {
	
	/**
	 * Give the uri of the subject in this statement.
	 * 
	 * @return the uri of the subject
	 */
	public String getSubject();
	
	/**
	 * Give the uri of the predicate in this statement.
	 * 
	 * @return the uri of the predicate.
	 */
	public String getProperty();
	
	/**
	 * Give the object defined in this statement.
	 * 
	 * @return the object of this statement as a KtbsResource if it 
	 * is a KTBS resource, or as a mapped Java object if it is a literal
	 */
	public Object getObject();
}
