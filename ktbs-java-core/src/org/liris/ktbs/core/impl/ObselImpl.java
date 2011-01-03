package org.liris.ktbs.core.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;

public class ObselImpl extends KtbsResourceImpl implements Obsel {

	private long begin;
	private long end;
	
	private Date beginDT;
	private Date endDT;
	private String typeURI;
	private String subject;

	private Collection<Relation> incomingRelations;
	private Collection<Relation> outgoingRelations;

	private Map<String, Object> attributes;

	private Trace parentTrace;
	private String traceURI;

	
	ObselImpl(String resourceUri, Trace parentTrace, String subject, Date beginDT, Date endDT, long begin, long end, String typeURI, Map<String, Object> attributes) {
		super(resourceUri);

		this.parentTrace = parentTrace;
		this.beginDT = beginDT;
		this.endDT = endDT;
		this.typeURI = typeURI;

		this.subject = subject;
		
		this.attributes = new HashMap<String, Object>();
		if(attributes!=null) 
			this.attributes.putAll(attributes);

		this.begin = begin;
		this.end = end;
		
		this.incomingRelations = new LinkedList<Relation>();
		this.outgoingRelations = new LinkedList<Relation>();
	}

	ObselImpl(String obselURI, String traceURI, String subject, Date beginDT, Date endDT, long begin, long end,
			String typeURI2, Map<String, Object> attributes2) {
		this(obselURI, (Trace)null, subject, beginDT, endDT,begin, end, typeURI2, attributes2);
		this.traceURI = traceURI;
	}

	@Override
	public Date getBeginDT() {
		return beginDT;
	}

	@Override
	public Date getEndDT() {
		return endDT;
	}

	@Override
	public String getTypeURI() {
		return typeURI;
	}

	@Override
	public Collection<Relation> getIncomingRelations() {
		return Collections.unmodifiableCollection(incomingRelations);
	}

	@Override
	public Collection<Relation> getOutgoingRelations() {
		return Collections.unmodifiableCollection(outgoingRelations);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		return this.attributes.get(attributeName);
	}

	@Override
	public Trace getTrace() {
		return parentTrace;
	}

	@Override
	public Obsel getTargetObsel(String relationName) {
		for(Relation rel:outgoingRelations) {
			if(rel.getRelationName().equals(relationName))
				return rel.getToObsel();
		}
		return null;
	}

	@Override
	public Obsel getSourceObsel(String relationName) {
		for(Relation rel:incomingRelations) {
			if(rel.getRelationName().equals(relationName))
				return rel.getFromObsel();
		}
		return null;
	}

	@Override
	public void addOutgoingRelation(Relation relation) {
		if(relation == null || (relation.getFromObsel() == null  && relation.getFromObselURI()==null)) 
			throw new IllegalStateException("Invalid source obsel for the relation \"" + relation + "\".");
		this.outgoingRelations.add(relation);

	}

	@Override
	public void addIncomingRelation(Relation relation) {
		if(relation == null || (relation.getToObsel() == null  && relation.getToObselURI()==null)) 
			throw new IllegalStateException("Invalid target obsel for the relation \"" + relation + "\".");
		this.incomingRelations.add(relation);
	}

	@Override
	public String getTraceURI() {
		if(traceURI != null)
			return traceURI;
		if(parentTrace != null)
			traceURI = parentTrace.getURI();
		return traceURI;
	}
	
	@Override
	public String getSubject() {
		return subject;
	}

	@Override
	public long getBegin() {
		return begin;
	}

	@Override
	public long getEnd() {
		return end;
	}

}
