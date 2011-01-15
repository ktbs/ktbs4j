package org.liris.ktbs.core.empty;

import java.util.Collection;
import java.util.Iterator;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;

public class EmptyBase extends EmptyResourceContainer implements Base {

	EmptyBase(String uri) {
		super(uri);
	}

	@Override
	public Iterator<Trace> listTraces() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<TraceModel> listTraceModels() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<StoredTrace> listStoredTraces() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<ComputedTrace> listComputedTraces() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<Method> listMethods() {
		throw new UnsupportedOperationException(MESSAGE);
	}


	@Override
	public Iterator<KtbsResource> listResources() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public StoredTrace getStoredTrace(String uri) {
		throw new UnsupportedOperationException(MESSAGE);
	}


	@Override
	public ComputedTrace getComputedTrace(String uri) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Trace getTrace(String uri) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public TraceModel getTraceModel(String uri) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Method getMethod(String uri) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public StoredTrace newStoredTrace(String traceURI, TraceModel model) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public ComputedTrace newComputedTrace(String traceURI, TraceModel model,
			Method method, Collection<Trace> sources) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Method newMethod(String methodURI, String inheritedMethod) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public TraceModel newTraceModel(String modelURI) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}
