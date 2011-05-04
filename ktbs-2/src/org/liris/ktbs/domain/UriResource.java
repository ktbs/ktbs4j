package org.liris.ktbs.domain;

import java.net.URI;

import org.liris.ktbs.domain.interfaces.IUriResource;
import org.liris.ktbs.utils.KtbsUtils;

public class UriResource implements IUriResource {

	private String uri;

	public UriResource() {
		super();
	}

	public UriResource(String uri) {
		super();
		setUri(uri);
	}

	@Override
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		URI.create(uri);
		this.uri = uri;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IUriResource) {
			IUriResource r = (IUriResource) obj;
			return uri.equals(r.getUri());
		} else 
			return super.equals(obj);
	}

	@Override
	public int hashCode() {
		if(uri != null)
			return uri.hashCode();
		else
			// anonym resource
			return System.identityHashCode(this);
	}

	@Override
	public int compareTo(IUriResource o) {
		// throws an exception for anonymous resource (uri = null)
		return uri.compareTo(o.getUri());
	}

	@Override
	public String getLocalName() {
		if(uri == null)
			// anonymous resource support
			return null;
		else
			return KtbsUtils.resolveLocalName(uri);
	}

	@Override
	public String getParentUri() {
		if(uri == null)
			// anonymous resource support
			return null;
		else
			return KtbsUtils.resolveParentURI(uri);
	}

	@Override
	public String toString() {
		return uri;
	}
}
