package org.liris.ktbs.domain.interfaces;

import java.math.BigInteger;
import java.util.Set;

public interface IObsel extends IKtbsResource {

	public String getSubject();

	public void setSubject(String subject);

	public BigInteger getBegin();

	public void setBegin(BigInteger begin);

	public BigInteger getEnd();

	public void setEnd(BigInteger end);

	public String getBeginDT();

	public void setBeginDT(String beginDT);

	public String getEndDT();

	public void setEndDT(String endDT);

	public Set<IObsel> getSourceObsels();

	public void setSourceObsels(Set<IObsel> sourceObsels);

	public Set<IAttributePair> getAttributePairs();

	public void setAttributePairs(Set<IAttributePair> attributePairs);

	public void setObselType(IObselType obselType);

	public IObselType getObselType();

	public void setIncomingRelations(Set<IRelationStatement> incomingRelations);

	public Set<IRelationStatement> getIncomingRelations();

	public void setOutgoingRelations(Set<IRelationStatement> outgoingRelations);

	public Set<IRelationStatement> getOutgoingRelations();

}