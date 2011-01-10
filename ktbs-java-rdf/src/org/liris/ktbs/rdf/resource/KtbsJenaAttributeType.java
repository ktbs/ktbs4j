package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class KtbsJenaAttributeType extends KtbsJenaResource implements
AttributeType {

	KtbsJenaAttributeType(String uri, Model rdfModel, KtbsResourceHolder holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public ObselType getDomain() {
		Resource domain = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN);
		if(domain == null)
			return null;
		else
			return holder.getResource(domain.getURI(), ObselType.class);
	}

	@Override
	public void setDomain(ObselType domain) {
		checkExitsenceAndAddResource(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN, domain);
	}
}
