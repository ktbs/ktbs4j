package org.liris.ktbs.client;

import org.liris.ktbs.dao.DaoFactory;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.dao.rest.RestDao;
import org.liris.ktbs.serial.DeserializationConfig;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.service.ServiceFactory;

/**
 * 
 * @author dcram
 *
 */
public class ClientFactory {
	
	private DaoFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
	/**
	 * 
	 * @param rootUri
	 * @return
	 */
	public KtbsClient createRestClient(String rootUri) {
		ResourceDao dao = daoFactory.createRestDao(rootUri);
		KtbsClientImpl client = createClient(rootUri, dao, false);
		return client;
	}

	/**
	 * 
	 * @param rootUri
	 * @param size
	 * @param timeout
	 * @return
	 */
	public KtbsClient createRestCachingClient(String rootUri, Integer size, Long timeout) {
		ResourceDao dao = daoFactory.createRestCachingDao(rootUri, size, timeout);
		KtbsClientImpl client = createClient(rootUri, dao, true);
		return client;
	}

	/**
	 * 
	 * @param rootUri
	 * @return
	 */
	public KtbsClient createMemoryClient(String rootUri) {
		ResourceDao dao = daoFactory.createMemoryDao(rootUri);
		KtbsClientImpl client = createClient(rootUri, dao, false);
		return client;
	}

	/**
	 * 
	 * @param rootUri
	 * @param dao
	 * @param caching
	 * @return
	 */
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

	public KtbsClient createRestClient(
			String rootUri, 
			String user,
			String password
	) {
		ResourceDao dao = daoFactory.createRestDao(rootUri, user, password);
		KtbsClientImpl client = createClient(rootUri, dao, false);
		return client;
	}
	/**
	 * 
	 * @param rootUri
	 * @param user
	 * @param password
	 * @param serializationConfig
	 * @param deSerializationConfig
	 * @return
	 */
	public KtbsClient createRestClient(
			String rootUri, 
			String user,
			String password,
			SerializationConfig serializationConfig,
			DeserializationConfig deSerializationConfig
			) {
		RestDao dao = (RestDao)daoFactory.createRestDao(rootUri, user, password);
		dao.setDefaultDeserializationConfig(deSerializationConfig);
		dao.setDefaultSerializationConfig(serializationConfig);
		KtbsClientImpl client = createClient(rootUri, dao, false);
		return client;
	}

	/**
	 * 
	 * @param rootUri
	 * @param user
	 * @param password
	 * @param size
	 * @param timeout
	 * @return
	 */
	public KtbsClient createRestCachingClient(
			String rootUri, 
			String user, 
			String password, 
			Integer size, 
			Long timeout
			) {
		ResourceDao dao = daoFactory.createRestCachingDao(rootUri, user, password, size, timeout);
		KtbsClientImpl client = createClient(rootUri, dao, true);
		return client;
	}
	
	/**
	 * 
	 * @param rootUri
	 * @param user
	 * @param password
	 * @param size
	 * @param timeout
	 * @param serializationConfig
	 * @param deSerializationConfig
	 * @return
	 */
	public KtbsClient createRestCachingClient(
			String rootUri, 
			String user, 
			String password, 
			Integer size, 
			Long timeout,
			SerializationConfig serializationConfig,
			DeserializationConfig deSerializationConfig
	) {
		RestDao dao = (RestDao)daoFactory.createRestCachingDao(rootUri, user, password, size, timeout);
		dao.setDefaultDeserializationConfig(deSerializationConfig);
		dao.setDefaultSerializationConfig(serializationConfig);
		
		KtbsClientImpl client = createClient(rootUri, dao, true);
		return client;
	}
}
