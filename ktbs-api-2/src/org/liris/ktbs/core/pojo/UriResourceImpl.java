package org.liris.ktbs.core.pojo;

import java.net.URI;

import org.liris.ktbs.core.UriResource;
import org.liris.ktbs.utils.KtbsUtils;

public class UriResourceImpl implements UriResource {

	private String uri;
	
	public UriResourceImpl() {
		super();
	}

	public UriResourceImpl(String uri) {
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
		if (obj instanceof UriResourceImpl) {
			UriResourceImpl r = (UriResourceImpl) obj;
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
		return uri.compareTo(o.getUri());
	}
	
	public String getLocalName() {
		return KtbsUtils.resolveLocalName(uri);
	}

	public String getParentUri() {
		return KtbsUtils.resolveParentURI(uri);
	}
}
