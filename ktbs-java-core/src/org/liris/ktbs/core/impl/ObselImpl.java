package org.liris.ktbs.core.impl;

import java.io.Serializable;
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

	private Date begin;
	private Date end;
	private String typeURI;

	private Collection<Relation> incomingRelations;
	private Collection<Relation> outgoingRelations;

	private Map<String, Serializable> attributes;

	private Trace parentTrace;
	private String traceURI;

	ObselImpl(String resourceUri, Trace parentTrace, Date begin, Date end, String typeURI, Map<String, Serializable> attributes) {
		super(resourceUri);


		this.parentTrace = parentTrace;
		this.begin = begin;
		this.end = end;
		this.typeURI = typeURI;

		this.attributes = new HashMap<String, Serializable>();
		if(attributes!=null) 
			this.attributes.putAll(attributes);

		this.incomingRelations = new LinkedList<Relation>();
		this.outgoingRelations = new LinkedList<Relation>();
	}

	ObselImpl(String obselURI, String traceURI, Date begin2, Date end2,
			String typeURI2, Map<String, Serializable> attributes2) {
		this(obselURI, (Trace)null, begin2, end2, typeURI2, attributes2);
		this.traceURI = traceURI;
	}

	@Override
	public Date getBegin() {
		return begin;
	}

	@Override
	public Date getEnd() {
		return end;
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
	public Map<String, Serializable> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	@Override
	public Serializable getAttributeValue(String attributeName) {
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
		if(relation == null || relation.getFromObsel() == null  || !relation.getFromObsel().getURI().equals(this.getURI())) 
			throw new IllegalStateException("Invalid source obsel for the relation \"" + relation + "\". Expected: \"" + this.getURI() + "\", actual:\"" + relation.getFromObsel() +"\".");
		this.outgoingRelations.add(relation);

	}

	@Override
	public void addIncomingRelation(Relation relation) {
		if(relation == null || relation.getToObsel() == null  || !relation.getToObsel().getURI().equals(this.getURI())) 
			throw new IllegalStateException("Invalid target obsel for the relation \"" + relation + "\". Expected: \"" + this.getURI() + "\", actual:\"" + relation.getToObsel() +"\".");
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

}
