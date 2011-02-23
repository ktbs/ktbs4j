package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

public class TraceModelPojo extends ResourcePojo {
	
	private Set<ObselTypePojo> obselTypes = new HashSet<ObselTypePojo>();
	private Set<RelationTypePojo> relationTypes = new HashSet<RelationTypePojo>();
	private Set<AttributeTypePojo> attributeTypes = new HashSet<AttributeTypePojo>();
	
	public Set<ObselTypePojo> getObselTypes() {
		return obselTypes;
	}
	
	public void setObselTypes(Set<ObselTypePojo> obselTypes) {
		this.obselTypes = obselTypes;
	}
	
	public Set<RelationTypePojo> getRelationTypes() {
		return relationTypes;
	}
	
	public void setRelationTypes(Set<RelationTypePojo> relationTypes) {
		this.relationTypes = relationTypes;
	}
	
	public Set<AttributeTypePojo> getAttributeTypes() {
		return attributeTypes;
	}
	
	public void setAttributeTypes(Set<AttributeTypePojo> attributeTypes) {
		this.attributeTypes = attributeTypes;
	}
}
