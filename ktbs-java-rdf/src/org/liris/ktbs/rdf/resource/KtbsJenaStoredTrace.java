package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;

public class KtbsJenaStoredTrace extends KtbsJenaTrace implements StoredTrace {

	KtbsJenaStoredTrace(String uri, Model rdfModel) {
		super(uri, rdfModel);
	}

	@Override
	public void addObsel(Obsel obsel) {
		throw new ReadOnlyObjectException(this);
	}

	@Override
	public String getDefaultSubject() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_SUBJECT);
		if(l == null)
			return null;
		else
			return l.getString();
	}

}
