package org.liris.ktbs.core.api;

import java.util.Collection;
import java.util.Iterator;

import org.liris.ktbs.core.api.share.ContainedResource;
import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.ResourceContainer;

/**
 * A KTBS trace model.
 * 
 * @author Damien Cram
 *
 */
public interface TraceModel extends KtbsResource, ResourceContainer<KtbsResource>, ContainedResource {
	
	/**
	 * List all attribute types defined in this trace model.
	 * 
	 * @return an iterator on the attribute types
	 */
	public Iterator<AttributeType> listAttributeTypes();
	
	/**
	 * List all relation types defined in this trace model.
	 * 
	 * @return an iterator on the relation types
	 */
	public Iterator<RelationType> listRelationTypes();

	/**
	 * List all obsel types defined in this trace model.
	 * 
	 * @return an iterator on the obsel types
	 */
	public Iterator<ObselType> listObselTypes();
	
	/**
	 * Create a new obsel type in this trace model.
	 * 
	 * @param localName the local name of the new obsel type
	 * @return the created obsel type
	 */
	public ObselType newObselType(String localName);
	
	/**
	 * Give the obsel type of a given uri contained in this trace model.
	 * 
	 * @param obselTypeUri the uri (either absolute or relative to this trace model uri) of the requested obsel type
	 * @return the obsel type of that uri contained in this trace model if any, null otherwise
	 */
	public ObselType getObselType(String obselTypeUri);

	/**
	 * Create a new relation type in this trace model.
	 * 
	 * @param localName the local name of the new relation type
	 * @param domains the list of domain types of the new relation type
	 * @param ranges the range types of the new relation type
	 * @return the created relation  type
	 */
	public RelationType newRelationType(String localName, Collection<ObselType> domains, Collection<ObselType> ranges);
	
	/**
	 * Give the relation type of a given uri contained in this trace model.
	 * 
	 * @param relationTypeUri the uri (either absolute or relative to this trace model uri) of the requested relation type
	 * @return the relation type of that uri contained in this trace model if any, null otherwise
	 */
	public RelationType getRelationType(String relationTypeUri);
	
	/**
	 * Create a new attribute type in this trace model.
	 * 
	 * @param localName the local name of the new attribute type
	 * @param domain the domain of the new attribute type
	 * @return the created attribute type
	 */
	public AttributeType newAttributeType(String localName, Collection<ObselType> domain);
	
	/**
	 * Give the attribute type of a given uri contained in this trace model.
	 * 
	 * @param attributeTypeUri the uri (either absolute or relative to this trace model uri)  of the requested attribute type
	 * @return the attribute type of that uri contained in this trace model if any, null otherwise
	 */
	public AttributeType getAttributeType(String attributeTypeUri);
}
