package org.liris.ktbs.rdf.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.liris.ktbs.core.InferenceException;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RdfRelationType extends RdfKtbsResource implements
RelationType {

	RdfRelationType(String uri, Model rdfModel, ResourceRepository repo) {
		super(uri, rdfModel, repo);
	}

	@Override
	public ObselType getDomain() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_RELATION_DOMAIN);
		if(res == null)
			return null;
		else
			return repository.getResource(res.getURI(), ObselType.class);
	}

	@Override
	public ObselType getRange() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_RELATION_RANGE);
		if(res == null)
			return null;
		else
			return repository.getResource(res.getURI(), ObselType.class);
	}

	@Override
	public RelationType getSuperRelationType() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_SUPER_RELATION_TYPE);
		if(res == null)
			return null;
		else
			return repository.getResource(res.getURI(), RelationType.class);
	}

	@Override
	public void addRange(ObselType range) {
		repository.checkExistency(range);
		addResource(KtbsConstants.P_HAS_RELATION_RANGE, range.getURI());
	}

	@Override
	public void addDomain(ObselType domain) {
		repository.checkExistency(domain);
		addResource(KtbsConstants.P_HAS_RELATION_DOMAIN, domain.getURI());
	}

	@Override
	public void setSuperRelationType(RelationType superRelationType) {
		checkExitsenceAndAddResource(KtbsConstants.P_HAS_SUPER_RELATION_TYPE, superRelationType);
	}

	@Override
	public ObselType[] getDomainsInferred() {
		Collection<ObselType> domains = new HashSet<ObselType>();
		ObselType domain = getDomain();
		if(domain!=null)
			domains.add(domain);
		for(RelationType rType:inferSuperRelationTypes()) 
			domains.addAll(KtbsUtils.toLinkedList(rType.listDomains()));
		return domains.toArray(new ObselType[domains.size()]);
	}

	private Collection<RelationType> inferSuperRelationTypes() {
		Collection<RelationType> types = new HashSet<RelationType>();
		return inferSuperRelationTypes(this, types);
	}

	private static Collection<RelationType> inferSuperRelationTypes(RelationType type, Collection<RelationType> types) {
		RelationType superRelationType = type.getSuperRelationType();
		if(superRelationType != null) {
			if(types.contains(superRelationType))
				throw new InferenceException("There are cycles in the Relation Type hierarchy. Problematic relation type: " + superRelationType);

			types.add(type.getSuperRelationType());
			return inferSuperRelationTypes(superRelationType, types);
		} else
			return 	types;
	}

	@Override
	public ObselType[] getRangesInferred() {
		Collection<ObselType> ranges = new HashSet<ObselType>();
		ObselType range = getRange();
		if(range!=null)
			ranges.add(range);
		for(RelationType rType:inferSuperRelationTypes()) 
			ranges.addAll(KtbsUtils.toLinkedList(rType.listRanges()));
		return ranges.toArray(new ObselType[ranges.size()]);
	}


	@Override
	public Iterator<ObselType> listRanges() {
		StmtIterator it = rdfModel.listStatements(
				rdfModel.getResource(uri), 
				rdfModel.getProperty(KtbsConstants.P_HAS_RELATION_RANGE), 
				(RDFNode)null
		);

		return new RdfObjectIterator<ObselType>(it, ObselType.class, repository, false);
	}

	@Override
	public Iterator<ObselType> listDomains() {
		StmtIterator it = rdfModel.listStatements(
				rdfModel.getResource(uri), 
				rdfModel.getProperty(KtbsConstants.P_HAS_RELATION_DOMAIN), 
				(RDFNode)null
		);

		return new RdfObjectIterator<ObselType>(it, ObselType.class, repository, false);
	}
}
