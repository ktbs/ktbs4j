package org.liris.ktbs.rdf.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.liris.ktbs.core.AbstractKtbsResource;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.KtbsResourceNotFoundException;
import org.liris.ktbs.core.KtbsStatement;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.rdf.KtbsConstants;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class KtbsJenaResource extends AbstractKtbsResource {

	protected KtbsJenaResourceHolder holder;
	protected Model rdfModel;

	KtbsJenaResource(String uri, Model rdfModel, KtbsResourceHolder holder) {
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
		removeAllAndAddLiteral(RDFS.label.getURI(), label);
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
		return nameSpace.equals(KtbsConstants.NAMESPACE) 
		|| predicate.equals(RDF.type)
		|| predicate.equals(RDFS.label);
	}


	protected Literal getObjectOfPropertyAsLiteral(String pName) {
		Statement stmt = getStatement(pName);
		if(stmt==null || !stmt.getObject().isLiteral())
			return null;
		else
			return stmt.getObject().asLiteral();
	}

	protected Resource getObjectOfPropertyAsResource(String pName) {
		Statement stmt = getStatement(pName);
		if(stmt==null || !stmt.getObject().isResource())
			return null;
		else
			return stmt.getObject().asResource();

	}

	private Statement getStatement(String pName) {
		return rdfModel.getProperty(
				rdfModel.getResource(uri), 
				rdfModel.getProperty(pName));
	}

	@Override
	public String getType() {
		Statement typeProperty = rdfModel.getProperty(
				rdfModel.getResource(this.getURI()),
				RDF.type);
		if(typeProperty!=null)
			return typeProperty.getObject().asLiteral().getString();
		else
			return null;

	}

	/**
	 * Removes all triples with a given property for this resource.
	 * 
	 * @param pName the name of the property to remove.
	 */
	protected void removeAllProperties(String pName) {
		StmtIterator it = rdfModel.listStatements(
				rdfModel.getResource(uri), 
				rdfModel.getProperty(pName), 
				(RDFNode)null);
		while(it.hasNext())
			it.removeNext();
	}
	
	@Override
	public void setType(String typeURI) {
		removeAllAndAddResource(RDF.type.getURI(), typeURI);
	}

	protected void removeParentConnection(KtbsJenaResource parent, KtbsResource childKtbsResource) {
		if (childKtbsResource instanceof KtbsJenaResource) {
			KtbsJenaResource child = (KtbsJenaResource) childKtbsResource;
			if(parent.getClass().equals(KtbsJenaRoot.class) && child.getClass().equals(Base.class)) 
				removeConnection(parent, child,KtbsConstants.P_HAS_BASE,KtbsConstants.P_HAS_BASE);
			else if(parent.getClass().equals(KtbsJenaBase.class) && child.getClass().equals(KtbsJenaTrace.class)) 
				removeConnection(parent, child,KtbsConstants.P_OWNS,KtbsConstants.P_OWNS);
			else if(parent.getClass().equals(KtbsJenaBase.class) && child.getClass().equals(KtbsJenaMethod.class)) 
				removeConnection(parent, child,KtbsConstants.P_OWNS,KtbsConstants.P_OWNS);
			else if(parent.getClass().equals(KtbsJenaBase.class) && child.getClass().equals(KtbsJenaTraceModel.class)) 
				removeConnection(parent, child,KtbsConstants.P_OWNS,KtbsConstants.P_OWNS);
			else
				throw new UnsupportedOperationException("Cannot remove a parent connection between an instance of class \""+parent.getClass()+"\" and an instance of class \""+child.getClass()+"\".");
		} else 
			throw new UnsupportedOperationException("Only instances of class "+KtbsJenaResource.class+" are supported [input class: "+childKtbsResource.getClass()+"].");
	}


	private void removeConnection(KtbsJenaResource parent,
			KtbsJenaResource child, String pNameInParent, String pNameInChild) {

		Resource parentResourceInParent = parent.rdfModel.getResource(parent.uri);
		Property hasBaseInParent = parent.rdfModel.getProperty(pNameInParent);
		Resource childResourceInParent = parent.rdfModel.getResource(child.uri);
		StmtIterator itParent = parent.rdfModel.listStatements(parentResourceInParent, hasBaseInParent, childResourceInParent);
		while(itParent.hasNext())
			itParent.removeNext();

		Resource parentResourceInChild = child.rdfModel.getResource(parent.uri);
		Property hasBaseInChild = child.rdfModel.getProperty(pNameInChild);
		Resource childResourceInChild = child.rdfModel.getResource(child.uri);
		StmtIterator itChild = child.rdfModel.listStatements(parentResourceInChild, hasBaseInChild, childResourceInChild);

		while(itChild.hasNext())
			itChild.removeNext();

	}

	protected void createParentConnection(KtbsJenaResource parent, KtbsResource childKtbsResource) {
		if (childKtbsResource instanceof KtbsJenaResource) {
			KtbsJenaResource child = (KtbsJenaResource) childKtbsResource;

			if(parent.getClass().equals(KtbsJenaRoot.class) && child.getClass().equals(Base.class)) 
				connect(parent, child,KtbsConstants.P_HAS_BASE,KtbsConstants.P_HAS_BASE,true,true);
			else if(parent.getClass().equals(KtbsJenaBase.class) && child.getClass().equals(KtbsJenaTrace.class)) 
				connect(parent, child,KtbsConstants.P_OWNS,KtbsConstants.P_OWNS,true,true);
			else if(parent.getClass().equals(KtbsJenaBase.class) && child.getClass().equals(KtbsJenaMethod.class)) 
				connect(parent, child,KtbsConstants.P_OWNS,KtbsConstants.P_OWNS,true,true);
			else if(parent.getClass().equals(KtbsJenaBase.class) && child.getClass().equals(KtbsJenaTraceModel.class)) 
				connect(parent, child,KtbsConstants.P_OWNS,KtbsConstants.P_OWNS,true,true);
			else
				throw new UnsupportedOperationException("Cannot create a parent connection between an instance of class \""+parent.getClass()+"\" and an instance of class \""+child.getClass()+"\".");
		} else 
			throw new UnsupportedOperationException("Only instances of class "+KtbsJenaResource.class+" are supported [input class: "+childKtbsResource.getClass()+"].");

	}

	private void connect(KtbsJenaResource parent, KtbsJenaResource child, String pNameInParent, String pNameInChild, boolean removeFirstInParent, boolean removeFirstInChild) {
		Resource parentResourceInParent = parent.rdfModel.getResource(parent.uri);
		Property hasBaseInParent = parent.rdfModel.getProperty(pNameInParent);
		Resource childResourceInParent = parent.rdfModel.getResource(child.uri);
		if(removeFirstInParent) {
			StmtIterator itParent = parent.rdfModel.listStatements(parentResourceInParent, hasBaseInParent, childResourceInParent);
			while(itParent.hasNext())
				itParent.removeNext();
		}
		parentResourceInParent.addProperty(
				hasBaseInParent, 
				childResourceInParent);

		Resource parentResourceInChild = child.rdfModel.getResource(parent.uri);
		Property hasBaseInChild = child.rdfModel.getProperty(pNameInChild);
		Resource childResourceInChild = child.rdfModel.getResource(child.uri);
		if(removeFirstInChild) {
			StmtIterator itChild = child.rdfModel.listStatements(null, hasBaseInChild, childResourceInChild);
			while(itChild.hasNext())
				itChild.removeNext();
		}
		parentResourceInChild.addProperty(
				hasBaseInChild, 
				childResourceInChild);
	}
	
	protected void removeAllAndAddLiteral(String pName, Object value) {
		removeAllProperties(pName);
		addLiteral(pName, value);
	}

	protected void addLiteral(String pName, Object value) {
		rdfModel.getResource(uri).addLiteral(rdfModel.getProperty(pName), value);
	}

	protected void removeAllAndAddResource(String pName, String resourceURI) {
		removeAllProperties(pName);
		addResource(pName, resourceURI);
	}

	protected Resource addResource(String pName, String resourceURI) {
		return rdfModel.getResource(uri).addProperty(
				rdfModel.getProperty(pName),
				rdfModel.getResource(resourceURI));
	}
	
	protected String getNotAKtbsJenaResourceMessage(ObselType type) {
		return "Unsupported type of KTBS Resource: " + type.getClass().getCanonicalName()+ ". Only instance of class " + KtbsJenaResource.class + " are supported.";
	}
	
	protected void checkExitsenceAndAddResource(String pName, KtbsResource resource) {
		if(!holder.exists(resource.getURI())) 
			throw new KtbsResourceNotFoundException(resource.getURI());
		removeAllAndAddResource(pName, resource.getURI());
	}
	
	public Model getJenaModel() {
		return rdfModel;
	}
}
