package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ResourceRepository;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public abstract class KtbsResourceIterator<T extends KtbsResource> implements Iterator<T> {
	protected ResourceRepository holder;
	protected StmtIterator stmtIterator;
	protected Class<T> clazz;
	protected boolean removeSupported;

	KtbsResourceIterator(StmtIterator stmtIterator, Class<T> clazz, ResourceRepository holder, boolean removeSupported) {
		super();
		this.stmtIterator = stmtIterator;
		this.clazz = clazz;
		this.holder = holder;
		this.removeSupported = removeSupported;
	}

	@Override
	public boolean hasNext() {
		return stmtIterator.hasNext();
	}

	@Override
	public T next() {
		Statement next = stmtIterator.next();
		return holder.getResource(
				getResourceFromStatement(next).getURI(),
				clazz
		);
	}

	@Override
	public void remove() {
		if(removeSupported)
			stmtIterator.remove();
		else
			throw new UnsupportedOperationException("Cannot use an iterator to remove such KTBS resource.");
	}

	protected abstract Resource getResourceFromStatement(Statement s);
}
