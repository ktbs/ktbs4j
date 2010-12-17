package org.liris.ktbs.core;

import java.util.Collection;

public interface KtbsRoot extends KtbsResource {
	
	public Collection<String> getBaseURIs();
	public Collection<Base> getBases();
	public void addBase(Base base);
	public Base getBase(String baseURI);
}
