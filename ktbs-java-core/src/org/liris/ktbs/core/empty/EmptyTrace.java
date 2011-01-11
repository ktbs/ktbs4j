package org.liris.ktbs.core.empty;

import java.util.Date;
import java.util.Iterator;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;

public class EmptyTrace extends EmptyResourceContainer implements Trace {

	EmptyTrace(String uri) {
		super(uri);
	}

	@Override
	public Iterator<Obsel> listObsels() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getOrigin() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Date getOriginAsDate() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Obsel getObsel(String obselURI) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Base getBase() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public boolean isCompliantWithModel() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<Obsel> listObsels(long begin, long end) {
		throw new UnsupportedOperationException(MESSAGE);
		
	}

	@Override
	public Iterator<Trace> listTransformedTraces() {
		throw new UnsupportedOperationException(MESSAGE);
		
	}

	@Override
	public TraceModel getTraceModel() {
		throw new UnsupportedOperationException(MESSAGE);
		
	}

	@Override
	public void setTraceModel(TraceModel traceModel) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setOrigin(String origin) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setOriginAsDate(Date origin) {
		throw new UnsupportedOperationException(MESSAGE);
	}

}
