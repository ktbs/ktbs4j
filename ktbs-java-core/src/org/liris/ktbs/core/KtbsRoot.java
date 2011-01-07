package org.liris.ktbs.core;

import java.util.Iterator;

public interface KtbsRoot extends KtbsResource, ResourceContainer {
	
	public Iterator<Base> listBases();
	public void addBase(Base base, String owner);
	public Base getBase(String baseURI);
}