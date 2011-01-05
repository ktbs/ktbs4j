package org.liris.ktbs.core.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Trace;

public class BaseImpl extends KtbsResourceImpl implements Base {

	private Map<String,Trace> traces;
	private Set<String> traceModelURIs;
	private KtbsRoot root;
	private String rootURI;

	
	BaseImpl(String resourceUri, String rootURI, String[] traceURIs, String[] traceModelURIs) {
		super(resourceUri);
		traces = new HashMap<String, Trace>();
		for(String traceURI:traceURIs) {
			traces.put(traceURI, null);
		}
		this.traceModelURIs = new HashSet<String>();
		for(String traceModelURI:traceModelURIs)
			this.traceModelURIs.add(traceModelURI);
		
		this.rootURI = rootURI;
	}

	public BaseImpl(String baseUri, KtbsRoot root2) {
		super(baseUri);
		traces = new HashMap<String, Trace>();
		traceModelURIs = new HashSet<String>();
		this.root = root2;
	}

	public BaseImpl(String baseLocalName, String ktbsRootURI) {
		this(ktbsRootURI+baseLocalName+"/",ktbsRootURI, new String[0], new String[0]);
	}

	@Override
	public Collection<String> getTraceModelURIs() {
		return Collections.unmodifiableCollection(traceModelURIs);
	}

	@Override
	public Iterator<Trace> listTraces() {
		return traces.values().iterator();
	}

	@Override
	public Collection<String> getTraceURIs() {
		return traces.keySet();
	}

	@Override
	public Trace getTrace(String uri) {
		return traces.get(uri);
	}

	@Override
	public void addTrace(Trace trace) {
		traces.put(trace.getURI(), trace);
		traceModelURIs.add(trace.getTraceModelUri());
	}

	@Override
	public KtbsRoot getKtbsRoot() {
		return root;
	}

	@Override
	public String getKtbsRootURI() {
		if(rootURI != null)
			return rootURI;
		if(root!=null)
			rootURI = getKtbsRoot().getURI();
		return rootURI;
	}

	@Override
	public KtbsResource get(String resourceURI) {
		return null;
	}
	
	
}
