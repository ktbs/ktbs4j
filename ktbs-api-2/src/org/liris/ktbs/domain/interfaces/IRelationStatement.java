package org.liris.ktbs.domain.interfaces;


public interface IRelationStatement {

	public IObsel getFromObsel();

	public void setFromObsel(IObsel fromObsel);

	public IRelationType getRelation();

	public void setRelation(IRelationType relation);

	public IObsel getToObsel();

	public void setToObsel(IObsel toObsel);

}