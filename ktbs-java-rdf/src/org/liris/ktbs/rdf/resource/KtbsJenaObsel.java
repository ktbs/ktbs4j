package org.liris.ktbs.rdf.resource;

import java.util.Date;
import java.util.Iterator;

import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsResourceNotFoundException;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.ReadOnlyObjectException;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.core.empty.EmptyResourceFactory;
import org.liris.ktbs.rdf.KtbsConstants;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class KtbsJenaObsel extends KtbsJenaResource implements Obsel {

	KtbsJenaObsel(String uri, Model rdfModel) {
		super(uri, rdfModel);
	}

	@Override
	public Trace getTrace() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_TRACE);
		if(r==null)
			return null;
		else
			return EmptyResourceFactory.getInstance().createTrace(r.getURI());
	}

	@Override
	public long getBegin() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_BEGIN);
		if(l==null)
			return -1;
		else
			return l.getLong();
	}

	@Override
	public long getEnd() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_END);
		if(l==null)
			return -1;
		else
			return l.getLong();
	}

	@Override
	public String getBeginDT() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_BEGIN_DT);
		if(l==null)
			return null;
		else
			return l.getString();
	}

	@Override
	public String getEndDT() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_END_DT);
		if(l==null)
			return null;
		else
			return l.getString();
	}

	@Override
	public ObselType getObselType() {
		Resource r = getObjectOfPropertyAsResource(RDF.type.getURI());
		if(r==null)
			return null;
		else {
			return EmptyResourceFactory.getInstance().createObselType(r.getURI());
		}
	}

	@Override
	public String getSubject() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_SUBJECT);
		if(l==null)
			return null;
		else
			return l.getString();
	}

	@Override
	public Iterator<AttributeStatement> listAttributes() {
		StmtIterator stmtIt = rdfModel.listStatements(new AttributeSelector());

		return new AttributeStatementIt(stmtIt);
	}

	@Override
	public Object getAttributeValue(AttributeType attribute) {
		/*
		 * TODO checks in the trace model if attribute type is a valid 
		 * attribute and return ull otherwise.
		 */
		Literal l = getObjectOfPropertyAsLiteral(attribute.getURI());
		if(l==null)
			return null;
		else
			return l.getValue();
	}

	@Override
	public void addOutgoingRelation(Relation relation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addIncomingRelation(Relation relation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Relation> listIncomingRelations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Relation> listOutgoingRelations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Obsel getTargetObsel(String relationName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Obsel getSourceObsel(String relationName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getBeginDTAsDate() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_BEGIN_DT);
		if(l==null)
			return null;
		else
			return ((XSDDateTime) l.getValue()).asCalendar().getTime();
	}

	@Override
	public Date getEndDTAsDate() {
		Literal l = getObjectOfPropertyAsLiteral(KtbsConstants.P_HAS_END_DT);
		if(l==null)
			return null;
		else
			return ((XSDDateTime) l.getValue()).asCalendar().getTime();
	}

	private final class AttributeSelector extends SimpleSelector {
		@Override
		public boolean selects(Statement s) {
			try {
				TraceModel tm = KtbsJenaObsel.this.getObselType().getTraceModel();
				/*
				 * TODO ask the trace model if the predicate is an attribute type
				 */
				throw new UnsupportedOperationException("Not yet implemented");
			} catch(UnsupportedOperationException e) {
				throw new KtbsResourceNotFoundException("TraceModel of type \""+KtbsJenaObsel.this.getObselType()+"\".");
			}
			
		}
	}

	private class AttributeStatementIt implements Iterator<AttributeStatement> {

		private StmtIterator stmtIt;

		AttributeStatementIt(StmtIterator stmtIt) {
			super();
			this.stmtIt = stmtIt;
		}

		@Override
		public boolean hasNext() {
			return stmtIt.hasNext();
		}

		@Override
		public AttributeStatement next() {
			final Statement next = stmtIt.next();

			return new AttributStatementImpl(next);
		}

		@Override
		public void remove() {
			throw new ReadOnlyObjectException(KtbsJenaObsel.this);
		}
	}
}
