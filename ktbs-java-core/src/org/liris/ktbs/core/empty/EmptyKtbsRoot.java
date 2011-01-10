package org.liris.ktbs.core.empty;

import java.util.Iterator;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsRoot;

public class EmptyKtbsRoot extends EmptyResourceContainer implements KtbsRoot {

	EmptyKtbsRoot(String uri) {
		super(uri);
	}

	@Override
	public Iterator<Base> listBases() {
		throw new UnsupportedOperationException(MESSAGE);
	}


	@Override
	public Base getBase(String baseURI) {
		throw new UnsupportedOperationException(MESSAGE);
	}

	@Override
	public void addBase(Base base) {
		throw new UnsupportedOperationException(MESSAGE);
	}

}
