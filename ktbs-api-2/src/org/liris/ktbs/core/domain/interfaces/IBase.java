package org.liris.ktbs.core.domain.interfaces;

import java.util.Set;

public interface IBase extends IResourceContainer<IKtbsResource>, IKtbsResource {

	public String getOwner();

	public void setOwner(String owner);

	public Set<ITraceModel> getTraceModels();

	public void setTraceModels(Set<ITraceModel> traceModels);

	public Set<IStoredTrace> getStoredTraces();

	public void setStoredTraces(Set<IStoredTrace> storedTraces);

	public Set<IComputedTrace> getComputedTraces();

	public void setComputedTraces(Set<IComputedTrace> computedTraces);

	public Set<IMethod> getMethods();

	public void setMethods(Set<IMethod> methods);

}