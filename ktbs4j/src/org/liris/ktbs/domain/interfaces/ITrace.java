package org.liris.ktbs.domain.interfaces;

import java.math.BigInteger;
import java.util.Set;


public interface ITrace extends IKtbsResource, IResourceContainer<IObsel> {

	public BigInteger getTraceBegin();
	public String getTraceBeginDT();
	public BigInteger getTraceEnd();
	public String getTraceEndDT();
	public void setTraceBegin(BigInteger traceBegin);
	public void setTraceBeginDT(String traceBeginDT);
	public void setTraceEnd(BigInteger traceEnd);
	public void setTraceEndDT(String traceEndDT);
	
	public String getOrigin();

	public void setOrigin(String origin);

	public String getCompliesWithModel();

	public void setCompliesWithModel(String compliesWithModel);

	public ITraceModel getTraceModel();

	public void setTraceModel(ITraceModel traceModel);

	public Set<IComputedTrace> getTransformedTraces();

	public void setTransformedTraces(Set<IComputedTrace> transformedTraces);

	public Set<IObsel> getObsels();

	public void setObsels(Set<IObsel> obsels);

}