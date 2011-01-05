package org.liris.ktbs.rdf.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.liris.ktbs.core.AbstractKtbsResource;
import org.liris.ktbs.core.KtbsStatement;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class KtbsJenaResource extends AbstractKtbsResource {

	protected Model rdfModel;

	KtbsJenaResource(String uri, Model rdfModel) {
		super(uri);
		this.rdfModel = rdfModel;
	}

	@Override
	public String getLabel() {
		Statement labelProperty = rdfModel.getProperty(
				rdfModel.getResource(this.getURI()),
				RDFS.label);
		if(labelProperty!=null)
			return labelProperty.getObject().asLiteral().getString();
		else
			return null;
	}

	@Override
	public void setLabel(String label) {
		throw new ReadOnlyObjectException(this);
	}

	@Override
	public Iterator<KtbsStatement> listAllProperties() {
		return new PropertyIterator(rdfModel.listStatements());
	}

	@Override
	public Iterator<KtbsStatement> listKtbsProperties() {
		SimpleSelector selector = new SimpleSelector(
				rdfModel.getResource(this.getURI()), 
				null, 
				(RDFNode) null) {
			public boolean selects(Statement s) {
				return KtbsJenaResource.this.isKtbsProperty(s);
			}
		};
		return new PropertyIterator(rdfModel.listStatements(selector));
	}

	@Override
	public Iterator<KtbsStatement> listNonKtbsProperties() {
		SimpleSelector selector = new SimpleSelector(
				rdfModel.getResource(this.getURI()), 
				null, 
				(RDFNode) null) {
			public boolean selects(Statement s) {
				return !KtbsJenaResource.this.isKtbsProperty(s);
			}
		};
		return new PropertyIterator(rdfModel.listStatements(selector));
	}

	@Override
	public String[] getPropertyValues(String propertyName) {
		SimpleSelector selector = new SimpleSelector(
				rdfModel.getResource(this.getURI()), 
				rdfModel.getProperty(propertyName), 
				(RDFNode)null);
		Iterator<KtbsStatement> it = new PropertyIterator(rdfModel.listStatements(selector));
		Collection<String> c = new HashSet<String>();
		while(it.hasNext())
			c.add(it.next().getObject());
		return c.toArray(new String[c.size()]);
	}

	private boolean isKtbsProperty(Statement s) {
		Property predicate = s.getPredicate();
		String nameSpace = predicate.getNameSpace();
		return nameSpace.equals(KtbsConstants.KTBS_NAMESPACE) 
		|| predicate.equals(RDF.type)
		|| predicate.equals(RDFS.label);
	}
}
