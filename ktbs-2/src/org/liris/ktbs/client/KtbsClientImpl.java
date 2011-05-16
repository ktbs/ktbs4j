package org.liris.ktbs.client;

import org.liris.ktbs.dao.ProxyFactory;
import org.liris.ktbs.dao.ResourceDao;
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
	
	private ResourceService resourceService;
	private StoredTraceService storedTraceService;
	private TraceModelService traceModelService;
	private ResourceDao dao;
	
	private String rootUri;
	
	KtbsClientImpl(String rootUri, ResourceDao dao, ResourceService resourceService, StoredTraceService storedTraceService, TraceModelService traceModelService) {
		super();
		this.rootUri = rootUri;
		this.resourceService = resourceService;
		this.storedTraceService = storedTraceService;
		this.traceModelService = traceModelService;
		this.dao = dao;
	}

	@Override
	public ResourceService getResourceService() {
		return resourceService;
	}
	
	
	@Override
	public TraceModelService getTraceModelService() {
		return traceModelService;
	}

	public void setTraceModelService(TraceModelService traceModelService) {
		this.traceModelService = traceModelService;
	}

	@Override
	public StoredTraceService getStoredTraceService() {
		return storedTraceService;
	}
	
	@Override
	public String getRootUri() {
		return rootUri;
	}

	@Override
	public ProxyFactory getProxyFactory() {
		return dao.getProxyFactory();
	}
}
