package org.liris.ktbs.rdf.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResourceNotFoundException;
import org.liris.ktbs.core.Mode;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class KtbsJenaObselType extends KtbsJenaResource implements ObselType {

	private Collection<AttributeType> inferredAttributes = null;
	private Collection<RelationType> inferredOutgoingRelations = null;
	private Collection<RelationType> inferredIncomingRelations = null;

	KtbsJenaObselType(String uri, Model rdfModel, RDFResourceRepositoryImpl holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public TraceModel getTraceModel() {
		String traceURI = KtbsUtils.resolveParentURI(uri);
		if(!repository.exists(traceURI))
			throw new KtbsResourceNotFoundException(traceURI);
		return repository.getResource(traceURI, TraceModel.class);
	}

	@Override
	public Iterator<AttributeType> listAttributes(Mode mode) {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN),
				rdfModel.getResource(uri)
		);

		Iterator<AttributeType> assertedIt = new SubjectWithRdfModelIterator<AttributeType>(rdfModel, it, AttributeType.class, repository, false);
		if(mode == Mode.INFERRED) {
			inferredAttributes = new HashSet<AttributeType>();
			inferAttributes(this);
			return Collections.unmodifiableCollection(inferredAttributes).iterator();
		} else
			return assertedIt;
	}

	private void inferAttributes(ObselType obselType) {
		inferredAttributes.addAll(KtbsUtils.toLinkedList(obselType.listAttributes(Mode.ASSERTED)));
		ObselType superObselType = obselType.getSuperObselType();
		if(superObselType!= null)
			inferAttributes(superObselType);
	}

	private void inferOutgoingRelations(ObselType obselType) {
		inferredOutgoingRelations.addAll(KtbsUtils.toLinkedList(obselType.listOutgoingRelations(Mode.ASSERTED)));
		ObselType superObselType = obselType.getSuperObselType();
		if(superObselType!= null)
			inferOutgoingRelations(superObselType);
	}

	private void inferIncomingRelations(ObselType obselType) {
		inferredIncomingRelations.addAll(KtbsUtils.toLinkedList(obselType.listIncomingRelations(Mode.ASSERTED)));
		ObselType superObselType = obselType.getSuperObselType();
		if(superObselType!= null)
			inferIncomingRelations(superObselType);
	}

	@Override
	public Iterator<RelationType> listOutgoingRelations(Mode mode) {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_HAS_RELATION_DOMAIN),
				rdfModel.getResource(uri)
		);

		SubjectWithRdfModelIterator<RelationType> assertedIt = new SubjectWithRdfModelIterator<RelationType>(rdfModel, it, RelationType.class, repository, false);
		if(mode == Mode.INFERRED) {
			inferredOutgoingRelations = new HashSet<RelationType>();
			inferOutgoingRelations(this);
			return Collections.unmodifiableCollection(inferredOutgoingRelations).iterator();
		} else
			return assertedIt;
	}

	@Override
	public Iterator<RelationType> listIncomingRelations(Mode mode) {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_HAS_RELATION_RANGE),
				rdfModel.getResource(uri)
		);
		SubjectWithRdfModelIterator<RelationType> assertedIt = new SubjectWithRdfModelIterator<RelationType>(rdfModel, it, RelationType.class, repository, false);
		if(mode == Mode.INFERRED) {
			inferredIncomingRelations = new HashSet<RelationType>();
			inferIncomingRelations(this);
			return Collections.unmodifiableCollection(inferredIncomingRelations).iterator();
		} else
			return assertedIt;
	}

	@Override
	public ObselType getSuperObselType() {
		StmtIterator it = rdfModel.listStatements(
				rdfModel.getResource(uri),
				rdfModel.getProperty(KtbsConstants.P_HAS_SUPER_OBSEL_TYPE),
				(RDFNode)null
		);
		return it.hasNext()?
				repository.getResource(it.next().getObject().asResource().getURI(), ObselType.class):
					null;
	}

	@Override
	public void setSuperObselType(ObselType type) {
		checkExitsenceAndAddResource(KtbsConstants.P_HAS_SUPER_OBSEL_TYPE, type);
	}

	@Override
	public boolean hasSuperType(ObselType type, Mode mode) {
		if(mode == Mode.ASSERTED) {
			ObselType superObselType = getSuperObselType();
			return superObselType!= null && superObselType.equals(type);
		} else {
			Collection<ObselType> superTypes = new HashSet<ObselType>();
			inferSuperTypes(this, superTypes);
			return superTypes.contains(type);
		}
	}

	private void inferSuperTypes(ObselType type, Collection<ObselType> superTypes) {
		ObselType superObselType = type.getSuperObselType();
		if(superObselType != null) {
			superTypes.add(superObselType);
			inferSuperTypes(superObselType, superTypes);
		}
	}
}
