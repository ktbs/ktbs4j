package org.liris.ktbs.core.api;

import java.util.Iterator;


public interface Root extends KtbsResource, ResourceContainer {
	
	public Iterator<Base> listBases();
	public void addBase(Base base);
	public Base getBase(String baseURI);
}
