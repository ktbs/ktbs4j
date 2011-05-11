package org.liris.ktbs.client;

import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.service.MultiUserRootProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The entry point of teh KTBS Java API. It provides root clients to the user.
 * 
 * @author Damien Cram
 *
 */
public class Ktbs {
	

	private static ApplicationContext singleUserContext;
	private static ApplicationContext getContext() {
		if(singleUserContext == null)
			singleUserContext = new ClassPathXmlApplicationContext("ktbs-client-context.xml");
		return singleUserContext;
	}

	/**
	 * Give a root client that manipulate KTBS resources through the REST API.
	 * 
	 * @return the root client
	 */
	public static KtbsClient getRestClient(String uri) {
		setRootUriProperty(uri);
		return (KtbsClient) getContext().getBean("restRootClient");
	}

	public static KtbsClient getRestClient() {
		return getRestClient(null);
	}

	/**
	 * Give a root client that manipulate KTBS resources through the REST API.
	 * 
	 * @return the root client
	 */
	public static KtbsClient getRestCachingClient(String uri, int size, long timeout) {
		setRootUriProperty(uri);
		System.setProperty("ktbs.cache.size",Integer.toString(size));
		System.setProperty("ktbs.cache.timeout",Long.toString(timeout));
		return (KtbsClient) getContext().getBean("restCachingClient");
	}

	public static KtbsClient getRestCachingClient(int size, long timeout) {
		return getRestCachingClient(null, size, timeout);
	}

	/**
	 * Give a root client that manipulate KTBS resources in memory.
	 * 
	 * @return the root client
	 */
	public static KtbsClient getMemoryClient(String uri) {
		setRootUriProperty(uri);
		return (KtbsClient) getContext().getBean("memoryRootClient");
	}

	public static KtbsClient getMemoryClient() {
		return getMemoryClient(null);
	}

	/**
	 * Give an object that can provide multiple distinct instances 
	 * of {@link KtbsClient} (one per username).
	 * 
	 * @return the root client provider object
	 */
	public static MultiUserRootProvider getMultiUserRestRootProvider(String uri) {
		setRootUriProperty(uri);
		return (MultiUserRootProvider) getContext().getBean("multiUserRootProvider");
	}

	private static void setRootUriProperty(String uri) {
		if(uri != null)
			System.setProperty("ktbs.root.uri",uri);
	}

	public static MultiUserRootProvider getMultiUserRestRootProvider() {
		return getMultiUserRestRootProvider(null);
	}

	public static PojoFactory getPojoFactory() {
		return (PojoFactory) getContext().getBean("pojoFactory");
	}
}
