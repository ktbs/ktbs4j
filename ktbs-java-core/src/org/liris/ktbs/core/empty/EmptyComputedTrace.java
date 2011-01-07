package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsParameter;
import org.liris.ktbs.core.Method;

public class EmptyComputedTrace extends EmptyTrace implements ComputedTrace {

	EmptyComputedTrace(String uri) {
		super(uri);
	}

	@Override
	public Method getMethod() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<KtbsParameter> listParameters() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setParameter(String key, String value) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public KtbsParameter getParameter(String key) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void removeParameter(String key) {
		throw new UnsupportedOperationException(MESSAGE);
	}
}