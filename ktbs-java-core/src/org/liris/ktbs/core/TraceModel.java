package org.liris.ktbs.core;

import java.util.Iterator;

public interface TraceModel extends KtbsResource, ResourceContainer {
	/*
	 * TODO __init__(uri)
	 */
	public Iterator<AttributeType> listAttributeTypes();
	public Iterator<RelationType> listRelationTypes();
	public Iterator<ObselType> listObselTypes();
	
	public void newObselType(String localName);
	public ObselType getObselType(String obselTypeUri);

	public void newRelationType(String localName, ObselType domain, ObselType range);
	public RelationType getRelationType(String relationTypeUri);
	
	public void newAttributeType(String localName, ObselType domain);
	public AttributeType getAttributeType(String attributeTypeUri);
}
