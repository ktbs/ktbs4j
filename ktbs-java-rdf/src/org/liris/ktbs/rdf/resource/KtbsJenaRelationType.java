package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class KtbsJenaRelationType extends KtbsJenaResource implements
		RelationType {

	KtbsJenaRelationType(String uri, Model rdfModel) {
		super(uri, rdfModel);
	}

	@Override
	public ObselType getDomain() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_RELATION_DOMAIN);
		if(res == null)
			return null;
		else
			return KtbsJenaResourceFactory.getInstance().createObselType(res.getURI(), rdfModel);
	}

	@Override
	public ObselType getRange() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_RELATION_RANGE);
		if(res == null)
			return null;
		else
			return KtbsJenaResourceFactory.getInstance().createObselType(res.getURI(), rdfModel);
	}

	@Override
	public RelationType getSuperRelationType() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_SUPER_RELATION_TYPE);
		if(res == null)
			return null;
		else
			return KtbsJenaResourceFactory.getInstance().createRelationType(res.getURI(), rdfModel);
	}
}
