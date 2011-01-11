package org.liris.ktbs.rdf.resource;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.DomainException;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResourceNotFoundException;
import org.liris.ktbs.core.Mode;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RangeException;
import org.liris.ktbs.core.RelationStatement;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.SimpleRelationStatement;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

public class KtbsJenaObsel extends KtbsJenaResource implements Obsel {

	KtbsJenaObsel(String uri, Model rdfModel, KtbsJenaResourceHolder holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public Trace getTrace() {
		return getObjectOfPropertyAsKtbsResourceIfExists(KtbsConstants.P_HAS_TRACE, Trace.class, false);
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
		return getObjectStringOrNull(KtbsConstants.P_HAS_BEGIN_DT);
	}

	@Override
	public String getEndDT() {
		return getObjectStringOrNull(KtbsConstants.P_HAS_END_DT);
	}

	@Override
	public ObselType getObselType() {
		return getObjectOfPropertyAsKtbsResourceIfExists(RDF.type.getURI(), ObselType.class, false);
	}


	@Override
	public String getSubject() {
		return getObjectStringOrNull(KtbsConstants.P_HAS_SUBJECT);
	}

	@Override
	public Iterator<AttributeStatement> listAttributes() {
		StmtIterator stmtIt = rdfModel.listStatements(new AttributeSelector(null));
		return new AttributeStatementIt(stmtIt);
	}

	@Override
	public Object getAttributeValue(AttributeType attribute) {
		StmtIterator it = rdfModel.listStatements(new AttributeSelector(attribute.getURI()));
		try  {

			if(!it.hasNext())
				return null;
			else
				return new AttributeStatementIt(it).next().getValue();
		} catch(KtbsResourceNotFoundException e) {
			// the obsel type could not be retrieved
			return null;
		}

	}

	@Override
	public Iterator<RelationStatement> listIncomingRelations() {
		StmtIterator it = rdfModel.listStatements(new IncomingRelationSelector(null));
		return new RelationStatementIt(it, false);
	}

	@Override
	public Iterator<RelationStatement> listOutgoingRelations() {
		StmtIterator it = rdfModel.listStatements(new OutgoingRelationSelector(null));
		return new RelationStatementIt(it, true);
	}

	@Override
	public Obsel getTargetObsel(RelationType relationType) {
		StmtIterator it = rdfModel.listStatements(new OutgoingRelationSelector(relationType.getURI()));
		if(!it.hasNext())
			return null;
		else
			return new RelationStatementIt(it, true).next().getToObsel();
	}

	@Override
	public Obsel getSourceObsel(RelationType relationType) {
		StmtIterator it = rdfModel.listStatements(new IncomingRelationSelector(relationType.getURI()));
		if(!it.hasNext())
			return null;
		else
			return new RelationStatementIt(it, false).next().getFromObsel();
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

	private final class AttributeSelector extends RelationOrAttributeSelector {
		public AttributeSelector(String attributeName) {
			super(attributeName, true);
		}

		@Override
		public boolean selects(Statement s) {
			if(!super.selects(s))
				return false;

			if(!holder.existsOfType(s.getPredicate().getURI(), AttributeType.class))
				// This is a relation
				return false;

			AttributeType asAttributeType = holder.getAfterCheck(s.getPredicate().getURI(), AttributeType.class);
			ObselType obselType = KtbsJenaObsel.this.getObselType();
			LinkedList<AttributeType> c = KtbsUtils.toLinkedList(obselType.listAttributes(Mode.INFERRED));
			boolean isAttribute = c.contains(asAttributeType);
			return isAttribute;
		}
	}

	

	private abstract class RelationOrAttributeSelector extends SimpleSelector {
		/*
		 * A null attributeName works matches with any attribute
		 */
		protected String relationName;
		private boolean outGoing;
		public RelationOrAttributeSelector(String relationName, boolean outGoing) {
			super();
			this.relationName = relationName;
			this.outGoing = outGoing;
		}

		@Override
		public boolean selects(Statement s) {
			if(relationName != null && !s.getPredicate().getURI().equals(relationName))
				return false;

			/*
			 *  relationName is null, WILDCARD
			 */
			Resource thisAsResource;
			if(outGoing)
				thisAsResource = s.getSubject();
			else {
				if(!s.getObject().isResource())
					return false;
				else 
					thisAsResource = s.getObject().asResource();
			}
			boolean sameURI = thisAsResource.getURI().equals(KtbsJenaObsel.this.uri);
			if(!sameURI)
				return false;
			return true;
		}
	}

	private final class OutgoingRelationSelector extends RelationOrAttributeSelector {
		public OutgoingRelationSelector(String relationName) {
			super(relationName, true);
		}

		@Override
		public boolean selects(Statement s) {
			if(!super.selects(s))
				return false;

			if(!holder.existsOfType(s.getPredicate().getURI(), RelationType.class))
				// This is an attribute
				return false;

			RelationType asRelationType = holder.getAfterCheck(s.getPredicate().getURI(), RelationType.class);
			LinkedList<RelationType> c = KtbsUtils.toLinkedList(KtbsJenaObsel.this.getObselType().listOutgoingRelations(Mode.INFERRED));
			boolean isOutgoingRelation = c.contains(asRelationType);
			return isOutgoingRelation;
		}
	}
	
	private final class IncomingRelationSelector extends RelationOrAttributeSelector {
		public IncomingRelationSelector(String relationName) {
			super(relationName, false);
		}

		@Override
		public boolean selects(Statement s) {
			if(!super.selects(s))
				return false;

			if(!holder.existsOfType(s.getPredicate().getURI(), RelationType.class))
				// This is an attribute
				return false;

			RelationType asRelationType = holder.getAfterCheck(s.getPredicate().getURI(), RelationType.class);
			LinkedList<RelationType> c = KtbsUtils.toLinkedList(KtbsJenaObsel.this.getObselType().listIncomingRelations(Mode.INFERRED));
			boolean isIncomingRelation = c.contains(asRelationType);
			return isIncomingRelation;
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

			return new SimpleAttributStatement(
					KtbsJenaObsel.this.holder.getResource(next.getPredicate().asResource().getURI(), AttributeType.class),
					next.getObject().asLiteral().getValue()
			);
		}

		@Override
		public void remove() {
			stmtIt.remove();
		}
	}

	private class RelationStatementIt implements Iterator<RelationStatement> {

		private StmtIterator stmtIt;
		private boolean isOutgoing;

		RelationStatementIt(StmtIterator stmtIt, boolean isOutgoing) {
			super();
			this.stmtIt = stmtIt;
			this.isOutgoing = isOutgoing;
		}

		@Override
		public boolean hasNext() {
			return stmtIt.hasNext();
		}

		@Override
		public RelationStatement next() {
			final Statement next = stmtIt.next();


			Obsel thisObsel = KtbsJenaObsel.this.holder.getResourceAlreadyInModel(
					KtbsJenaObsel.this.uri, 
					Obsel.class, 
					KtbsJenaObsel.this.rdfModel);

			Obsel otherObsel =  KtbsJenaObsel.this.holder.getResourceAlreadyInModel(
					isOutgoing?next.getObject().asResource().getURI():next.getResource().getURI(), 
							Obsel.class, 
							KtbsJenaObsel.this.rdfModel);

			return new SimpleRelationStatement(
					isOutgoing?thisObsel:otherObsel, 
							KtbsJenaObsel.this.holder.getAfterCheck(next.getPredicate().getURI(),RelationType.class), 
							isOutgoing?otherObsel:thisObsel
			);

		}

		@Override
		public void remove() {
			stmtIt.remove();
		}
	}

	@Override
	public Obsel getSourceObsel() {
		Resource res = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_SOURCE_OBSEL);
		if(res==null)
			return null;
		return holder.getAfterCheck(res.getURI(), Obsel.class);
	}

	@Override
	public void setBegin(long begin) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_BEGIN, begin);
	}

	@Override
	public void setEnd(long end) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_END, end);
	}

	@Override
	public void setBeginDT(String beginDT) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_BEGIN_DT, beginDT);
	}

	@Override
	public void setEndDT(String endDT) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_END_DT, endDT);
	}

	@Override
	public void setObselType(ObselType type) {
		checkExitsenceAndAddResource(RDF.type.getURI(), type);
	}

	@Override
	public void setSubject(String subject) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_SUBJECT, subject);
	}

	@Override
	public void addAttribute(AttributeType attribute, Object value) {

		if(!holder.exists(attribute.getURI()))
			throw new KtbsResourceNotFoundException(attribute.getURI());
		else {
			AttributeType attType = holder.getResource(attribute.getURI(), AttributeType.class);
			ObselType obselType = getObselType();
			ObselType attDomain = attType.getDomain();
			checkDomainOrRange(obselType, attDomain, true);
			addLiteral(attribute.getURI(), value);
		}
	}

	private void checkDomainOrRange(ObselType obselType, ObselType attDomain, boolean domain) {
		if(!(obselType.equals(attDomain) || obselType.hasSuperType(attDomain, Mode.INFERRED))) {
			if(domain)
				throw new DomainException(attDomain, obselType);
			else
				throw new RangeException(attDomain, obselType);

		}
	}

	@Override
	public void addOutgoingRelation(RelationType relationType, Obsel target) {
		if(!holder.exists(relationType.getURI()))
			throw new KtbsResourceNotFoundException(relationType.getURI());
		if(!holder.exists(target.getURI()))
			throw new KtbsResourceNotFoundException(target.getURI());

		ObselType obselType = getObselType();
		ObselType relDomain = relationType.getDomain();
		checkDomainOrRange(obselType, relDomain, true);
		addResource(relationType.getURI(), target.getURI());
	}

	@Override
	public void addIncomingRelation(Obsel source, RelationType relationType) {
		if(!holder.exists(relationType.getURI()))
			throw new KtbsResourceNotFoundException(relationType.getURI());
		if(!holder.exists(source.getURI()))
			throw new KtbsResourceNotFoundException(source.getURI());

		ObselType obselType = getObselType();
		ObselType relrange = relationType.getRange();
		checkDomainOrRange(obselType, relrange, false);
		
		rdfModel.getResource(source.getURI()).addProperty(
				rdfModel.getProperty(relationType.getURI()),
				rdfModel.getResource(uri));
	}

	@Override
	public void setBeginDTAsDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		XSDDateTime x = new XSDDateTime(cal);
		removeAllAndAddLiteral(KtbsConstants.P_HAS_BEGIN_DT, x);
	}

	@Override
	public void setEndDTAsDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		XSDDateTime x = new XSDDateTime(cal);
		removeAllAndAddLiteral(KtbsConstants.P_HAS_END_DT, x);
	}
	
	@Override
	public String toString() {
		return getURI();
	}
}

