package org.liris.ktbs.domain;

import java.util.Set;

import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IMethodParameter;
import org.liris.ktbs.domain.interfaces.WithParameters;


@SuppressWarnings("serial")
public class Method extends KtbsResource implements IMethod {

	private String etag;
	private String inherits;

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IMethod#getEtag()
	 */
	@Override
	public String getEtag() {
		return etag;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IMethod#setEtag(java.lang.String)
	 */
	@Override
	public void setEtag(String etag) {
		this.etag = etag;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IMethod#getInherits()
	 */
	@Override
	public String getInherits() {
		return inherits;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IMethod#setInherits(java.lang.String)
	 */
	@Override
	public void setInherits(String inherits) {
		this.inherits = inherits;
	}

	private WithParameters withMethodParameterDelegate = new WithParametersDelegate();

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IMethod#getMethodParameters()
	 */
	@Override
	public Set<IMethodParameter> getMethodParameters() {
		return withMethodParameterDelegate.getMethodParameters();
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IMethod#setMethodParameters(java.util.Set)
	 */
	@Override
	public void setMethodParameters(Set<IMethodParameter> methodParameters) {
		withMethodParameterDelegate.setMethodParameters(methodParameters);
	}
	
	public void setWithMethodParameterDelegate(
			WithParameters withMethodParameterDelegate) {
		this.withMethodParameterDelegate = withMethodParameterDelegate;
	}
}
