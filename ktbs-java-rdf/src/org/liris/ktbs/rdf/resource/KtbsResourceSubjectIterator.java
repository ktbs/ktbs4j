package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResource;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

class KtbsResourceSubjectIterator<T extends KtbsResource> extends KtbsResourceIterator<T> {

	KtbsResourceSubjectIterator(StmtIterator stmtIterator, Class<T> clazz) {
		super(stmtIterator, clazz);
	}

	@Override
	protected Resource getResource(Statement s) {
		return s.getSubject();
	}
	
}