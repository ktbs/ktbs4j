package org.liris.ktbs.core.empty;

import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.TraceModel;

public class EmptyStoredTrace extends EmptyTrace implements StoredTrace {

	EmptyStoredTrace(String uri) {
		super(uri);
	}

	@Override
	public void addObsel(Obsel obsel) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getDefaultSubject() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setTraceModel(TraceModel traceModel) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setDefaultSubject(String string) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void removeObsel(String obselURI) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}
