package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.KtbsResource;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RdfObjectIterator<T extends KtbsResource> extends ResourceIterator<T> { 

	RdfObjectIterator(StmtIterator stmtIterator, Class<T> clazz, ResourceRepository repo, boolean removeSupported) {
		super(stmtIterator, clazz, repo, removeSupported);
	}
	
	@Override
	protected Resource getResourceFromStatement(Statement s) {
		return s.getObject().asResource();
	}
}