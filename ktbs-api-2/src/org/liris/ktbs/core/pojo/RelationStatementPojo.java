package org.liris.ktbs.core.pojo;


public class RelationStatementPojo {
	
	
	private ObselPojo fromObsel;
	private RelationTypePojo relation;
	private ObselPojo toObsel;
	public ObselPojo getFromObsel() {
		return fromObsel;
	}
	public void setFromObsel(ObselPojo fromObsel) {
		this.fromObsel = fromObsel;
	}
	public RelationTypePojo getRelation() {
		return relation;
	}
	public void setRelation(RelationTypePojo relation) {
		this.relation = relation;
	}
	public ObselPojo getToObsel() {
		return toObsel;
	}
	
	public void setToObsel(ObselPojo toObsel) {
		this.toObsel = toObsel;
	}
	
	public RelationStatementPojo(ObselPojo fromObsel,
			RelationTypePojo relation, ObselPojo toObsel) {
		super();
		this.fromObsel = fromObsel;
		this.relation = relation;
		this.toObsel = toObsel;
	}
	
	public RelationStatementPojo() {
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RelationStatementPojo) {
			RelationStatementPojo rel = (RelationStatementPojo) obj;
			return rel.getFromObsel().equals(fromObsel)
					&& rel.getToObsel().equals(toObsel)
					&& rel.getRelation().equals(relation);
		}
		return false;
	}
}
