package org.liris.ktbs.service.impl;

import org.liris.ktbs.service.IRootAwareService;

public abstract class RootAwareService implements IRootAwareService {
	
	protected String rootUri;

	public void setRootUri(String rootUri) {
		this.rootUri = rootUri;
	}
	
	public String getRootUri() {
		return rootUri;
	}
}
