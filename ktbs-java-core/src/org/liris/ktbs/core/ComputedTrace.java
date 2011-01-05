package org.liris.ktbs.core;

import java.util.Iterator;

public interface ComputedTrace extends Trace {
	public Method getMethod();
	public Iterator<String> listParameterKeys();
	public String getParameter(String name);
	public void setParameter(String key, String value);
	public void removeParameter(String key);
}
