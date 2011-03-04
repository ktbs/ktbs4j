package org.liris.ktbs.client;

import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.service.TraceModelService;

public interface KtbsRootClient {

	public void setCredentials(String username, String password);
	
	public ResourceService getResourceService();

	public TraceModelService getTraceModelService();

	public StoredTraceService getStoredTraceService();

}