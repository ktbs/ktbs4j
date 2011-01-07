package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;

class SubjectWithRdfModelIterator<T extends KtbsResource> extends JenaSubjectAsKtbsResourceIterator<T> {

	private Model rdfModel;

	SubjectWithRdfModelIterator(Model rdfModel,
			StmtIterator stmtIterator, Class<T> clazz) {
		super(null, stmtIterator, clazz);
		this.rdfModel = rdfModel;
	}

	@Override
	public T next() {
		return KtbsJenaResourceFactory.getInstance().createResource(
				stmtIterator.next().getSubject().getURI(), 
				this.rdfModel, 
				clazz);
	}
}
