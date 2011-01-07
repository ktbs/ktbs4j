package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.KtbsConstants;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class KtbsJenaObselType extends KtbsJenaResource implements ObselType {
	
	KtbsJenaObselType(String uri, Model rdfModel) {
		super(uri, rdfModel);
	}

	@Override
	public TraceModel getTraceModel() {
		return EmptyResourceFactory.getInstance().createTraceModel(KtbsUtils.resolveParentURI(uri));
	}

	@Override
	public Iterator<AttributeType> listAttributes() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN),
				rdfModel.getResource(uri)
				);
		return new SubjectWithRdfModelIterator<AttributeType>(rdfModel, it, AttributeType.class);
	}

	@Override
	public Iterator<RelationType> listOutgoingRelations() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_HAS_RELATION_DOMAIN),
				rdfModel.getResource(uri)
		);
		return new SubjectWithRdfModelIterator<RelationType>(rdfModel, it, RelationType.class);
	}

	@Override
	public Iterator<RelationType> listIncomingRelations() {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_HAS_RELATION_RANGE),
				rdfModel.getResource(uri)
		);
		return new SubjectWithRdfModelIterator<RelationType>(rdfModel, it, RelationType.class);
	}

	@Override
	public ObselType getSuperObselType() {
		StmtIterator it = rdfModel.listStatements(
				rdfModel.getResource(uri),
				rdfModel.getProperty(KtbsConstants.P_HAS_SUPER_OBSEL_TYPE),
				(RDFNode)null
		);
		return it.hasNext()?
				EmptyResourceFactory.getInstance().createObselType(it.next().getObject().asResource().getURI()):
					null;
	}
}