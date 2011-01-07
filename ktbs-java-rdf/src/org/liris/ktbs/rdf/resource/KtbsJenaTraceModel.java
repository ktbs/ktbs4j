package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class KtbsJenaTraceModel extends KtbsJenaResource implements TraceModel {

	KtbsJenaTraceModel(String uri, Model rdfModel) {
		super(uri, rdfModel);
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
		return new SubjectWithRdfModelIterator<AttributeType>(rdfModel, it, AttributeType.class);
	}

	@Override
	public Iterator<RelationType> listRelationTypes() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				RDF.type,
				rdfModel.getResource(KtbsConstants.RELATION_TYPE)
		);
		return new SubjectWithRdfModelIterator<RelationType>(rdfModel, it, RelationType.class);
	}

	@Override
	public Iterator<ObselType> listObselTypes() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				RDF.type,
				rdfModel.getResource(KtbsConstants.OBSEL_TYPE)
		);
		return new SubjectWithRdfModelIterator<ObselType>(rdfModel, it, ObselType.class);
	}

	@Override
	public ObselType getObselType(String obselTypeUri) {
		StmtIterator stmt = getTypeStatements(obselTypeUri, KtbsConstants.OBSEL_TYPE);
		if(stmt.hasNext())
			return KtbsJenaResourceFactory.getInstance().createObselType(obselTypeUri, rdfModel);
		else
			return null;
	}

	@Override
	public RelationType getRelationType(String relationTypeUri) {
		StmtIterator stmt = getTypeStatements(relationTypeUri, KtbsConstants.RELATION_TYPE);
		if(stmt.hasNext())
			return KtbsJenaResourceFactory.getInstance().createRelationType(relationTypeUri, rdfModel);
		else
			return null;
	}

	@Override
	public AttributeType getAttributeType(String attributeTypeUri) {
		StmtIterator stmt = getTypeStatements(attributeTypeUri, KtbsConstants.ATTRIBUTE_TYPE);
		if(stmt.hasNext())
			return KtbsJenaResourceFactory.getInstance().createAttributeType(attributeTypeUri, rdfModel);
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
	
}
