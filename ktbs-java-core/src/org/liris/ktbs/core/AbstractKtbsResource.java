package org.liris.ktbs.core;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class AbstractKtbsResource implements KtbsResource {

	protected final String uri;
	
	/**
	 * Constructs a new KTBS resource from an uri. The uri passed as parameter is 
	 * normalized throw the method {@link URI#normalize()}.
	 * 
	 * @param uri the URI of the resource.
	 * @throws [@link URISyntaxException} if the uri is malformed, {@link NullPointerException} if uri is null
	 */
	public AbstractKtbsResource(String uri) {
		super();
		try {
			this.uri = new URI(uri).normalize().toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getURI() {
		return uri;
	}

	@Override
	public int compareTo(KtbsResource o) {
		if(o==null || o.getURI()==null)
			return -1;
		return getURI().compareTo(o.getURI());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KtbsResource) {
			KtbsResource r = (KtbsResource) obj;
			return this.getURI().equals(r.getURI());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return uri.hashCode();
	}
}
