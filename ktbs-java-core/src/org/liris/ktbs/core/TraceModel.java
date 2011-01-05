package org.liris.ktbs.core;

public interface TraceModel extends KtbsResource, ResourceContainer {
	/*
	 * TODO __init__(uri)
	 */
	public AttributeType listAttributeTypes();
	public RelationType listRelationTypes();
	public ObselType listObselTypes();
}
