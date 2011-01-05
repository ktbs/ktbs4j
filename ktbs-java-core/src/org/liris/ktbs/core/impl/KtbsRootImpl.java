package org.liris.ktbs.core.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsRoot;

public class KtbsRootImpl extends KtbsResourceImpl implements KtbsRoot {

	private Map<String,Base> bases;
	
	KtbsRootImpl(String resourceUri, String... baseURIs) {
		super(resourceUri);
		bases = new HashMap<String, Base>();
		for(String baseURI:baseURIs)
			bases.put(baseURI, null);
	}

	@Override
	public Collection<String> getBaseURIs() {
		return Collections.unmodifiableCollection(bases.keySet());
	}

	@Override
	public Collection<Base> listBases() {
		Collection<Base> c = new LinkedList<Base>();
		for(Base b:bases.values()) {
			if(b!=null)
				c.add(b);
		}
		return Collections.unmodifiableCollection(c);
	}

	@Override
	public Base getBase(String baseURI) {
		return bases.get(baseURI);
	}

	@Override
	public void addBase(Base base) {
		bases.put(base.getURI(), base);
	}

}
