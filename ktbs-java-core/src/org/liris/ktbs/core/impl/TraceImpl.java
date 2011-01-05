package org.liris.ktbs.core.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;

public class TraceImpl extends KtbsResourceImpl implements Trace {

	private Map<String,Obsel> obsels;
	private String traceModelURI;
	private Date origin;
	private Base base;
	private String baseURI;
	private boolean compliantWithModel;


	TraceImpl(String resourceUri,String traceModelURI, Date origin, Base base, boolean compliantWithModel) {
		super(resourceUri);
		this.traceModelURI = traceModelURI;
		obsels = new HashMap<String, Obsel>();
		this.origin = origin;
		this.base = base;
		this.compliantWithModel = compliantWithModel;
	}

	TraceImpl(String resourceUri,String traceModelURI, Date origin, String baseURI, boolean compliantWithModel) {
		super(resourceUri);
		this.traceModelURI = traceModelURI;
		obsels = new HashMap<String, Obsel>();
		this.origin = origin;
		this.baseURI = baseURI;
		this.compliantWithModel = compliantWithModel;
	}

	@Override
	public Collection<Obsel> listObsels() {
		return obsels.values();
	}

	@Override
	public void addObsel(Obsel obsel) {
		obsels.put(obsel.getURI(),obsel);
	}

	@Override
	public void removeObsel(String obselURI) {
		obsels.remove(obselURI);
	}

	@Override
	public String getTraceModelUri() {
		return traceModelURI;
	}

	@Override
	public Collection<String> listObselURIs() {
		return obsels.keySet();
	}

	@Override
	public Date getOrigin() {
		return origin;
	}

	@Override
	public Base getBase() {
		return base;
	}

	@Override
	public String getBaseURI() {
		if(getBase() != null) 
			this.baseURI = getBase().getURI();
		return baseURI;

	}

	@Override
	public boolean isCompliantWithModel() {
		return compliantWithModel;
	}

	@Override
	public Obsel getObsel(String obselURI) {
		return obsels.get(obselURI);
	}
}
