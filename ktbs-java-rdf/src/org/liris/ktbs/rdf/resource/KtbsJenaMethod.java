package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.RDFRestConstants;

import com.hp.hpl.jena.rdf.model.Model;

public class KtbsJenaMethod extends KtbsJenaResourceWithParameter implements Method {

	KtbsJenaMethod(String uri, Model rdfModel, RDFResourceRepositoryImpl holder) {
		super(uri, rdfModel, holder);
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
