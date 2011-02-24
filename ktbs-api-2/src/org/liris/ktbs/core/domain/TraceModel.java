package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;

public class TraceModel extends KtbsResource {
	
	private Set<ObselType> obselTypes = new HashSet<ObselType>();
	private Set<RelationType> relationTypes = new HashSet<RelationType>();
	private Set<AttributeType> attributeTypes = new HashSet<AttributeType>();
	
	public Set<ObselType> getObselTypes() {
		return obselTypes;
	}
	
	public void setObselTypes(Set<ObselType> obselTypes) {
		this.obselTypes = obselTypes;
	}
	
	public Set<RelationType> getRelationTypes() {
		return relationTypes;
	}
	
	public void setRelationTypes(Set<RelationType> relationTypes) {
		this.relationTypes = relationTypes;
	}
	
	public Set<AttributeType> getAttributeTypes() {
		return attributeTypes;
	}
	
	public void setAttributeTypes(Set<AttributeType> attributeTypes) {
		this.attributeTypes = attributeTypes;
	}
}
