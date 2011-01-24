package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.ObselType;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RdfAttributeType extends RdfKtbsResource implements
AttributeType {

	public RdfAttributeType(String uri, Model rdfModel, ResourceRepository holder) {
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
	public void addDomain(ObselType domain) {
		repository.checkExistency(domain);
		addResource(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN, domain.getURI());
	}

	@Override
	public Iterator<ObselType> listDomains() {
		StmtIterator it = rdfModel.listStatements(
				rdfModel.getResource(uri), 
				rdfModel.getProperty(KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN), 
				(RDFNode)null);
		
		return new RdfObjectIterator<ObselType>(it, ObselType.class, repository, false);
	}
}
