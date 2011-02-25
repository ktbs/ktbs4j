package org.liris.ktbs.core.domain.interfaces;

import java.util.Set;


public interface ITrace extends IKtbsResource, IResourceContainer<IObsel> {

	public String getOrigin();

	public void setOrigin(String origin);

	public String getCompliesWithModel();

	public void setCompliesWithModel(String compliesWithModel);

	public ITraceModel getTraceModel();

	public void setTraceModel(ITraceModel traceModel);

	public Set<ITrace> getTransformedTraces();

	public void setTransformedTraces(Set<ITrace> transformedTraces);

	public Set<IObsel> getObsels();

	public void setObsels(Set<IObsel> obsels);

}