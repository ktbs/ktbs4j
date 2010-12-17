package org.liris.ktbs.core;

import java.util.Collection;

public interface Base extends KtbsResource {
	
	public Collection<String> getTraceModelURIs();
	public Collection<Trace> getTraces();
	public void addTrace(Trace trace);
	public Collection<String> getTraceURIs();
	public Trace getTrace(String uri);
	public KtbsRoot getKtbsRoot();
	public String getKtbsRootURI();
}
