package org.liris.ktbs.core;

import java.util.Iterator;

public interface ObselType extends KtbsResource {
	public Iterator<AttributeType> listAttributes();
	public Iterator<RelationType> listIncomingRelations();
}
