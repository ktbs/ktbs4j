package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public abstract class KtbsResourceIterator<T extends KtbsResource> implements Iterator<T> {
	protected KtbsJenaResourceHolder holder;
	protected StmtIterator stmtIterator;
	protected Class<T> clazz;
	
	KtbsResourceIterator(StmtIterator stmtIterator, Class<T> clazz, KtbsJenaResourceHolder holder) {
		super();
		this.stmtIterator = stmtIterator;
		this.clazz = clazz;
		this.holder = holder;
	}

	@Override
	public boolean hasNext() {
		return stmtIterator.hasNext();
	}

	@Override
	public T next() {
		return holder.getResource(
				getResourceFromStatement(stmtIterator.next()).getURI(),
				clazz
				);
	}

	@Override
	public void remove() {
		stmtIterator.remove();
	}

	protected abstract Resource getResourceFromStatement(Statement s);
}
