package org.liris.ktbs.core;

public interface RelationType extends KtbsResource {
	public ObselType getDomain();
	public ObselType getRange();
	public RelationType getSuperRelationType();

	public void setRange(ObselType range);
	public void setDomain(ObselType domain);
	public void setSuperRelationType(RelationType superRelationType);
}
