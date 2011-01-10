package org.liris.ktbs.rdf.resource;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.liris.ktbs.core.AttributeStatement;
import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsResourceHolder;
import org.liris.ktbs.core.KtbsResourceNotFoundException;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationStatement;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.SimpleRelationStatement;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.rdf.KtbsConstants;
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

	KtbsJenaObsel(String uri, Model rdfModel, KtbsResourceHolder holder) {
		super(uri, rdfModel, holder);
	}

	@Override
	public Trace getTrace() {
		Resource r = getObjectOfPropertyAsResource(KtbsConstants.P_HAS_TRACE);
		if(r==null)
			return null;
		else
			return holder.getResource(r.getURI(), Trace.class);
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
			return holder.getResource(r.getURI(), ObselType.class);
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
		StmtIterator stmtIt = rdfModel.listStatements(new AttributeSelector(null));

		return new AttributeStatementIt(stmtIt);
	}

	@Override
	public Object getAttributeValue(AttributeType attribute) {
		StmtIterator it = rdfModel.listStatements(new AttributeSelector(attribute.getURI()));
		if(!it.hasNext())
			return null;
		else
			return new AttributeStatementIt(it).next().getValue();

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
	public Obsel getTargetObsel(String relationName) {
		StmtIterator it = rdfModel.listStatements(new OutgoingRelationSelector(relationName));
		if(!it.hasNext())
			return null;
		else
			return new RelationStatementIt(it, true).next().getToObsel();
	}

	@Override
	public Obsel getSourceObsel(String relationName) {
		StmtIterator it = rdfModel.listStatements(new IncomingRelationSelector(relationName));
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

			AttributeType asAttributeType = holder.getResource(s.getPredicate().getURI(), AttributeType.class);
			LinkedList<AttributeType> c = KtbsUtils.toLinkedList(KtbsJenaObsel.this.getObselType().listAttributes());
			boolean isAttribute = c.contains(asAttributeType);
			return isAttribute;
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

			RelationType asRelationType = holder.getResource(s.getPredicate().getURI(), RelationType.class);
			LinkedList<RelationType> c = KtbsUtils.toLinkedList(KtbsJenaObsel.this.getObselType().listOutgoingRelations());
			boolean isOutgoingRelation = c.contains(asRelationType);
			return isOutgoingRelation;

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
			Resource thisAsResource = outGoing?
					s.getSubject():
						s.getObject().asResource();
					boolean sameURI = thisAsResource.getURI().equals(KtbsJenaObsel.this.uri);
					if(!sameURI)
						return false;
					return true;
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

			RelationType asRelationType = holder.getResource(s.getPredicate().getURI(), RelationType.class);
			LinkedList<RelationType> c = KtbsUtils.toLinkedList(KtbsJenaObsel.this.getObselType().listIncomingRelations());
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
							next.getPredicate().getURI(), 
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
		return holder.getResource(res.getURI(), Obsel.class);
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
	public void setSourceObsel(Obsel obsel) {
		if(!holder.exists(obsel.getURI()))
			throw new KtbsResourceNotFoundException(obsel.getURI());
		removeAllAndAddResource(KtbsConstants.P_HAS_SOURCE_OBSEL, obsel.getURI());
	}

	@Override
	public void setSubject(String subject) {
		removeAllAndAddLiteral(KtbsConstants.P_HAS_SUBJECT, subject);
	}

	@Override
	public void addAttribute(AttributeType attribute, Object value) {
		if(!holder.exists(attribute.getURI()))
			throw new KtbsResourceNotFoundException(attribute.getURI());
		addLiteral(attribute.getURI(), value);
	}

	@Override
	public void addOutgoingRelation(RelationType relationType, Obsel target) {
		if(!holder.exists(relationType.getURI()))
			throw new KtbsResourceNotFoundException(relationType.getURI());
		if(!holder.exists(target.getURI()))
			throw new KtbsResourceNotFoundException(target.getURI());
		addResource(relationType.getURI(), target.getURI());
	}

	@Override
	public void addIncomingRelation(Obsel source, RelationType relationType) {
		if(!holder.exists(source.getURI()))
			throw new KtbsResourceNotFoundException(source.getURI());
		else
			source.addOutgoingRelation(relationType, source);
	}
}

