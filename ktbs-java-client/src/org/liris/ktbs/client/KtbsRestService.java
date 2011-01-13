package org.liris.ktbs.client;

import org.liris.ktbs.core.KtbsResource;

public interface KtbsRestService {
	
	public KtbsResponse retrieve(String uri);
	public KtbsResponse create(KtbsResource resource);
	public KtbsResponse update(KtbsResource resource, String eTag);
	public KtbsResponse delete(String uri);
	
}
