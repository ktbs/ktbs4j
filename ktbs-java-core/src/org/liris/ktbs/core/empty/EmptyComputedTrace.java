package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.ReadOnlyObjectException;

public class EmptyComputedTrace extends EmptyTrace implements ComputedTrace {

	EmptyComputedTrace(String uri) {
		super(uri);
	}

	@Override
	public Method getMethod() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public Iterator<String> listParameterKeys() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getParameter(String name) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setParameter(String key, String value) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void removeParameter(String key) {
		throw new UnsupportedOperationException(MESSAGE);
	}

}
