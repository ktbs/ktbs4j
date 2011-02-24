package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

public class RelationTypePojo extends ResourcePojo implements WithDomain<ObselTypePojo>, WithRange<ObselTypePojo> {
	
	private Set<RelationTypePojo> superRelationTypes = new HashSet<RelationTypePojo>();

	private Set<ObselTypePojo> domains = new HashSet<ObselTypePojo>();
	private Set<ObselTypePojo> ranges = new HashSet<ObselTypePojo>();
	
	public Set<RelationTypePojo> getSuperRelationTypes() {
		return superRelationTypes;
	}
	
	public void setSuperRelationTypes(Set<RelationTypePojo> superRelationTypes) {
		this.superRelationTypes = superRelationTypes;
	}
	
	@Override
	public Set<ObselTypePojo> getDomains() {
		return domains;
	}
	
	@Override
	public void setDomains(Set<ObselTypePojo> domains) {
		this.domains = domains;
	}
	
	@Override
	public Set<ObselTypePojo> getRanges() {
		return ranges;
	}
	
	@Override
	public void setRanges(Set<ObselTypePojo> ranges) {
		this.ranges = ranges;
	}
}
