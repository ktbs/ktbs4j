package org.liris.ktbs.core;

import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.TraceModelService;

public interface KtbsClient {

	public void setCredentials(String username, String password);
	
	public ResourceService getResourceService();

	public TraceModelService getTraceModelService();

}