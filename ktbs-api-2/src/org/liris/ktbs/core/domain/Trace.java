package org.liris.ktbs.core.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.liris.ktbs.core.domain.interfaces.IComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.ITrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;

public class Trace extends ResourceContainer<IObsel> implements ITrace {
	private String origin;
	private String compliesWithModel;
	private ITraceModel traceModel;
	
	private Set<IComputedTrace> transformedTraces = new HashSet<IComputedTrace>();
	private Set<IObsel> obsels = new HashSet<IObsel>();

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#getOrigin()
	 */
	@Override
	public String getOrigin() {
		return origin;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#setOrigin(java.lang.String)
	 */
	@Override
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#getCompliesWithModel()
	 */
	@Override
	public String getCompliesWithModel() {
		return compliesWithModel;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#setCompliesWithModel(java.lang.String)
	 */
	@Override
	public void setCompliesWithModel(String compliesWithModel) {
		this.compliesWithModel = compliesWithModel;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#getTraceModel()
	 */
	@Override
	public ITraceModel getTraceModel() {
		return traceModel;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#setTraceModel(org.liris.ktbs.core.domain.TraceModel)
	 */
	@Override
	public void setTraceModel(ITraceModel traceModel) {
		this.traceModel = traceModel;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#getTransformedTraces()
	 */
	@Override
	public Set<IComputedTrace> getTransformedTraces() {
		return transformedTraces;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#setTransformedTraces(java.util.Set)
	 */
	@Override
	public void setTransformedTraces(Set<IComputedTrace> transformedTraces) {
		this.transformedTraces = transformedTraces;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#getObsels()
	 */
	@Override
	public Set<IObsel> getObsels() {
		return obsels;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITrace#setObsels(java.util.Set)
	 */
	@Override
	public void setObsels(Set<IObsel> obsels) {
		this.obsels = obsels;
	}

	@Override
	protected Collection<? extends Collection<? extends IObsel>> getContainedResourceCollections() {
		LinkedList<Set<IObsel>> linkedList = new LinkedList<Set<IObsel>>();
		linkedList.add(obsels);
		return linkedList;
	}
}
