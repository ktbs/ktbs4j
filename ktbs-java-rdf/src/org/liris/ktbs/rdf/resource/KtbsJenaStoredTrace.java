package org.liris.ktbs.rdf.resource;

import java.util.Map;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.StoredTrace;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;

public class KtbsJenaStoredTrace extends KtbsJenaTrace implements StoredTrace {

	KtbsJenaStoredTrace(String uri, Model rdfModel, ResourceRepository holder) {
		super(uri, rdfModel, holder);
	}

//	@Override
//	public void addObsel(Obsel obsel) {
//		holder.addResourceAsPartOfExistingModel(obsel, rdfModel);
//		KtbsJenaObsel kjObsel = (KtbsJenaObsel)holder.getResource(obsel.getURI(), Obsel.class);
//		kjObsel.rdfModel = this.rdfModel;
//		kjObsel.removeAllAndAddResource(KtbsConstants.P_HAS_TRACE,this.uri);
//	}

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
		repository.removeObsel(this, getObsel(obselURI));
	}

	@Override
	public void setDefaultSubject(String subject) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_SUBJECT, subject);
	}

	@Override
	public Obsel newObsel(String obselURI, StoredTrace hasTrace,
			ObselType type, Map<AttributeType, Object> attributes) {
		return repository.createObsel(this, obselURI, type, attributes);
	}
}
