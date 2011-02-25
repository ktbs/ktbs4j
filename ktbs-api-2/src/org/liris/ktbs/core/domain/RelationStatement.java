package org.liris.ktbs.core.domain;

import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IRelationStatement;
import org.liris.ktbs.core.domain.interfaces.IRelationType;


public class RelationStatement implements IRelationStatement {
	
	
	private IObsel fromObsel;
	private IRelationType relation;
	private IObsel toObsel;
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRelationStatement#getFromObsel()
	 */
	@Override
	public IObsel getFromObsel() {
		return fromObsel;
	}
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRelationStatement#setFromObsel(org.liris.ktbs.core.domain.Obsel)
	 */
	@Override
	public void setFromObsel(IObsel fromObsel) {
		this.fromObsel = fromObsel;
	}
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRelationStatement#getRelation()
	 */
	@Override
	public IRelationType getRelation() {
		return relation;
	}
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRelationStatement#setRelation(org.liris.ktbs.core.domain.RelationType)
	 */
	@Override
	public void setRelation(IRelationType relation) {
		this.relation = relation;
	}
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRelationStatement#getToObsel()
	 */
	@Override
	public IObsel getToObsel() {
		return toObsel;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRelationStatement#setToObsel(org.liris.ktbs.core.domain.Obsel)
	 */
	@Override
	public void setToObsel(IObsel toObsel) {
		this.toObsel = toObsel;
	}
	
	public RelationStatement(IObsel fromObsel,
			IRelationType relation, IObsel toObsel) {
		super();
		this.fromObsel = fromObsel;
		this.relation = relation;
		this.toObsel = toObsel;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RelationStatement) {
			IRelationStatement rel = (IRelationStatement) obj;
			return rel.getFromObsel().equals(fromObsel)
					&& rel.getToObsel().equals(toObsel)
					&& rel.getRelation().equals(relation);
		}
		return false;
	}
}
