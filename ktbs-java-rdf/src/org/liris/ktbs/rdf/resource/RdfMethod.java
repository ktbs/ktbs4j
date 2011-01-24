package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.RDFRestConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.Method;

import com.hp.hpl.jena.rdf.model.Model;

public class RdfMethod extends RdfResourceWithParameter implements Method {

	RdfMethod(String uri, Model rdfModel, ResourceRepository repo) {
		super(uri, rdfModel, repo);
	}

	@Override
	public String getInherits() {
		return getObjectResourceURIOrNull(KtbsConstants.P_INHERITS);
	}

	@Override
	public String getETag() {
		return getObjectStringOrNull(RDFRestConstants.P_HAS_ETAG);
	}
	

	@Override
	public void setInherits(String methodURI) {
		removeAllAndAddResource(KtbsConstants.P_INHERITS, methodURI);
	}
}
