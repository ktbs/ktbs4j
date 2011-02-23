package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

public class RelationTypePojo extends ResourcePojo {
	
	private Set<RelationTypePojo> superRelationTypes = new HashSet<RelationTypePojo>();

	private Set<ObselTypePojo> domains = new HashSet<ObselTypePojo>();
	private Set<ObselTypePojo> ranges = new HashSet<ObselTypePojo>();
	
	public Set<RelationTypePojo> getSuperRelationTypes() {
		return superRelationTypes;
	}
	
	public void setSuperRelationTypes(Set<RelationTypePojo> superRelationTypes) {
		this.superRelationTypes = superRelationTypes;
	}
	
	public Set<ObselTypePojo> getDomains() {
		return domains;
	}
	
	public void setDomains(Set<ObselTypePojo> domains) {
		this.domains = domains;
	}
	
	public Set<ObselTypePojo> getRanges() {
		return ranges;
	}
	
	public void setRanges(Set<ObselTypePojo> ranges) {
		this.ranges = ranges;
	}
}
