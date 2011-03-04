package org.liris.ktbs.core;

import org.liris.ktbs.core.domain.ResourceFactory;
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
	
	public static KtbsClient getRestClient() {
		return (KtbsClient) getContext().getBean("ktbsRestClient");
	}

	public static KtbsClient getMemoryClient() {
		return (KtbsClient) getContext().getBean("ktbsMemoryClient");
	}
	
	public static MultiUserService getMultiUserService() {
		return (MultiUserService) getContext().getBean("multiUserService");
	}
}
