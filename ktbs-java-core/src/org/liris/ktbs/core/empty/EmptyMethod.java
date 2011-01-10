package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.KtbsParameter;
import org.liris.ktbs.core.Method;

public class EmptyMethod extends EmptyResource implements Method {

	public EmptyMethod(String uri) {
		super(uri);
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

	@Override
	public String getInherits() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public String getETag() {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void setInherits(String methodURI) {
		throw new UnsupportedOperationException(MESSAGE);
	}

}
