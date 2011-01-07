package org.liris.ktbs.core;

import java.util.Iterator;

public interface ObselType extends KtbsResource {
	public TraceModel getTraceModel();
	public Iterator<AttributeType> listAttributes();
	public Iterator<RelationType> listOutgoingRelations();
	public Iterator<RelationType> listIncomingRelations();
	public ObselType getSuperObselType();
}
