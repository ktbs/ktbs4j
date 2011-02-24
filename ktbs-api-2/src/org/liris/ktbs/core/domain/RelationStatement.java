package org.liris.ktbs.core.domain;


public class RelationStatement {
	
	
	private Obsel fromObsel;
	private RelationType relation;
	private Obsel toObsel;
	
	public Obsel getFromObsel() {
		return fromObsel;
	}
	public void setFromObsel(Obsel fromObsel) {
		this.fromObsel = fromObsel;
	}
	public RelationType getRelation() {
		return relation;
	}
	public void setRelation(RelationType relation) {
		this.relation = relation;
	}
	public Obsel getToObsel() {
		return toObsel;
	}
	
	public void setToObsel(Obsel toObsel) {
		this.toObsel = toObsel;
	}
	
	public RelationStatement(Obsel fromObsel,
			RelationType relation, Obsel toObsel) {
		super();
		this.fromObsel = fromObsel;
		this.relation = relation;
		this.toObsel = toObsel;
	}
	
	public RelationStatement() {
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RelationStatement) {
			RelationStatement rel = (RelationStatement) obj;
			return rel.getFromObsel().equals(fromObsel)
					&& rel.getToObsel().equals(toObsel)
					&& rel.getRelation().equals(relation);
		}
		return false;
	}
}
