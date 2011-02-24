package org.liris.ktbs.core.domain;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Obsel extends KtbsResource {
	
	private ObselType obselType;
	
	private Trace trace;

	private String subject;
	private BigInteger begin;
	private BigInteger end;
	private String beginDT;
	private String endDT;
	
	private Set<Obsel> sourceObsels = new HashSet<Obsel>();
	private Set<RelationStatement> incomingRelations = new HashSet<RelationStatement>();
	private Set<RelationStatement> outgoingRelations = new HashSet<RelationStatement>();

	private Set<AttributePair> 	attributePairs = new HashSet<AttributePair>();

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public BigInteger getBegin() {
		return begin;
	}

	public void setBegin(BigInteger begin) {
		this.begin = begin;
	}

	public BigInteger getEnd() {
		return end;
	}

	public void setEnd(BigInteger end) {
		this.end = end;
	}

	public String getBeginDT() {
		return beginDT;
	}

	public void setBeginDT(String beginDT) {
		this.beginDT = beginDT;
	}

	public String getEndDT() {
		return endDT;
	}

	public void setEndDT(String endDT) {
		this.endDT = endDT;
	}

	public Set<Obsel> getSourceObsels() {
		return sourceObsels;
	}

	public void setSourceObsels(Set<Obsel> sourceObsels) {
		this.sourceObsels = sourceObsels;
	}


	public Set<AttributePair> getAttributePairs() {
		return attributePairs;
	}

	public void setAttributePairs(Set<AttributePair> attributePairs) {
		this.attributePairs = attributePairs;
	}

	public void setObselType(ObselType obselType) {
		this.obselType = obselType;
	}

	public ObselType getObselType() {
		return obselType;
	}

	public void setIncomingRelations(Set<RelationStatement> incomingRelations) {
		this.incomingRelations = incomingRelations;
	}

	public Set<RelationStatement> getIncomingRelations() {
		return incomingRelations;
	}

	public void setOutgoingRelations(Set<RelationStatement> outgoingRelations) {
		this.outgoingRelations = outgoingRelations;
	}

	public Set<RelationStatement> getOutgoingRelations() {
		return outgoingRelations;
	}
	
	public Trace getTrace() {
		return trace;
	}
	
	public void setTrace(Trace trace) {
		this.trace = trace;
	}
}
