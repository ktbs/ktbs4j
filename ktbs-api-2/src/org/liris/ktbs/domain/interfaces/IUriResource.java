package org.liris.ktbs.domain.interfaces;

public interface IUriResource extends Comparable<IUriResource> {
	public String getUri();
	public String getLocalName();
	String getParentUri();
}
