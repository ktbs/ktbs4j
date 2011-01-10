package org.liris.ktbs.core;

import java.util.Iterator;


public interface ComputedTrace extends Trace, ResourceWithParameters {
	public Method getMethod();
	public void setMethod(Method method);
	
	public Iterator<Trace> listSources();
	public void addSourceTrace(Trace sourceTrace);
	
}
