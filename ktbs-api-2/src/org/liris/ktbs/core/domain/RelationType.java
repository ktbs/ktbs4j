package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;

public class RelationType extends KtbsResource implements WithDomain<ObselType>, WithRange<ObselType> {
	
	private Set<RelationType> superRelationTypes = new HashSet<RelationType>();

	private Set<ObselType> domains = new HashSet<ObselType>();
	private Set<ObselType> ranges = new HashSet<ObselType>();
	
	public Set<RelationType> getSuperRelationTypes() {
		return superRelationTypes;
	}
	
	public void setSuperRelationTypes(Set<RelationType> superRelationTypes) {
		this.superRelationTypes = superRelationTypes;
	}
	
	@Override
	public Set<ObselType> getDomains() {
		return domains;
	}
	
	@Override
	public void setDomains(Set<ObselType> domains) {
		this.domains = domains;
	}
	
	@Override
	public Set<ObselType> getRanges() {
		return ranges;
	}
	
	@Override
	public void setRanges(Set<ObselType> ranges) {
		this.ranges = ranges;
	}
}
