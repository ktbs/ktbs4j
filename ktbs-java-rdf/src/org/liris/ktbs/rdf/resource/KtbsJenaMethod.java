package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.Method;
import org.liris.ktbs.rdf.KtbsConstants;
import org.liris.ktbs.rdf.RDFRestConstants;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;

public class KtbsJenaMethod extends KtbsJenaResourceWithParameter implements Method {

	KtbsJenaMethod(String uri, Model rdfModel) {
		super(uri, rdfModel);
	}

	@Override
	public String getInherits() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_INHERITS);
		if(l==null)
			return null;
		return l.getString();
	}

	@Override
	public String getETag() {
		Literal l = getObjectOfPropertyAsLiteral(RDFRestConstants.P_HAS_ETAG);
		if(l==null)
			return null;
		return l.getString();
	}


	
}
