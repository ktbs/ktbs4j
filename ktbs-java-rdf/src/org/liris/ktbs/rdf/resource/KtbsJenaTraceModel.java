package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.TraceModel;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class KtbsJenaTraceModel extends KtbsJenaResource implements TraceModel {

	KtbsJenaTraceModel(String uri, Model rdfModel, ResourceRepository holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public KtbsResource get(String resourceURI) {
		KtbsResource resource ;
		
		resource = getObselType(resourceURI);
		if(resource != null)
			return resource;

		resource = getAttributeType(resourceURI);
		if(resource != null)
			return resource;
		
		resource = getRelationType(resourceURI);
		if(resource != null)
			return resource;
		return null;
	}

	@Override
	public Iterator<AttributeType> listAttributeTypes() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				RDF.type,
				rdfModel.getResource(KtbsConstants.ATTRIBUTE_TYPE)
				);
		return new SubjectWithRdfModelIterator<AttributeType>(it, AttributeType.class, repository, false);
	}

	@Override
	public Iterator<RelationType> listRelationTypes() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				RDF.type,
				rdfModel.getResource(KtbsConstants.RELATION_TYPE)
		);
		return new SubjectWithRdfModelIterator<RelationType>(it, RelationType.class, repository, false);
	}

	@Override
	public Iterator<ObselType> listObselTypes() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				RDF.type,
				rdfModel.getResource(KtbsConstants.OBSEL_TYPE)
		);
		return new SubjectWithRdfModelIterator<ObselType>(it, ObselType.class, repository, false);
	}

	@Override
	public ObselType getObselType(String obselTypeUri) {
		StmtIterator stmt = getTypeStatements(obselTypeUri, KtbsConstants.OBSEL_TYPE);
		if(stmt.hasNext())
			return KtbsJenaTraceModel.this.repository.getResource(obselTypeUri, ObselType.class);
		else
			return null;
	}

	@Override
	public RelationType getRelationType(String relationTypeUri) {
		StmtIterator stmt = getTypeStatements(relationTypeUri, KtbsConstants.RELATION_TYPE);
		if(stmt.hasNext())
			return KtbsJenaTraceModel.this.repository.getResource(relationTypeUri, RelationType.class);
		else
			return null;
	}

	@Override
	public AttributeType getAttributeType(String attributeTypeUri) {
		StmtIterator stmt = getTypeStatements(attributeTypeUri, KtbsConstants.ATTRIBUTE_TYPE);
		if(stmt.hasNext())
			return KtbsJenaTraceModel.this.repository.getResource(attributeTypeUri, AttributeType.class);
		else
			return null;
	}

	private StmtIterator getTypeStatements(String subjectUri, String requestedTypeUri) {
		return rdfModel.listStatements(
				rdfModel.getResource(subjectUri),
				RDF.type,
				rdfModel.getResource(requestedTypeUri)
				);
	}

	@Override
	public ObselType newObselType(String localName) {
		return repository.createObselType(this, localName);
	}

	@Override
	public RelationType newRelationType(String localName, ObselType domain,
			ObselType range) {
		return repository.createRelationType(this, localName, domain, range);
	}

	@Override
	public AttributeType newAttributeType(String localName, ObselType domain) {
		return repository.createAttributeType(this, localName, domain);
		
	}
}
