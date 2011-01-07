package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

class KtbsResourceSubjectIterator<T extends KtbsResource> extends KtbsResourceIterator<T> {

	
	
	KtbsResourceSubjectIterator(StmtIterator stmtIterator, Class<T> clazz, KtbsJenaResourceHolder holder) {
		super(stmtIterator, clazz, holder);
	}

	@Override
	protected Resource getResourceFromStatement(Statement s) {
		return s.getSubject();
	}
	
}