package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.ResourceRepository;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class RDFAttributeType extends RdfKtbsResource implements
AttributeType {

	RDFAttributeType(String uri, Model rdfModel, ResourceRepository holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public ObselType getDomain() {
		Resource domain = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN);
		if(domain == null)
			return null;
		else
			return repository.getResource(domain.getURI(), ObselType.class);
	}
	
	@Override
	public void setDomain(ObselType domain) {
		checkExitsenceAndAddResource(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN, domain);
	}
}
