package org.liris.ktbs.domain.interfaces;

import java.util.Set;

import org.liris.ktbs.client.KtbsConstants;

public interface IMethod extends IKtbsResource, WithParameters {

	public static final String FILTER = KtbsConstants.FILTER;
	public static final String FUSION = KtbsConstants.FUSION;
	public static final String SPARQL = KtbsConstants.SPARQL;
	public static final String SCRIPT_PYTHON = KtbsConstants.SCRIPT_PYTHON;
	
	public String getEtag();

	public void setEtag(String etag);

	public String getInherits();

	public void setInherits(String inherits);

	public Set<IMethodParameter> getMethodParameters();

	public void setMethodParameters(Set<IMethodParameter> methodParameters);
}