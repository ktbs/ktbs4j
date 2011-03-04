package org.liris.ktbs.domain.interfaces;

import java.util.Set;

public interface IMethod extends IKtbsResource, WithParameters {

	public String getEtag();

	public void setEtag(String etag);

	public String getInherits();

	public void setInherits(String inherits);

	public Set<IMethodParameter> getMethodParameters();

	public void setMethodParameters(Set<IMethodParameter> methodParameters);
}