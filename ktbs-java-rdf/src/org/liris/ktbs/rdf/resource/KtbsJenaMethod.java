package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.rdf.KtbsConstants;
import org.liris.ktbs.rdf.RDFRestConstants;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class KtbsJenaMethod extends KtbsJenaResourceWithParameter implements Method {

	KtbsJenaMethod(String uri, Model rdfModel, KtbsResourceHolder holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public String getInherits() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_INHERITS);
		if(r==null)
			return null;
		return r.getURI();
	}

	@Override
	public String getETag() {
		Literal l = getObjectOfPropertyAsLiteral(RDFRestConstants.P_HAS_ETAG);
		if(l==null)
			return null;
		return l.getString();
	}

	@Override
	public String setInherits(String methodURI) {
		throw new UnsupportedOperationException();
	}
}
