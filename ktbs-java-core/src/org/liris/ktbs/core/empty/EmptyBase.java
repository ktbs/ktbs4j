package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
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
	public KtbsRoot getKtbsRoot() {
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
	public void addStoredTrace(Trace trace) {
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
	public void addTraceModel(TraceModel traceModel) {
		throw new UnsupportedOperationException(MESSAGE);
	}


	@Override
	public TraceModel getTraceModel(String uri) {
		throw new UnsupportedOperationException(MESSAGE);
	}


	@Override
	public void addMethod(Method method) {
		throw new UnsupportedOperationException(MESSAGE);
	}


	@Override
	public Method getMethod(String uri) {
		throw new UnsupportedOperationException(MESSAGE);
	}


}
