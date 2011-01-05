package org.liris.ktbs.rdf.resource;

import org.liris.ktbs.core.KtbsStatement;

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
	public String getObject() {
		return jenaStatement.getObject().toString();
	}
	
	@Override
	public String toString() {
		return "<"+getSubject()+"> "+getProperty()+" <"+getObject()+">";
	}
	
}
