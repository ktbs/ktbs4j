package org.liris.ktbs.core;

public interface RelationType extends KtbsResource {
	public ObselType getDomain();
	public ObselType getRange();
	public RelationType getSuperRelationType();
}
