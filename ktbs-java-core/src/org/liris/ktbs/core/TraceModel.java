package org.liris.ktbs.core;

import java.util.Iterator;

public interface TraceModel extends KtbsResource, ResourceContainer {
	/*
	 * TODO __init__(uri)
	 */
	public Iterator<AttributeType> listAttributeTypes();
	public Iterator<RelationType> listRelationTypes();
	public Iterator<ObselType> listObselTypes();
	
	public void addObselType(ObselType obselType);
	public ObselType getObselType(String obselTypeUri);

	public void addRelationType(RelationType obselType);
	public RelationType getRelationType(String relationTypeUri);
	
	public void addAttributeType(AttributeType obselType);
	public AttributeType getAttributeType(String attributeTypeUri);
}
