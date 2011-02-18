package org.liris.ktbs.core.impl;

import java.math.BigInteger;
import java.util.Iterator;

import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;

public class TraceImpl extends ResourceContainerImpl<Obsel> implements Trace {

	protected TraceImpl(String uri) {
		super(uri);
	}

	protected ResourceCollectionDelegate<Obsel> obselDelegate = new ResourceCollectionDelegate<Obsel>(manager);
	private ResourceCollectionDelegate<ComputedTrace> transformedTraceDelegate = new ResourceCollectionDelegate<ComputedTrace>(manager);
	private LinkedResourceDelegate<TraceModel> traceModelDelegate = new LinkedResourceDelegate<TraceModel>(manager);
	private String compliantWithModel;
	private String origin;
	
	@Override
	public Iterator<Obsel> listObsels() {
		return obselDelegate.list();
	}

	@Override
	public Iterator<Obsel> listObsels(BigInteger begin, BigInteger end) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Iterator<Obsel> listObsels(long begin, long end) {
		return listObsels(
				new BigInteger(Long.toString(begin)), 
				new BigInteger(Long.toString(end)));
	}

	@Override
	public Iterator<ComputedTrace> listTransformedTraces() {
		return transformedTraceDelegate.list();
	}
	
	@Override
	public String getOrigin() {
		return origin;
	}

	@Override
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Override
	public Obsel getObsel(String obselURI) {
		return obselDelegate.get(obselURI);
	}

	@Override
	public TraceModel getTraceModel() {
		return traceModelDelegate.get();
	}

	@Override
	public void setTraceModel(TraceModel traceModel) {
		traceModelDelegate.set(traceModel);
	}

	
	@Override
	public String getCompliantWithModel() {
		return compliantWithModel;
	}
}
