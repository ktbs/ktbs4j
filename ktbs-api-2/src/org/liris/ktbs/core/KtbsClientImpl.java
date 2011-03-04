package org.liris.ktbs.core;

import org.liris.ktbs.dao.UserAwareDao;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.StoredTraceService;
import org.liris.ktbs.service.TraceModelService;

/**
 * The entry point of the KTBS Java API.
 * 
 * @author Damien Cram
 *
 */
public class KtbsClientImpl implements KtbsClient {
	
	private StoredTraceService storedTraceService;
	private ResourceService resourceService;
	private TraceModelService traceModelService;
	
	private UserAwareDao dao;
	
	@Override
	public ResourceService getResourceService() {
		return resourceService;
	}
	
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	public void setDao(UserAwareDao dao) {
		this.dao = dao;
	}
	
	@Override
	public TraceModelService getTraceModelService() {
		return traceModelService;
	}

	public void setTraceModelService(TraceModelService traceModelService) {
		this.traceModelService = traceModelService;
	}

	@Override
	public void setCredentials(String username, String password) {
		if(dao != null)
			dao.setCredentials(username, password);
	}

	@Override
	public StoredTraceService getStoredTraceService() {
		return storedTraceService;
	}
	
	public void setStoredTraceService(StoredTraceService storedTraceService) {
		this.storedTraceService = storedTraceService;
	}
	
}
