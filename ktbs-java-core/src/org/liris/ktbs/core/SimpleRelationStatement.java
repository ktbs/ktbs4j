package org.liris.ktbs.core;

import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.RelationStatement;
import org.liris.ktbs.core.api.RelationType;

/**
 * A simple implementation of the {@link RelationStatement} interface.
 * 
 * @author Damien Cram
 *
 */
public class SimpleRelationStatement implements RelationStatement {

	private Obsel from;
	private RelationType relation;
	private Obsel to;
	
	
	public SimpleRelationStatement(Obsel from, RelationType relation, Obsel to) {
		super();
		this.from = from;
		this.relation = relation;
		this.to = to;
	}

	@Override
	public Obsel getFromObsel() {
		return from;
	}

	@Override
	public RelationType getRelation() {
		return relation;
	}

	@Override
	public Obsel getToObsel() {
		return to;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RelationStatement) {
			RelationStatement rs = (RelationStatement) obj;
			return getRelation().equals(((RelationStatement) obj).getRelation())
						&& getFromObsel().equals(rs.getFromObsel())
						&& getToObsel().equals(rs.getToObsel());
		}
		return false;
	}
}
