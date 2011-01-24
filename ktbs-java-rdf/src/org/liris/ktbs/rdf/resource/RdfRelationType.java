package org.liris.ktbs.rdf.resource;

import java.util.Collection;
import java.util.HashSet;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class RdfRelationType extends RdfKtbsResource implements
RelationType {

	public RdfRelationType(String uri, Model rdfModel, ResourceRepository holder) {
		super(uri, rdfModel, holder);
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
	public void setRange(ObselType range) {
		checkExitsenceAndAddResource(KtbsConstants.P_HAS_RELATION_RANGE, range);
	}

	@Override
	public void setDomain(ObselType domain) {
		checkExitsenceAndAddResource(KtbsConstants.P_HAS_RELATION_DOMAIN, domain);
	}

	@Override
	public void setSuperRelationType(RelationType superRelationType) {
		checkExitsenceAndAddResource(KtbsConstants.P_HAS_SUPER_RELATION_TYPE, superRelationType);
	}

	@Override
	public ObselType[] getDomainsInferred() {
		Collection<ObselType> domains = new HashSet<ObselType>();
		inferDomains(this, domains);
		return domains.toArray(new ObselType[domains.size()]);
	}

	private void inferDomains(RelationType relationType,
			Collection<ObselType> domains) {
		ObselType domain = relationType.getDomain();
		if(domain!=null)
			domains.add(domain);
		RelationType superRelationType = relationType.getSuperRelationType();
		if(superRelationType != null)
			inferDomains(superRelationType, domains);
	}

	@Override
	public ObselType[] getRangesInferred() {
		Collection<ObselType> ranges = new HashSet<ObselType>();
		inferRanges(this, ranges);
		return ranges.toArray(new ObselType[ranges.size()]);
	}

	private void inferRanges(RelationType relationType,
			Collection<ObselType> ranges) {
		ObselType range = relationType.getRange();
		if(range != null)
			ranges.add(range);
		RelationType superRelationType = relationType.getSuperRelationType();
		if(superRelationType != null)
			inferRanges(superRelationType, ranges);
	}
}
