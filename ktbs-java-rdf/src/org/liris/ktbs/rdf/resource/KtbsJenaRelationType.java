package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class KtbsJenaRelationType extends KtbsJenaResource implements
		RelationType {

	KtbsJenaRelationType(String uri, Model rdfModel, KtbsJenaResourceHolder holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public ObselType getDomain() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_RELATION_DOMAIN);
		if(res == null)
			return null;
		else
			return holder.getResourceAlreadyInModel(res.getURI(), ObselType.class, rdfModel);
	}

	@Override
	public ObselType getRange() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_RELATION_RANGE);
		if(res == null)
			return null;
		else
			return holder.getResourceAlreadyInModel(res.getURI(), ObselType.class, rdfModel);
	}

	@Override
	public RelationType getSuperRelationType() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_SUPER_RELATION_TYPE);
		if(res == null)
			return null;
		else
			return holder.getResourceAlreadyInModel(res.getURI(), RelationType.class, rdfModel);
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
}
