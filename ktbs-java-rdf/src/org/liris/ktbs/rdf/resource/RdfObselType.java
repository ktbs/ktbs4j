package org.liris.ktbs.rdf.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.InferenceException;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Mode;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RdfObselType extends RdfKtbsResource implements ObselType {

	RdfObselType(String uri, Model rdfModel, ResourceRepository repo) {
		super(uri, rdfModel, repo);
	}

	@Override
	public TraceModel getTraceModel() {
		String traceURI = KtbsUtils.resolveParentURI(uri);
		return repository.getResource(traceURI, TraceModel.class);
	}

	@Override
	public Iterator<AttributeType> listAttributes(Mode mode) {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN),
				rdfModel.getResource(uri)
		);

		Iterator<AttributeType> assertedIt = new SubjectWithRdfModelIterator<AttributeType>(it, AttributeType.class, repository, false);
		if(mode == Mode.INFERRED) {
			Set<AttributeType> inferredAttributes = new HashSet<AttributeType>();
			for(AttributeType type:KtbsUtils.toIterable(assertedIt))
				inferredAttributes.add(type);

			for(ObselType superType:KtbsUtils.toIterable(inferSuperTypes()))
				inferredAttributes.addAll(KtbsUtils.toLinkedList(superType.listAttributes(Mode.ASSERTED)));
			return Collections.unmodifiableCollection(inferredAttributes).iterator();
		} else
			return assertedIt;
	}




	@Override
	public Iterator<RelationType> listOutgoingRelations(Mode mode) {
		StmtIterator it = rdfModel.listStatements(
				null, 
				rdfModel.getProperty(KtbsConstants.P_HAS_RELATION_DOMAIN),
				rdfModel.getResource(uri)
		);

		SubjectWithRdfModelIterator<RelationType> assertedIt = new SubjectWithRdfModelIterator<RelationType>(it, RelationType.class, repository, false);
		if(mode == Mode.INFERRED) {
			Set<RelationType> inferredOutgoingRelations = new HashSet<RelationType>();
			for(RelationType rType:KtbsUtils.toIterable(assertedIt))
				inferredOutgoingRelations.add(rType);
			for(ObselType superType:KtbsUtils.toIterable(inferSuperTypes()))
				inferredOutgoingRelations.addAll(KtbsUtils.toLinkedList(superType.listOutgoingRelations(Mode.ASSERTED)));
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
		SubjectWithRdfModelIterator<RelationType> assertedIt = new SubjectWithRdfModelIterator<RelationType>(it, RelationType.class, repository, false);
		if(mode == Mode.INFERRED) {
			Set<RelationType> inferredIncomingRelations = new HashSet<RelationType>();
			for(RelationType rType:KtbsUtils.toIterable(assertedIt))
				inferredIncomingRelations.add(rType);
			for(ObselType superType:KtbsUtils.toIterable(inferSuperTypes()))
				inferredIncomingRelations.addAll(KtbsUtils.toLinkedList(superType.listIncomingRelations(Mode.ASSERTED)));
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
			return KtbsUtils.toLinkedList(inferSuperTypes()).contains(type);
		}
	}

	private void inferSuperTypes(ObselType type, Collection<ObselType> superTypes) {
		ObselType superObselType = type.getSuperObselType();
		if(superObselType != null) {
			if(superTypes.contains(superObselType))
				throw new InferenceException("There are cycles in the Obsel Type hierarchie. Problematic obsel type: " + superObselType);
			superTypes.add(superObselType);
			inferSuperTypes(superObselType, superTypes);
		}
	}

	@Override
	public Iterator<ObselType> inferSuperTypes() {
		Collection<ObselType> superTypes = new HashSet<ObselType>();
		inferSuperTypes(this, superTypes);
		return Collections.unmodifiableCollection(superTypes).iterator();
	}
}
