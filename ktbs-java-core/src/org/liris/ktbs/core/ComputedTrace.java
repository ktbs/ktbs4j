package org.liris.ktbs.core;

import java.util.Iterator;


public interface ComputedTrace extends Trace, ResourceWithParameters {
	public Method getMethod();
	public Method setMethod();
	
	public Iterator<Trace> listSources();
	public void addSourceTrace(Trace sourceTrace);
	
}
