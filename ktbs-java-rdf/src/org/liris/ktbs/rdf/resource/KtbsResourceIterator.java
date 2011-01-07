package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.empty.EmptyResourceFactory;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public abstract class KtbsResourceIterator<T extends KtbsResource> implements Iterator<T> {
	protected StmtIterator stmtIterator;
	protected Class<T> clazz;
	
	KtbsResourceIterator(StmtIterator stmtIterator, Class<T> clazz) {
		super();
		this.stmtIterator = stmtIterator;
		this.clazz = clazz;
	}

	@Override
	public boolean hasNext() {
		return stmtIterator.hasNext();
	}

	@Override
	public T next() {
		return EmptyResourceFactory.getInstance().createEmptyResource(
				getResource(stmtIterator.next()).getURI(),
				clazz
				);
	}

	@Override
	public void remove() {
		throw new ReadOnlyObjectException(null);
	}

	protected abstract Resource getResource(Statement s);
}
