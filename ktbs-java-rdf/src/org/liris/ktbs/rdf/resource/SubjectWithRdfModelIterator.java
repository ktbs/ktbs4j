package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ResourceRepository;

import com.hp.hpl.jena.rdf.model.StmtIterator;

class SubjectWithRdfModelIterator<T extends KtbsResource> extends RDFSubjectIterator<T> {

	SubjectWithRdfModelIterator(
			StmtIterator stmtIterator, Class<T> clazz, ResourceRepository holder, boolean removeSupported) {
		super(stmtIterator, clazz, holder, removeSupported);
	}

	@Override
	public T next() {
		return holder.getResource(
				stmtIterator.next().getSubject().getURI(), 
				clazz); 
	}
}
