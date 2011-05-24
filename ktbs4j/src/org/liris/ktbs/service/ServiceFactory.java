package org.liris.ktbs.service;

import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.service.impl.CachingResourceManager;
import org.liris.ktbs.service.impl.DefaultResourceManager;
import org.liris.ktbs.service.impl.StoredTraceManager;
import org.liris.ktbs.service.impl.TraceModelManager;

public class ServiceFactory {
	
	private PojoFactory pojoFactory;
	
	public void setPojoFactory(PojoFactory pojoFactory) {
		this.pojoFactory = pojoFactory;
	}
	
	public ResourceService createResourceService(ResourceDao dao, boolean caching) {
		if(caching)
			return new CachingResourceManager(dao, pojoFactory);
		else
		return new DefaultResourceManager(dao, pojoFactory);
	}
	
	public StoredTraceService createStoredTraceService(ResourceDao dao, boolean caching) {
		ResourceService resource = createResourceService(dao, caching);
		StoredTraceManager storedTraceManager = new StoredTraceManager(
				resource, 
				dao, 
				pojoFactory
				);
		return storedTraceManager;
	}
	
	public TraceModelService createTraceModelService(ResourceDao dao,boolean caching) {
		TraceModelManager traceModelManager = new TraceModelManager(dao, pojoFactory);
		return traceModelManager;
	}
}
