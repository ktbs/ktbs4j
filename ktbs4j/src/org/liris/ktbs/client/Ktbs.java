package org.liris.ktbs.client;

import java.io.IOException;
import java.util.Properties;

import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.serial.DeserializationConfig;
import org.liris.ktbs.serial.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The entry point of the KTBS Java API. It provides KTBS clients to the user.
 * 
 * @author Damien Cram
 * @see ClientFactory
 */
public class Ktbs {

	private static final Logger logger = LoggerFactory.getLogger(Ktbs.class);

	public static final String PROPERTY_USER_PASSWORD = "user.password";
	public static final String PROPERTY_USER_NAME = "user.name";
	public static final String PROPERTY_ROOT_URI = "ktbs.root.uri";
	public static final String PROPERTY_CACHE_SIZE = "ktbs.cache.size";
	public static final String PROPERTY_CACHE_TIMEOUT = "ktbs.cache.timeout";

	private static ApplicationContext singleUserContext;
	private static ApplicationContext getContext() {
		if(singleUserContext == null)
			singleUserContext = new ClassPathXmlApplicationContext("ktbs4j-context.xml");
		return singleUserContext;
	}

	/**
	 * Creates a client that manipulate KTBS resources through the REST API.
	 * 
	 * @param uri the uri of the KtbsRoot
	 * @return a Rest client bound to the KtbsRoot uri
	 */
	public static KtbsClient getRestClient(String uri) {
		String rootUri = getRootUri(uri);
		KtbsClient client = getClientFactory().createRestClient(rootUri);
		return client;
	}

	/**
	 * Creates a client that manipulate KTBS resources through the REST API.
	 * 
	 * @param uri the uri of the KtbsRoot
	 * @param userName the user name for the underlying HTTP authentication
	 * @param userPassword the user password for the underlying HTTP authentication
	 * @return a Rest client bound to the KtbsRoot uri
	 */
	public static KtbsClient getRestClient(String uri, String userName, String userPassword) {
		String rootUri = getRootUri(uri);
		String userName2 = getUserName(userName);
		String userPassword2 = getUserPassword(userPassword);
		KtbsClient client = getClientFactory().createRestClient(
				rootUri, 
				userName2, 
				userPassword2,
				new SerializationConfig(),
				new DeserializationConfig()
		);
		return client;
	}


	private static ClientFactory clientFactory;

	/**
	 * Gives the client factory singleton used to create customized instances of {@link KtbsClient}.
	 * 
	 * @return the client factory
	 */
	public static ClientFactory getClientFactory() {
		if (clientFactory == null) {
			clientFactory = getContext().getBean("clientFactory", ClientFactory.class);
		}
		return clientFactory;
	}

	/**
	 * Give a factory that create KTBS resources in the form of POJOs
	 * (Plain Old Java Object). Each resource created with this factory 
	 * must to belinked manually to other KTBS resources and are not bound 
	 * to any service.
	 *  
	 * @return the pojo factory singleton object
	 */
	public static PojoFactory getPojoFactory() {
		return getContext().getBean("pojoFactory", PojoFactory.class);
	}

	/**
	 * Creates a client that manipulates KTBS resources through 
	 * the REST API. 
	 * 
	 * <p>
	 * Its root uri is taken in the system property
	 * <code>ktbs.root.uri</code> if set, or is read from the file 
	 * <code>ktbs4j.properties</code> if it exists in the classpath.
	 * </p>
	 * 
	 * @return a Rest client bound to the KtbsRoot uri
	 */
	public static KtbsClient getRestClient() {
		return getRestClient(null);
	}

	/**
	 * Give a root client that manipulate KTBS resources through the REST API 
	 * and that supports in-memory caching.
	 * 
	 * @param uri the uri of the KtbsRoot
	 * @param size the maximum number of resources that can be held at a time in the cache queue
	 * @param timeout the duration (in milliseconds) before a resource gets considered as out of date and removed from the cache
	 * @return a Rest client bound to the KtbsRoot uri and that supports 
	 * resource in-memory caching
	 */
	public static KtbsClient getRestCachingClient(String uri, Integer size, Long timeout) {
		String rootUri = getRootUri(uri);
		Integer cacheMaxsize = getCacheMaxsize(size);
		Long cacheTimeout = getCacheTimeout(timeout);
		KtbsClient client = getClientFactory().createRestCachingClient(rootUri, cacheMaxsize, cacheTimeout);

		return client;
	}

	/**
	 * Give a root client that manipulate KTBS resources through the REST API 
	 * and that supports in-memory caching.
	 * 
	 * @param uri the uri of the KtbsRoot
	 * @param size the maximum number of resources that can be held at a time in the cache queue
	 * @param timeout the duration (in milliseconds) before a resource gets considered as out of date and removed from the cache
	 * @param userName the user name for the underlying HTTP authentication
	 * @param userPassword the user password for the underlying HTTP authentication
	 * @return a Rest client bound to the KtbsRoot uri and that supports 
	 * resource in-memory caching
	 */
	public static KtbsClient getRestCachingClient(String uri, Integer size, Long timeout, String userName, String userPassword) {
		String rootUri = getRootUri(uri);
		Integer cacheMaxsize = getCacheMaxsize(size);
		Long cacheTimeout = getCacheTimeout(timeout);
		String userName2 = getUserName(userName);
		String userPassword2 = getUserPassword(userPassword);
		KtbsClient client = getClientFactory().createRestCachingClient(
				rootUri, 
				userName2,
				userPassword2,
				cacheMaxsize, 
				cacheTimeout);

		return client;
	}

	/**
	 * Give a root client that manipulate KTBS resources through the REST API 
	 * and that supports in-memory caching.
	 * 
	 * <p>
	 * Its root uri is taken in the system property
	 * <code>ktbs.root.uri</code> if set, or is read from the file 
	 * <code>ktbs4j.properties</code> if it exists in classpath.
	 * </p>
	 * 
	 * @param size the maximum number of resources that can be held at a time in the cache queue
	 * @param timeout the duration (in milliseconds) before a resource gets considered as out of date and removed from the cache
	 * @return a Rest client bound to the KtbsRoot uri and that supports 
	 * resource in-memory caching
	 */
	public static KtbsClient getRestCachingClient(Integer size, Long timeout) {
		return getRestCachingClient(null, size, timeout);
	}

	/**
	 * Give a root client that manipulate KTBS resources through the REST API 
	 * and that supports in-memory caching.
	 * 
	 * <p>
	 * <ul>
	 * <li>Its cache size is taken in the system property
	 * <code>ktbs.cache.size</code> if set, or is read from the file 
	 * <code>ktbs4j.properties</code> if it exists in classpath.</li>
	 * <li>Its cache timeout is taken in the system property
	 * <code>ktbs.cache.timeout</code> if set, or is read from the file 
	 * <code>ktbs4j.properties</code> if it exists in classpath.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param uri the uri of the KtbsRoot
	 * @return a Rest client bound to the KtbsRoot uri and that supports 
	 * resource in-memory caching
	 */
	public static KtbsClient getRestCachingClient(String uri) {
		return getRestCachingClient(uri, null, null);
	}

	/**
	 * Give a root client that manipulate KTBS resources through the REST API 
	 * and that supports in-memory caching.
	 * 
	 * <p>
	 * <ul>
	 * <li>Its cache size is taken in the system property
	 * <code>ktbs.cache.size</code> if set, or is read from the file 
	 * <code>ktbs4j.properties</code> if it exists in classpath.</li>
	 * <li>Its cache timeout is taken in the system property
	 * <code>ktbs.cache.timeout</code> if set, or is read from the file 
	 * <code>ktbs4j.properties</code> if it exists in classpath.</li>
	 * <li>Its root uri is taken in the system property
	 * <code>ktbs.root.uri</code> if set, or is read from the file 
	 * <code>ktbs4j.properties</code> if it exists in classpath.</li>
	 * </ul>
	 * </p>
	 * 
	 * @return a Rest client bound to the KtbsRoot uri and that supports 
	 * resource in-memory caching
	 */
	public static KtbsClient getRestCachingClient() {
		return getRestCachingClient(null, null, null);
	}

	/**
	 * Give a KTBS client that manipulates KTBS resources in memory.
	 * 
	 * <p>
	 * No underlying protocol for resource persistance. Resoures are removed 
	 * from memory and disappear for ever after the program exits.
	 * </p>
	 * 
	 * @param uri the uri of the KtbsRoot
	 * @return the in-memory KTBS client bound to that uri
	 */
	public static KtbsClient getMemoryClient(String uri) {
		String rootUri = getRootUri(uri);
		KtbsClient client = getClientFactory().createMemoryClient(rootUri);
		return client;
	}


	/**
	 * Give a KTBS client that manipulates KTBS resources in memory.
	 * 
	 * <p>
	 * No underlying protocol for resource persistance. Resoures are removed 
	 * from memory and disappear for ever after the program exits.
	 * </p>

	 * <p>
	 * <ul>
	 * <li>Its root uri is taken in the system property
	 * <code>ktbs.root.uri</code> if set, or is read from the file 
	 * <code>ktbs4j.properties</code> if it exists in classpath.</li>
	 * </ul>
	 * </p>
	 * 
	 * @return the in-memory KTBS client bound to that uri
	 */
	public static KtbsClient getMemoryClient() {
		return getMemoryClient(null);
	}



	private static Properties properties;

	private static Long getCacheTimeout(Long timeout) {
		return getProperty(timeout, PROPERTY_CACHE_TIMEOUT, 60000l, Long.class); 
	}

	private static Integer getCacheMaxsize(Integer cacheSize) {
		return getProperty(cacheSize, PROPERTY_CACHE_SIZE, 2000, Integer.class); 
	}

	private static String getRootUri(String rooUri) {
		return getProperty(rooUri, PROPERTY_ROOT_URI, "http://localhost:8001", String.class); 
	}

	private static String getUserName(String userName) {
		return getProperty(userName, PROPERTY_USER_NAME, "default", String.class); 
	}

	private static String getUserPassword(String userPassword) {
		return getProperty(userPassword, PROPERTY_USER_PASSWORD, "default", String.class); 
	}

	private static <T> T getProperty(T value, String pName, T defaultValue, Class<T> cls) {
		if(value!= null)
			return value;
		else {
			String property = getProperty(pName);
			if(property != null)
				if(Integer.class.isAssignableFrom(cls))
					return cls.cast(Integer.parseInt(property));
				else if(Long.class.isAssignableFrom(cls))
					return cls.cast(Long.parseLong(property));
				else
					return cls.cast(property);
			else 
				return defaultValue;
		}
	}

	private static String getProperty(String name) {
		if(properties == null) {
			properties = new Properties();
			try {
				properties.load(ClassLoader.getSystemResourceAsStream("ktbs4j.properties"));
			} catch (IOException e) {
				logger.info("No ktbs4j.properties found: {}", e.getMessage());
			}
		}
		return properties.getProperty(name);
	}
}
