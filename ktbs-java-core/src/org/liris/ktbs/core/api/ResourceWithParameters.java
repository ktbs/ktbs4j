package org.liris.ktbs.core.api;

import java.util.Iterator;


public interface ResourceWithParameters {

	public Iterator<KtbsParameter> listParameters();
	public void setParameter(String key, String value);
	public KtbsParameter getParameter(String key);
	public void removeParameter(String key);
	
}
