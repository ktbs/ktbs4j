package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;


public class BasePojo extends ResourcePojo {

	private String owner;
	private Set<TraceModelPojo> traceModels = new HashSet<TraceModelPojo>();
	private Set<StoredTracePojo> storedTraces = new HashSet<StoredTracePojo>();
	private Set<ComputedTracePojo> computedTraces = new HashSet<ComputedTracePojo>();
	private Set<MethodPojo> methods = new HashSet<MethodPojo>();
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public Set<TraceModelPojo> getTraceModels() {
		return traceModels;
	}
	
	public void setTraceModels(Set<TraceModelPojo> traceModels) {
		this.traceModels = traceModels;
	}
	
	public Set<StoredTracePojo> getStoredTraces() {
		return storedTraces;
	}
	
	public void setStoredTraces(Set<StoredTracePojo> storedTraces) {
		this.storedTraces = storedTraces;
	}
	
	public Set<ComputedTracePojo> getComputedTraces() {
		return computedTraces;
	}
	
	public void setComputedTraces(Set<ComputedTracePojo> computedTraces) {
		this.computedTraces = computedTraces;
	}
	
	public Set<MethodPojo> getMethods() {
		return methods;
	}
	
	public void setMethods(Set<MethodPojo> methods) {
		this.methods = methods;
	}
}
