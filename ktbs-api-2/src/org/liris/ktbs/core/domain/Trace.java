package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;

public class Trace extends KtbsResource {
	private String origin;
	private String compliesWithModel;
	private TraceModel traceModel;
	
	private Set<Trace> transformedTraces = new HashSet<Trace>();
	private Set<Obsel> obsels = new HashSet<Obsel>();

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getCompliesWithModel() {
		return compliesWithModel;
	}

	public void setCompliesWithModel(String compliesWithModel) {
		this.compliesWithModel = compliesWithModel;
	}

	public TraceModel getTraceModel() {
		return traceModel;
	}

	public void setTraceModel(TraceModel traceModel) {
		this.traceModel = traceModel;
	}

	public Set<Trace> getTransformedTraces() {
		return transformedTraces;
	}

	public void setTransformedTraces(Set<Trace> transformedTraces) {
		this.transformedTraces = transformedTraces;
	}

	public Set<Obsel> getObsels() {
		return obsels;
	}

	public void setObsels(Set<Obsel> obsels) {
		this.obsels = obsels;
	}
}
