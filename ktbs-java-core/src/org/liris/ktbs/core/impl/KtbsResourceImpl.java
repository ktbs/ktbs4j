package org.liris.ktbs.core.impl;

import org.liris.ktbs.core.KtbsResource;

public class KtbsResourceImpl implements KtbsResource {

	private String resourceUri;
	private String label;
	
	
	
	KtbsResourceImpl(String resourceUri) {
		super();
		this.resourceUri = resourceUri;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}


	@Override
	public String getURI() {
		return resourceUri;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public String toString() {
		return getURI();
	}
}
