package org.liris.ktbs.client;

import org.liris.ktbs.dao.DaoFactory;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.service.MultiUserRootProvider;
import org.liris.ktbs.service.ServiceFactory;
import org.liris.ktbs.service.impl.MultiUserManager;

public class ClientFactory {
	
	private DaoFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
	public KtbsClient createRestClient(String rootUri) {
		ResourceDao dao = daoFactory.createRestDao(rootUri);
		KtbsClientImpl client = createClient(rootUri, dao, false);
		return client;
	}

	public KtbsClient createRestCachingClient(String rootUri, Integer size, Long timeout) {
		ResourceDao dao = daoFactory.createRestCachingDao(rootUri, size, timeout);
		KtbsClientImpl client = createClient(rootUri, dao, true);
		return client;
	}

	public KtbsClient createMemoryClient(String rootUri) {
		ResourceDao dao = daoFactory.createMemoryDao(rootUri);
		KtbsClientImpl client = createClient(rootUri, dao, false);
		return client;
	}

	private KtbsClientImpl createClient(String rootUri, ResourceDao dao,
			boolean caching) {
		KtbsClientImpl client = new KtbsClientImpl(
				rootUri, 
				dao, 
				serviceFactory.createResourceService(dao, caching), 
				serviceFactory.createStoredTraceService(dao, caching), 
				serviceFactory.createTraceModelService(dao, caching)
				);
		return client;
	}

	public MultiUserRootProvider createMultiUserProvider(String rootUri) {
		return new MultiUserManager(rootUri, this);
	}

	public KtbsClient createRestClient(String rootUri, String user,
			String password) {
		ResourceDao dao = daoFactory.createRestDao(rootUri, user, password);
		KtbsClientImpl client = createClient(rootUri, dao, false);
		return client;
	}

	public KtbsClient createRestCachingClient(String rootUri, String user, String password, Integer size, Long timeout) {
		ResourceDao dao = daoFactory.createRestCachingDao(rootUri, user, password, size, timeout);
		KtbsClientImpl client = createClient(rootUri, dao, true);
		return client;
	}
}
