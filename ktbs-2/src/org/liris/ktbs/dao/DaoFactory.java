package org.liris.ktbs.dao;

import org.liris.ktbs.dao.memory.MemoryDao;
import org.liris.ktbs.dao.rest.ApacheKtbsRestClient;
import org.liris.ktbs.dao.rest.KtbsRestClient;
import org.liris.ktbs.dao.rest.RestDao;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.serial.SerializerFactory;

public class DaoFactory {
	
	private PojoFactory pojoFactory;
	private SerializerFactory serializerFactory;
	
	public void setPojoFactory(PojoFactory pojoFactory) {
		this.pojoFactory = pojoFactory;
	}
	
	public void setSerializerFactory(SerializerFactory serializerFactory) {
		this.serializerFactory = serializerFactory;
	}
	
	public ResourceDao createMemoryDao(String rootUri) {
		return new MemoryDao(rootUri, pojoFactory);
	}

	public ResourceDao createRestDao (
			String rootUri, 
			String username, 
			String password
			) {
		KtbsRestClient client = new ApacheKtbsRestClient(rootUri);
		client.setCredentials(username, password);
		RestDao restDao = buildRestDao(rootUri, client);
		return restDao;
	}

	private RestDao buildRestDao(String rootUri, KtbsRestClient client) {
		client.startSession();
		RestDao restDao = new RestDao(client, rootUri);
		restDao.setProxyFactory(new ProxyFactory(restDao));
		restDao.setSerializerFactory(serializerFactory);
		return restDao;
	}
			
	public CachingDao createRestCachingDao(String rootUri, String username, String password, int max, long timeout) {
		RestDao restDao = (RestDao) createRestDao(rootUri, username, password);
		DefaultCachingDao defaultCachingDao = buildCachingFactory(max, timeout,
				restDao);
		return defaultCachingDao;
	}

	public CachingDao createRestCachingDao(String rootUri, int max, long timeout) {
		RestDao restDao = (RestDao) createRestDao(rootUri);
		DefaultCachingDao defaultCachingDao = buildCachingFactory(max, timeout,
				restDao);
		return defaultCachingDao;
	}

	private DefaultCachingDao buildCachingFactory(int max, long timeout,
			RestDao restDao) {
		DefaultCachingDao defaultCachingDao = new DefaultCachingDao(restDao, max, timeout);
		ProxyFactory proxyFactory = new ProxyFactory(defaultCachingDao);
		restDao.setProxyFactory(proxyFactory);
		return defaultCachingDao;
	}
	
	public ResourceDao createRestDao(String rootUri) {
		KtbsRestClient client = new ApacheKtbsRestClient(rootUri);
		RestDao restDao = buildRestDao(rootUri, client);
		return restDao;
	}
}
