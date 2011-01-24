package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.core.api.KtbsParameter;
import org.liris.ktbs.core.api.ResourceWithParameters;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RdfResourceWithParameter extends RdfKtbsResource implements ResourceWithParameters {

	public RdfResourceWithParameter(String uri, Model rdfModel, ResourceRepository holder) {
		super(uri, rdfModel, holder);
	}
	
	@Override
	public Iterator<KtbsParameter> listParameters() {
		Resource resource = rdfModel.getResource(uri);
		Property property = rdfModel.getProperty(KtbsConstants.P_HAS_PARAMETER);
		StmtIterator it = rdfModel.listStatements(
			resource,
			property,
			(RDFNode) null
		);
		return new KtbsParameterIterator(it);
	}
	
	private class KtbsParameterIterator implements Iterator<KtbsParameter> {

		private StmtIterator stmtIt;
		
		
		KtbsParameterIterator(StmtIterator stmtIt) {
			super();
			this.stmtIt = stmtIt;
		}

		@Override
		public boolean hasNext() {
			return stmtIt.hasNext();
		}

		@Override
		public KtbsParameter next() {
			return KtbsUtils.parseParameter(stmtIt.next().getObject().asLiteral().getString());
		}

		@Override
		public void remove() {
			stmtIt.remove();
		}
	}

	
	@Override
	public void setParameter(String key, String value) {
		removeParameter(key);
		rdfModel.getResource(uri).addLiteral(
				rdfModel.getProperty(KtbsConstants.P_HAS_PARAMETER), 
				rdfModel.createLiteral(key+"="+value));
	}

	@Override
	public KtbsParameter getParameter(String key) {
		for(KtbsParameter param:KtbsUtils.toLinkedList(listParameters())) {
			if(param.getName().equals(key))
				return param;
		}
		return null;
	}

	@Override
	public void removeParameter(String key) {
		Iterator<KtbsParameter> it = listParameters();
		while (it.hasNext()) {
			KtbsParameter ktbsParameter = (KtbsParameter) it.next();
			if(ktbsParameter.getName().equals(key))
				it.remove();
		}
	}
}
