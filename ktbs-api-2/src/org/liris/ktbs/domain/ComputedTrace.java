package org.liris.ktbs.domain;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IMethodParameter;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.WithParameters;


public class ComputedTrace extends Trace implements IComputedTrace {

	private IMethod method;
	
	private Set<ITrace> sourceTraces = new HashSet<ITrace>();
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IComputedTrace#getMethod()
	 */
	@Override
	public IMethod getMethod() {
		return method;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IComputedTrace#setMethod(org.liris.ktbs.core.domain.Method)
	 */
	@Override
	public void setMethod(IMethod method) {
		this.method = method;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IComputedTrace#getSourceTraces()
	 */
	@Override
	public Set<ITrace> getSourceTraces() {
		return sourceTraces;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IComputedTrace#setSourceTraces(java.util.Set)
	 */
	@Override
	public void setSourceTraces(Set<ITrace> sourceTraces) {
		this.sourceTraces = sourceTraces;
	}
	
	private WithParameters withMethodParameterDelegate = new WithParametersDelegate();
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IComputedTrace#getMethodParameters()
	 */
	@Override
	public Set<IMethodParameter> getMethodParameters() {
		return withMethodParameterDelegate.getMethodParameters();
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IComputedTrace#setMethodParameters(java.util.Set)
	 */
	@Override
	public void setMethodParameters(Set<IMethodParameter> methodParameters) {
		withMethodParameterDelegate.setMethodParameters(methodParameters);
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IComputedTrace#setWithMethodParameterDelegate(org.liris.ktbs.core.domain.WithParameters)
	 */
	public void setWithMethodParameterDelegate(
			WithParameters withMethodParameterDelegate) {
		this.withMethodParameterDelegate = withMethodParameterDelegate;
	}
}
