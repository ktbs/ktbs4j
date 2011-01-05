package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.empty.EmptyResourceFactory;

import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RdfKtbsObjectIterator<T extends KtbsResource> implements Iterator<T> {

	private StmtIterator stmtIterator;
	private Class<T> clazz;
	
	RdfKtbsObjectIterator(StmtIterator stmtIterator, Class<T> clazz) {
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
				stmtIterator.next().getObject().asResource().getURI(),
				clazz);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Read-only iterator");
	}
}
