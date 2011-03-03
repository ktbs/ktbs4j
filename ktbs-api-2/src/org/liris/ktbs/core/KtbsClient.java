package org.liris.ktbs.core;

import org.liris.ktbs.service.MultiUserCollectService;
import org.liris.ktbs.service.ResourceService;
import org.liris.ktbs.service.TraceModelService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The entry point of the KTBS Java API.
 * 
 * @author Damien Cram
 *
 */
public class KtbsClient {

	public static ResourceService getRestResourceService() {
		ApplicationContext context = new ClassPathXmlApplicationContext("ktbs-client-context.xml");
		return context.getBean("defaultProxyingRestManager", ResourceService.class);
	}

	public static TraceModelService getRestTraceModelService() {
		ApplicationContext context = new ClassPathXmlApplicationContext("ktbs-client-context.xml");
		return context.getBean("traceModelRestManager", TraceModelService.class);
	}

	public static ResourceService getDefaultInMemoryManager() {
		ApplicationContext context = new ClassPathXmlApplicationContext("ktbs-client-context.xml");
		return context.getBean("defaultMemoryProxyingManager", ResourceService.class);
	}
	
	public static MultiUserCollectService getCollectSession() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	
}
