package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.empty.EmptyResourceFactory;

import com.hp.hpl.jena.rdf.model.StmtIterator;

class JenaSubjectAsKtbsResourceIterator<T extends KtbsResource> implements Iterator<T> {

	/**
	 * 
	 */
	private final KtbsJenaResource ktbsJenaResource;
	protected StmtIterator stmtIterator;
	protected Class<T> clazz;
	
	JenaSubjectAsKtbsResourceIterator(KtbsJenaResource ktbsJenaResource, StmtIterator stmtIterator, Class<T> clazz) {
		super();
		this.ktbsJenaResource = ktbsJenaResource;
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
				stmtIterator.next().getSubject().getURI(),
				clazz
				);
	}

	@Override
	public void remove() {
		throw new ReadOnlyObjectException(this.ktbsJenaResource);
	}
}