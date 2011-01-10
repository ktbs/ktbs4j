package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsStatement;

import com.hp.hpl.jena.rdf.model.StmtIterator;

public class PropertyIterator implements Iterator<KtbsStatement> {

	private StmtIterator stmtIterator;
	PropertyIterator(StmtIterator stmtIt) {
		super();
		this.stmtIterator = stmtIt;
	}

	@Override
	public boolean hasNext() {
		return stmtIterator.hasNext();
	}

	@Override
	public KtbsStatement next() {
		if(!hasNext())
			return null;
		else
			return new KtbsStatementImpl(stmtIterator.next());
	}

	@Override
	public void remove() {
		stmtIterator.remove();
	}
}