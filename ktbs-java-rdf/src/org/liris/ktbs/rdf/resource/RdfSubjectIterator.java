package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ResourceRepository;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

class RdfSubjectIterator<T extends KtbsResource> extends ResourceIterator<T> {
	
	RdfSubjectIterator(StmtIterator stmtIterator, Class<T> clazz, ResourceRepository holder, boolean removeSupported) {
		super(stmtIterator, clazz, holder, removeSupported);
	}

	@Override
	protected Resource getResourceFromStatement(Statement s) {
		return s.getSubject();
	}
	
}