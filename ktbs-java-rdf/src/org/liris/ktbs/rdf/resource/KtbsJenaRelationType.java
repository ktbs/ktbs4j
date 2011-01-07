package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class KtbsJenaRelationType extends KtbsJenaResource implements
		RelationType {

	KtbsJenaRelationType(String uri, Model rdfModel, KtbsResourceHolder holder) {
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
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void setDomain(ObselType domain) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSuperRelationType(RelationType superRelationType) {
		throw new UnsupportedOperationException();
	}
}
