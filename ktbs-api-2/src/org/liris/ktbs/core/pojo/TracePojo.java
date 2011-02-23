package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

public class TracePojo extends ResourcePojo {
	private String origin;
	private String compliesWithModel;
	private TraceModelPojo traceModel;
	
	private Set<TracePojo> transformedTraces = new HashSet<TracePojo>();
	private Set<ObselPojo> obsels = new HashSet<ObselPojo>();

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

	public TraceModelPojo getTraceModel() {
		return traceModel;
	}

	public void setTraceModel(TraceModelPojo traceModel) {
		this.traceModel = traceModel;
	}

	public Set<TracePojo> getTransformedTraces() {
		return transformedTraces;
	}

	public void setTransformedTraces(Set<TracePojo> transformedTraces) {
		this.transformedTraces = transformedTraces;
	}

	public Set<ObselPojo> getObsels() {
		return obsels;
	}

	public void setObsels(Set<ObselPojo> obsels) {
		this.obsels = obsels;
	}
}
