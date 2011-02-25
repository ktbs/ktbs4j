package org.liris.ktbs.core.domain;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.core.domain.interfaces.IAttributePair;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationStatement;
import org.liris.ktbs.core.domain.interfaces.ITrace;

public class Obsel extends KtbsResource implements IObsel {
	
	private IObselType obselType;
	
	private ITrace trace;

	private String subject;
	private BigInteger begin;
	private BigInteger end;
	private String beginDT;
	private String endDT;
	
	private Set<IObsel> sourceObsels = new HashSet<IObsel>();
	private Set<IRelationStatement> incomingRelations = new HashSet<IRelationStatement>();
	private Set<IRelationStatement> outgoingRelations = new HashSet<IRelationStatement>();

	private Set<IAttributePair> 	attributePairs = new HashSet<IAttributePair>();

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getSubject()
	 */
	@Override
	public String getSubject() {
		return subject;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setSubject(java.lang.String)
	 */
	@Override
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getBegin()
	 */
	@Override
	public BigInteger getBegin() {
		return begin;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setBegin(java.math.BigInteger)
	 */
	@Override
	public void setBegin(BigInteger begin) {
		this.begin = begin;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getEnd()
	 */
	@Override
	public BigInteger getEnd() {
		return end;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setEnd(java.math.BigInteger)
	 */
	@Override
	public void setEnd(BigInteger end) {
		this.end = end;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getBeginDT()
	 */
	@Override
	public String getBeginDT() {
		return beginDT;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setBeginDT(java.lang.String)
	 */
	@Override
	public void setBeginDT(String beginDT) {
		this.beginDT = beginDT;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getEndDT()
	 */
	@Override
	public String getEndDT() {
		return endDT;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setEndDT(java.lang.String)
	 */
	@Override
	public void setEndDT(String endDT) {
		this.endDT = endDT;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getSourceObsels()
	 */
	@Override
	public Set<IObsel> getSourceObsels() {
		return sourceObsels;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setSourceObsels(java.util.Set)
	 */
	@Override
	public void setSourceObsels(Set<IObsel> sourceObsels) {
		this.sourceObsels = sourceObsels;
	}


	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getAttributePairs()
	 */
	@Override
	public Set<IAttributePair> getAttributePairs() {
		return attributePairs;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setAttributePairs(java.util.Set)
	 */
	@Override
	public void setAttributePairs(Set<IAttributePair> attributePairs) {
		this.attributePairs = attributePairs;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setObselType(org.liris.ktbs.core.domain.ObselType)
	 */
	@Override
	public void setObselType(IObselType obselType) {
		this.obselType = obselType;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getObselType()
	 */
	@Override
	public IObselType getObselType() {
		return obselType;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setIncomingRelations(java.util.Set)
	 */
	@Override
	public void setIncomingRelations(Set<IRelationStatement> incomingRelations) {
		this.incomingRelations = incomingRelations;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getIncomingRelations()
	 */
	@Override
	public Set<IRelationStatement> getIncomingRelations() {
		return incomingRelations;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setOutgoingRelations(java.util.Set)
	 */
	@Override
	public void setOutgoingRelations(Set<IRelationStatement> outgoingRelations) {
		this.outgoingRelations = outgoingRelations;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getOutgoingRelations()
	 */
	@Override
	public Set<IRelationStatement> getOutgoingRelations() {
		return outgoingRelations;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#getTrace()
	 */
	@Override
	public ITrace getTrace() {
		return trace;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObsel#setTrace(org.liris.ktbs.core.domain.ITrace)
	 */
	@Override
	public void setTrace(ITrace trace) {
		this.trace = trace;
	}
}
