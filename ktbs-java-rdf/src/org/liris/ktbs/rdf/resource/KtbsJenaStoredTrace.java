package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class KtbsJenaStoredTrace extends KtbsJenaTrace implements StoredTrace {

	KtbsJenaStoredTrace(String uri, Model rdfModel, KtbsResourceHolder holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public void addObsel(Obsel obsel) {
		holder.addResourceAsPartOfExistingModel(obsel, rdfModel);
		((KtbsJenaObsel)obsel).removeAllAndAddResource(KtbsConstants.P_HAS_TRACE,this.uri);
	}

	@Override
	public String getDefaultSubject() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_SUBJECT);
		if(l == null)
			return null;
		else
			return l.getString();
	}
	
	@Override
	public void removeObsel(String obselURI) {
		rdfModel.removeAll(rdfModel.getResource(obselURI), null, (RDFNode)null);
		rdfModel.removeAll(null, null, rdfModel.getResource(obselURI));
		holder.removeResource(obselURI);
	}

	

	@Override
	public void setDefaultSubject(String subject) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_SUBJECT, subject);
	}
}
