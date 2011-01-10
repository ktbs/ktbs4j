package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;

class SubjectWithRdfModelIterator<T extends KtbsResource> extends KtbsResourceSubjectIterator<T> {

	private Model rdfModel;

	SubjectWithRdfModelIterator(Model rdfModel,
			StmtIterator stmtIterator, Class<T> clazz, KtbsJenaResourceHolder holder, boolean removeSupported) {
		super(stmtIterator, clazz, holder, removeSupported);
		this.rdfModel = rdfModel;
	}

	@Override
	public T next() {
		return holder.getResourceAlreadyInModel(
				stmtIterator.next().getSubject().getURI(), 
				clazz,
				this.rdfModel); 
	}
}
