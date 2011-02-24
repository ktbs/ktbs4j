package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;


public class Base extends KtbsResource {

	private String owner;
	private Set<TraceModel> traceModels = new HashSet<TraceModel>();
	private Set<StoredTrace> storedTraces = new HashSet<StoredTrace>();
	private Set<ComputedTrace> computedTraces = new HashSet<ComputedTrace>();
	private Set<Method> methods = new HashSet<Method>();
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public Set<TraceModel> getTraceModels() {
		return traceModels;
	}
	
	public void setTraceModels(Set<TraceModel> traceModels) {
		this.traceModels = traceModels;
	}
	
	public Set<StoredTrace> getStoredTraces() {
		return storedTraces;
	}
	
	public void setStoredTraces(Set<StoredTrace> storedTraces) {
		this.storedTraces = storedTraces;
	}
	
	public Set<ComputedTrace> getComputedTraces() {
		return computedTraces;
	}
	
	public void setComputedTraces(Set<ComputedTrace> computedTraces) {
		this.computedTraces = computedTraces;
	}
	
	public Set<Method> getMethods() {
		return methods;
	}
	
	public void setMethods(Set<Method> methods) {
		this.methods = methods;
	}
}
