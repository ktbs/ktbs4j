package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class KtbsJenaRoot extends KtbsJenaResource implements KtbsRoot{

	KtbsJenaRoot(String uri, Model rdfModel, RDFResourceRepositoryImpl holder) {
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
		return new KtbsResourceObjectIterator<Base>(stmtIt, Base.class, repository, false);
	}

	@Override
	public void addBase(Base base) {
		repository.checkExistency(base);
		
		createParentConnection(this, base);
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
				return repository.getResource(baseURI, Base.class);
		}
		return null;
	}
}
