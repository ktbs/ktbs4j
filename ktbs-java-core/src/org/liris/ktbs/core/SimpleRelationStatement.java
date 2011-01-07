package org.liris.ktbs.core;

public class SimpleRelationStatement implements RelationStatement {

	private Obsel from;
	private String relationURI;
	private Obsel to;
	
	
	public SimpleRelationStatement(Obsel from, String relationURI, Obsel to) {
		super();
		this.from = from;
		this.relationURI = relationURI;
		this.to = to;
	}

	@Override
	public Obsel getFromObsel() {
		return from;
	}

	@Override
	public String getRelationURI() {
		return relationURI;
	}

	@Override
	public Obsel getToObsel() {
		return to;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RelationStatement) {
			RelationStatement rs = (RelationStatement) obj;
			return getRelationURI().equals(((RelationStatement) obj).getRelationURI())
						&& getFromObsel().equals(rs.getFromObsel())
						&& getToObsel().equals(rs.getToObsel());
		}
		return false;
	}
}
