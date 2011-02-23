package org.liris.ktbs.core.pojo;

import java.net.URI;

import org.liris.ktbs.utils.KtbsUtils;

public class UriResource implements Comparable<UriResource>{

	private String uri;
	
	public UriResource() {
		super();
	}

	public UriResource(String uri) {
		super();
		setURI(uri);
	}

	public String getUri() {
		return uri;
	}

	public void setURI(String uri) {
		URI.create(uri);
		this.uri = uri;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UriResource) {
			UriResource r = (UriResource) obj;
			return uri.equals(r.uri);
		} else 
			return false;
	}
	
	@Override
	public int hashCode() {
		return uri.hashCode();
	}

	@Override
	public int compareTo(UriResource o) {
		return uri.compareTo(o.uri);
	}
	
	public String getLocalName() {
		return KtbsUtils.resolveLocalName(uri);
	}

	public String getParentUri() {
		return KtbsUtils.resolveParentURI(uri);
	}
}
