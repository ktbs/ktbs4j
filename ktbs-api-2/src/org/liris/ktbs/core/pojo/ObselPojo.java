package org.liris.ktbs.core.pojo;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class ObselPojo extends ResourcePojo {
	
	private ObselTypePojo obselType;
	
	private String subject;
	private BigInteger begin;
	private BigInteger end;
	private String beginDT;
	private String endDT;
	
	private Set<ObselPojo> sourceObsels = new HashSet<ObselPojo>();
	private Set<RelationStatementPojo> incomingRelations = new HashSet<RelationStatementPojo>();
	private Set<RelationStatementPojo> outgoingRelations = new HashSet<RelationStatementPojo>();

	private Set<AttributePairPojo> 	attributePairs;

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

	public Set<ObselPojo> getSourceObsels() {
		return sourceObsels;
	}

	public void setSourceObsels(Set<ObselPojo> sourceObsels) {
		this.sourceObsels = sourceObsels;
	}


	public Set<AttributePairPojo> getAttributePairs() {
		return attributePairs;
	}

	public void setAttributePairs(Set<AttributePairPojo> attributePairs) {
		this.attributePairs = attributePairs;
	}

	public void setObselType(ObselTypePojo obselType) {
		this.obselType = obselType;
	}

	public ObselTypePojo getObselType() {
		return obselType;
	}

	public void setIncomingRelations(Set<RelationStatementPojo> incomingRelations) {
		this.incomingRelations = incomingRelations;
	}

	public Set<RelationStatementPojo> getIncomingRelations() {
		return incomingRelations;
	}

	public void setOutgoingRelations(Set<RelationStatementPojo> outgoingRelations) {
		this.outgoingRelations = outgoingRelations;
	}

	public Set<RelationStatementPojo> getOutgoingRelations() {
		return outgoingRelations;
	}
}
