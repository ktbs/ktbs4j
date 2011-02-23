package org.liris.ktbs.core.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.AttributePair;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationStatement;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.Trace;

public class ObselImpl extends ResourceImpl implements Obsel {

	protected ObselImpl(String uri) {
		super(uri);
	}

	@Override
	public Trace getTrace() {
		return (Trace)getParentResource();
	}

	private BigInteger begin;
	private BigInteger end;
	private String beginDT;
	private String endDT;
	private String subject;
	
	private LinkedResourceDelegate<ObselType> obselTypeDelegate = new LinkedResourceDelegate<ObselType>(manager);
	
	private Set<AttributePair> attributePairs = new HashSet<AttributePair>();
	private Set<RelationStatement> outgoingRelations = new HashSet<RelationStatement>();
	private Set<RelationStatement> incomingRelations = new HashSet<RelationStatement>();
	
	private ResourceCollectionDelegate<Obsel> transformationSourceObselDelegate = new ResourceCollectionDelegate<Obsel>(manager);
	
	@Override
	public BigInteger getBegin() {
		return begin;
	}

	@Override
	public void setBegin(BigInteger begin) {
		this.begin = begin;
	}

	@Override
	public BigInteger getEnd() {
		return end;
	}

	@Override
	public void setEnd(BigInteger end) {
		this.end = end;
	}

	@Override
	public String getBeginDT() {
		return beginDT;
	}

	@Override
	public String getEndDT() {
		return endDT;
	}

	@Override
	public void setBeginDT(String beginDT) {
		this.beginDT = beginDT;
	}

	@Override
	public void setEndDT(String endDT) {
		this.endDT = endDT;
	}
	
	@Override
	public ObselType getObselType() {
		return obselTypeDelegate.get();
	}

	@Override
	public void setObselType(ObselType type) {
		obselTypeDelegate.set(type);
	}

	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	private class AttributePairURI implements AttributePair {

		private String attributeTypeURI;
		private Object value;
		
		private AttributePairURI(String attributeTypeURI, Object value) {
			super();
			this.attributeTypeURI = attributeTypeURI;
			this.value = value;
		}

		@Override
		public AttributeType getAttributeType() {
			return (AttributeType) ObselImpl.this.manager.getKtbsResource(attributeTypeURI);
		}

		@Override
		public Object getValue() {
			return value;
		}
	}
	
	
	@Override
	public Iterator<AttributePair> listAttributes() {
		return attributePairs.iterator();
	}

	@Override
	public Object getAttributeValue(AttributeType attribute) {
		return getAttributeValue(attribute.getUri());
	}

	@Override
	public Collection<Object> getAttributeValues(AttributeType attribute) {
		return getAttributeValues(attribute.getUri());
	}

	@Override
	public Object getAttributeValue(String attributeTypeURI) {
		for(AttributePair pair:attributePairs) {
			if(pair.getAttributeType().getUri().equals(attributeTypeURI))
				return pair.getValue();
		}
		return null;
	}

	@Override
	public Collection<Object> getAttributeValues(String attributeTypeURI) {
		Set<Object> values = new HashSet<Object>();
		for(AttributePair pair:attributePairs) {
			if(pair.getAttributeType().getUri().equals(attributeTypeURI))
				values.add(pair.getValue());
		}
		return values;
	}

	@Override
	public void addAttribute(AttributeType attribute, Object value) {
		attributePairs.add(new AttributePairURI(attribute.getUri(), value));
	}

	@Override
	public void addAttribute(String attributeTypeURI, Object value) {
		attributePairs.add(new AttributePairURI(attributeTypeURI, value));
	}

	private class RelationStatementURI implements RelationStatement  {
		private String fromObselURI;
		private String relationURI;
		private String toObselURI;
		
		public RelationStatementURI(String fromObselURI, String relationURI,
				String toObselURI) {
			super();
			this.fromObselURI = fromObselURI;
			this.relationURI = relationURI;
			this.toObselURI = toObselURI;
		}

		@Override
		public Obsel getFromObsel() {
			return (Obsel) ObselImpl.this.manager.getKtbsResource(fromObselURI);
		}

		@Override
		public RelationType getRelation() {
			return (RelationType) ObselImpl.this.manager.getKtbsResource(relationURI);
		}

		@Override
		public Obsel getToObsel() {
			return (Obsel) ObselImpl.this.manager.getKtbsResource(toObselURI);
		}
		
	}
	
	
	@Override
	public void addOutgoingRelation(RelationType relationType, Obsel target) {
		outgoingRelations.add(new RelationStatementURI(uri, relationType.getUri(), target.getUri()));
	}

	@Override
	public void addIncomingRelation(Obsel source, RelationType relationType) {
		incomingRelations.add(new RelationStatementURI(source.getUri(), relationType.getUri(), uri));
	}

	@Override
	public Iterator<RelationStatement> listIncomingRelations() {
		return incomingRelations.iterator();
	}

	@Override
	public Iterator<RelationStatement> listOutgoingRelations() {
		return outgoingRelations.iterator();
	}

	@Override
	public Obsel getTargetObsel(RelationType relationType) {
		return getTargetObsel(relationType.getUri());
	}

	@Override
	public Obsel getTargetObsel(String relationTypeUri) {
		for(RelationStatement stmt:outgoingRelations) {
			if(stmt.getRelation().getUri().equals(relationTypeUri))
				return stmt.getToObsel();
		}
		return null;
	}

	@Override
	public Obsel getSourceObsel(RelationType relationType) {
		return getSourceObsel(relationType.getUri());
	}

	@Override
	public Obsel getSourceObsel(String relationTypeUri) {
		for(RelationStatement stmt:incomingRelations) {
			if(stmt.getRelation().getUri().equals(relationTypeUri))
				return stmt.getFromObsel();
		}
		return null;
	}

	@Override
	public Collection<Obsel> getTargetObsels(RelationType relationType) {
		return getTargetObsels(relationType.getUri());
	}

	@Override
	public Collection<Obsel> getTargetObsels(String relationTypeUri) {
		Set<Obsel> targets = new HashSet<Obsel>();
		for(RelationStatement stmt:outgoingRelations) {
			if(stmt.getRelation().getUri().equals(relationTypeUri))
				targets.add(stmt.getToObsel());
		}
		return targets;
	}

	@Override
	public Collection<Obsel> getSourceObsels(RelationType relationType) {
		return getSourceObsels(relationType.getUri());
	}

	@Override
	public Collection<Obsel> getSourceObsels(String relationTypeUri) {
		Set<Obsel> sources = new HashSet<Obsel>();
		for(RelationStatement stmt:incomingRelations) {
			if(stmt.getRelation().getUri().equals(relationTypeUri))
				sources.add(stmt.getFromObsel());
		}
		return sources;
	}
	
	@Override
	public Iterator<Obsel> listTransformationSourceObsels() {
		return transformationSourceObselDelegate.list();
	}
}
