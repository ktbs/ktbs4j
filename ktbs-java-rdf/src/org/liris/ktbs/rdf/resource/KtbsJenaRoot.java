package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class KtbsJenaRoot extends KtbsJenaResource implements KtbsRoot{

	KtbsJenaRoot(String uri, Model rdfModel, KtbsResourceHolder holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public KtbsResource get(String resourceURI) {
		return getBase(resourceURI);
	}

	@Override
	public Iterator<Base> listBases() {
		StmtIterator stmtIt = rdfModel.listStatements(
				rdfModel.getResource(uri), 
				rdfModel.getProperty(KtbsConstants.P_HAS_BASE),
				(RDFNode)null);
		return new KtbsResourceObjectIterator<Base>(stmtIt, Base.class, holder);
	}

	@Override
	public void addBase(Base base, String owner) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Base getBase(String baseURI) {
		NodeIterator it = rdfModel.listObjectsOfProperty(
				rdfModel.getResource(uri),
				rdfModel.getProperty(KtbsConstants.P_HAS_BASE)
				);
		while(it.hasNext()) {
			RDFNode node = it.next();
			if(node.asResource().getURI().equals(baseURI))
				return holder.getResource(baseURI, Base.class);
		}
		return null;
	}
}
