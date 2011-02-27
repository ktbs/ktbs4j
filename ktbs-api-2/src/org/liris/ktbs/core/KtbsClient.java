package org.liris.ktbs.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The entry point of the KTBS Java API.
 * 
 * @author Damien Cram
 *
 */
public class KtbsClient {

	public static ResourceManager getDefaultRestManager() {
		ApplicationContext context = new ClassPathXmlApplicationContext("ktbs-client-context.xml");
		return context.getBean("defaultProxyingManager", ResourceManager.class);
	}

	public static ResourceManager getDefaultInMemoryManager() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	public static CollectSession getCollectSession() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	
}
