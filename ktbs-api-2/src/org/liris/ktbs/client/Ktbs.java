package org.liris.ktbs.client;

import org.liris.ktbs.domain.ResourceFactory;
import org.liris.ktbs.service.MultiUserRootProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
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
	
	public static ResourceFactory getPojoFactory() {
		return (ResourceFactory) getContext().getBean("pojoFactory");
	}
	
	public static KtbsRootClient getRestRootClient() {
		return (KtbsRootClient) getContext().getBean("restRootClient");
	}

	public static KtbsRootClient getMemoryRootClient() {
		return (KtbsRootClient) getContext().getBean("memoryRootClient");
	}
	
	public static MultiUserRootProvider getMultiUserRestRootProvider() {
		return (MultiUserRootProvider) getContext().getBean("multiUserRootProvider");
	}
}
