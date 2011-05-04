package org.liris.ktbs.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;


@SuppressWarnings("serial")
public class Base extends ResourceContainer<IKtbsResource> implements IBase {

	private String owner;
	private Set<ITraceModel> traceModels = new HashSet<ITraceModel>();
	private Set<IStoredTrace> storedTraces = new HashSet<IStoredTrace>();
	private Set<IComputedTrace> computedTraces = new HashSet<IComputedTrace>();
	private Set<IMethod> methods = new HashSet<IMethod>();
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#getOwner()
	 */
	@Override
	public String getOwner() {
		return owner;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#setOwner(java.lang.String)
	 */
	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#getTraceModels()
	 */
	@Override
	public Set<ITraceModel> getTraceModels() {
		return traceModels;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#setTraceModels(java.util.Set)
	 */
	@Override
	public void setTraceModels(Set<ITraceModel> traceModels) {
		this.traceModels = traceModels;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#getStoredTraces()
	 */
	@Override
	public Set<IStoredTrace> getStoredTraces() {
		return storedTraces;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#setStoredTraces(java.util.Set)
	 */
	@Override
	public void setStoredTraces(Set<IStoredTrace> storedTraces) {
		this.storedTraces = storedTraces;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#getComputedTraces()
	 */
	@Override
	public Set<IComputedTrace> getComputedTraces() {
		return computedTraces;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#setComputedTraces(java.util.Set)
	 */
	@Override
	public void setComputedTraces(Set<IComputedTrace> computedTraces) {
		this.computedTraces = computedTraces;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#getMethods()
	 */
	@Override
	public Set<IMethod> getMethods() {
		return methods;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IBase#setMethods(java.util.Set)
	 */
	@Override
	public void setMethods(Set<IMethod> methods) {
		this.methods = methods;
	}

	@Override
	protected Collection<? extends Collection<? extends IKtbsResource>> getContainedResourceCollections() {
		List<Set<? extends IKtbsResource>> c = new LinkedList<Set<? extends IKtbsResource>>();
		c.add(storedTraces);
		c.add(computedTraces);
		c.add(methods);
		c.add(traceModels);
		return c;
	}

}
