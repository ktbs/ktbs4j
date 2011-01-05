package org.liris.ktbs.rdf.resource;

import java.util.Iterator;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class KtbsJenaRoot extends KtbsJenaResource implements KtbsRoot{

	KtbsJenaRoot(String uri, Model rdfModel) {
		super(uri, rdfModel);
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
		return new RdfKtbsObjectIterator<Base>(stmtIt, Base.class);
	}

	@Override
	public void addBase(Base base, String owner) {
		throw new ReadOnlyObjectException(this);
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
				return EmptyResourceFactory.getInstance().createBase(baseURI);
		}
		return null;
	}
}
