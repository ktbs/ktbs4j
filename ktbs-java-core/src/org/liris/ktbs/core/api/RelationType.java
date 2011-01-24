package org.liris.ktbs.core.api;


public interface RelationType extends KtbsResource {
	public ObselType getDomain();
	public ObselType getRange();
	public RelationType getSuperRelationType();
	public ObselType[] getDomainsInferred();
	public ObselType[] getRangesInferred();

	public void setRange(ObselType range);
	public void setDomain(ObselType domain);
	public void setSuperRelationType(RelationType superRelationType);
}
