package org.liris.ktbs.service;

import org.liris.ktbs.dao.rest.KtbsResponse;

public interface ResponseAwareService {
	
	public KtbsResponse getLastResponse();
}
