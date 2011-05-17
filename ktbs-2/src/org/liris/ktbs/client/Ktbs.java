package org.liris.ktbs.client;

import java.io.IOException;
import java.util.Properties;

import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.service.MultiUserClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The entry point of the KTBS Java API. It provides root clients to the user.
 * 
 * @author Damien Cram
 *
 */
public class Ktbs {

	private static final Logger logger = LoggerFactory.getLogger(Ktbs.class);

	public static final String PROPERTY_ROOT_URI = "ktbs.root.uri";
	public static final String PROPERTY_CACHE_SIZE = "ktbs.cache.size";
	public static final String PROPERTY_CACHE_TIMEOUT = "ktbs.cache.timeout";

	private static ApplicationContext singleUserContext;
	private static ApplicationContext getContext() {
		if(singleUserContext == null)
			singleUserContext = new ClassPathXmlApplicationContext("ktbs-client-context.xml");
		return singleUserContext;
	}

	/**
	 * Creates a client that manipulate KTBS resources through the REST API.
	 * 
	 * @param uri
	 * @return
	 */
	public static KtbsClient getRestClient(String uri) {
		String rootUri = getRootUri(uri);
			KtbsClient client = getClientFactory().createRestClient(rootUri);
		return client;
	}


	private static ClientFactory clientFactory;

	private static ClientFactory getClientFactory() {
		if (clientFactory == null) {
			clientFactory = getContext().getBean("clientFactory", ClientFactory.class);
		}
		return clientFactory;
	}

	/**
	 * 
	 * @return
	 */
	public static KtbsClient getRestClient() {
		return getRestClient(null);
	}

	/**
	 * 
	 * Give a root client that manipulate KTBS resources through the REST API.
	 * @param uri
	 * @param size
	 * @param timeout
	 * @return
	 */
	public static KtbsClient getRestCachingClient(String uri, Integer size, Long timeout) {
		String rootUri = getRootUri(uri);
		Integer cacheMaxsize = getCacheMaxsize(size);
		Long cacheTimeout = getCacheTimeout(timeout);
		KtbsClient client = getClientFactory().createRestCachingClient(rootUri, cacheMaxsize, cacheTimeout);

		return client;
	}

	/**
	 * 
	 * @param size
	 * @param timeout
	 * @return
	 */
	public static KtbsClient getRestCachingClient(Integer size, Long timeout) {
		return getRestCachingClient(null, size, timeout);
	}

	/**
	 * 
	 * @param uri
	 * @return
	 */
	public static KtbsClient getRestCachingClient(String uri) {
		return getRestCachingClient(uri, null, null);
	}

	/**
	 * 
	 * @return
	 */
	public static KtbsClient getRestCachingClient() {
		return getRestCachingClient(null, null, null);
	}

	/**
	 * Give a root client that manipulate KTBS resources in memory.
	 * 
	 * @return the root client
	 */
	public static KtbsClient getMemoryClient(String uri) {
		String rootUri = getRootUri(uri);
			KtbsClient client = getClientFactory().createMemoryClient(rootUri);
		return client;
	}

	/**
	 * 
	 * @return
	 */
	public static KtbsClient getMemoryClient() {
		return getMemoryClient(null);
	}

	/**
	 * Give an object that can provide multiple distinct instances 
	 * of {@link KtbsClient} (one per username).
	 * 
	 * @return the root client provider object
	 */
	public static MultiUserClientProvider getMultiUserRestClientProvider(String uri) {
		String rootUri = getRootUri(uri);
			MultiUserClientProvider multiUserRootProvider = getClientFactory().createMultiUserProvider(rootUri);

		return multiUserRootProvider;
	}

	/**
	 * 
	 * @return
	 */
	public static MultiUserClientProvider getMultiUserRestClientProvider() {
		return getMultiUserRestClientProvider(null);
	}

	/**
	 * 
	 * @return
	 */
	public static PojoFactory getPojoFactory() {
		return (PojoFactory) getContext().getBean("pojoFactory");
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
				properties.load(ClassLoader.getSystemResourceAsStream("ktbs-client.properties"));
			} catch (IOException e) {
				logger.info("No ktbs-client.properties found: {}", e.getMessage());
			}
		}
		return properties.getProperty(name);
	}
}
