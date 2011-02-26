package org.liris.ktbs.core.domain;

import java.net.URI;

import org.liris.ktbs.core.domain.interfaces.IUriResource;
import org.liris.ktbs.utils.KtbsUtils;

public class UriResource implements IUriResource {

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
		if (obj instanceof IUriResource) {
			IUriResource r = (IUriResource) obj;
			return uri.equals(r.getUri());
		} else 
			return false;
	}
	
	@Override
	public int hashCode() {
		return uri.hashCode();
	}

	@Override
	public int compareTo(IUriResource o) {
		return uri.compareTo(o.getUri());
	}
	
	public String getLocalName() {
		return KtbsUtils.resolveLocalName(uri);
	}

	public String getParentUri() {
		return KtbsUtils.resolveParentURI(uri);
	}
	
	@Override
	public String toString() {
		return uri;
	}
}
