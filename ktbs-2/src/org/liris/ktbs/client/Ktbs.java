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
	public static KtbsRootClient getRestRootClient() {
		return (KtbsRootClient) getContext().getBean("restRootClient");
	}

	/**
	 * Give a root client that manipulate KTBS resources in memory.
	 * 
	 * @return the root client
	 */
	public static KtbsRootClient getMemoryRootClient() {
		return (KtbsRootClient) getContext().getBean("memoryRootClient");
	}
	
	/**
	 * Give an object that can provide multiple distinct instances 
	 * of {@link KtbsRootClient} (one per username).
	 * 
	 * @return the root client provider object
	 */
	public static MultiUserRootProvider getMultiUserRestRootProvider() {
		return (MultiUserRootProvider) getContext().getBean("multiUserRootProvider");
	}

	public static PojoFactory getPojoFactory() {
		return (PojoFactory) getContext().getBean("pojoFactory");
	}
}
