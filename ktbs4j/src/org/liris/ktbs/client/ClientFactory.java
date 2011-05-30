package org.liris.ktbs.client;

import org.liris.ktbs.dao.DaoFactory;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.dao.rest.RestDao;
import org.liris.ktbs.serial.DeserializationConfig;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.service.ServiceFactory;

/**
 * Creates customized KTBS clients of various types.
 * 
 * @author Damien Cram
 *
 */
public class ClientFactory {
	
	private DaoFactory daoFactory;
	private ServiceFactory serviceFactory;
	
	// Injected by Spring
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	// Injected by Spring
	public void setServiceFactory(ServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}
	
	/**
	 * Creates a client that manipulate KTBS resources through the REST API.
	 * 
	 * @param rootUri the uri of the KtbsRoot
	 * @return a Rest client bound to the KtbsRoot uri
	 */
	public KtbsClient createRestClient(String rootUri) {
		ResourceDao dao = daoFactory.createRestDao(rootUri);
		KtbsClientImpl client = createClient(rootUri, dao, false);
		return client;
	}

	/**
	 * Give a root client that manipulate KTBS resources through the REST API 
	 * and that supports in-memory caching.
	 * 
	 * @param rootUri the uri of the KtbsRoot
	 * @param size the maximum number of resources that can be held at a time in the cache queue
	 * @param timeout the duration (in milliseconds) before a resource gets considered as out of date and removed from the cache
	 * @return a Rest client bound to the KtbsRoot uri and that supports 
	 * resource in-memory caching
	 */
	public KtbsClient createRestCachingClient(String rootUri, Integer size, Long timeout) {
		ResourceDao dao = daoFactory.createRestCachingDao(rootUri, size, timeout);
		KtbsClientImpl client = createClient(rootUri, dao, true);
		return client;
	}

	/**
	 * Give a KTBS client that manipulates KTBS resources in memory.
	 * 
	 * @param rootUri the uri of the KtbsRoot
	 * @return the in-memory KTBS client bound to that uri
	 */
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

	/**
	 * Creates a client that manipulate KTBS resources through the REST API.
	 * 
	 * @param rootUri the uri of the KtbsRoot
	 * @param user the user name for the underlying HTTP authentication
	 * @param password the user password for the underlying HTTP authentication
	 * @return a Rest client bound to the KtbsRoot uri
	 */
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
	 * Creates a client that manipulate KTBS resources through the REST API.
	 * 
	 * @param rootUri the uri of the KtbsRoot
	 * @param user the user name for the underlying HTTP authentication
	 * @param password the user password for the underlying HTTP authentication
	 * @param serializationConfig the custom configuration to use when serializing resources sent to the KTBS server
	 * @param serializationConfig the custom configuration to use when deserializing resources received form the KTBS server
	 * @return a Rest client bound to the KtbsRoot uri
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
	 * Give a root client that manipulate KTBS resources through the REST API 
	 * and that supports in-memory caching.
	 * 
	 * @param rootUri the uri of the KtbsRoot
	 * @param user the user name for the underlying HTTP authentication
	 * @param password the user password for the underlying HTTP authentication
	 * @param size the maximum number of resources that can be held at a time in the cache queue
	 * @param timeout the duration (in milliseconds) before a resource gets considered as out of date and removed from the cache
	 * @return a Rest client bound to the KtbsRoot uri and that supports 
	 * resource in-memory caching
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
	 * Give a root client that manipulate KTBS resources through the REST API 
	 * and that supports in-memory caching.
	 * 
	 * @param rootUri the uri of the KtbsRoot
	 * @param user the user name for the underlying HTTP authentication
	 * @param password the user password for the underlying HTTP authentication
	 * @param size the maximum number of resources that can be held at a time in the cache queue
	 * @param timeout the duration (in milliseconds) before a resource gets considered as out of date and removed from the cache
	 * @param serializationConfig the custom configuration to use when serializing resources sent to the KTBS server
	 * @param serializationConfig the custom configuration to use when deserializing resources received form the KTBS server
	 * @return a Rest client bound to the KtbsRoot uri and that supports 
	 * resource in-memory caching
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
