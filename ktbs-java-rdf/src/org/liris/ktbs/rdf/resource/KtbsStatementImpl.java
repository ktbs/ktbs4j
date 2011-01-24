package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.api.KtbsStatement;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * An RDF triple.
 * <p>
 * This implementation wraps a Jena statement. This is meant to hide the 
 * rather complicated Jena API to the user.
 * </p>
 * @author Damien Cram
 *
 */
public class KtbsStatementImpl implements KtbsStatement{

	private Statement jenaStatement;

	KtbsStatementImpl(Statement jenaStatement) {
		super();
		this.jenaStatement = jenaStatement;
	}

	@Override
	public String getSubject() {
		return jenaStatement.getSubject().getURI();
	}

	@Override
	public String getProperty() {
		return jenaStatement.getPredicate().getURI();
	}

	@Override
	public Object getObject() {
		RDFNode object = jenaStatement.getObject();
		if(object.isAnon())
			return "";
		else if(object.isLiteral())
			return object.asLiteral().getValue();
		else if(object.isResource())
			return object.asResource().getURI();
		else
			throw new IllegalStateException("Unkown type of object in the statement " + jenaStatement.toString());
	}
	
	@Override
	public String toString() {
		return "<"+getSubject()+"> "+getProperty()+" <"+getObject()+">";
	}
	
}
