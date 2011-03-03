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
	private static ApplicationContext context = new ClassPathXmlApplicationContext("ktbs-client-context.xml");
	
	public static ResourceFactory getPojoFactory() {
		return (ResourceFactory) context.getBean("pojoFactory");
	}
	
	public static KtbsClient getRestClient() {
		return (KtbsClient) context.getBean("ktbsRestClient");
	}

	public static KtbsClient getMemoryClient() {
		return (KtbsClient) context.getBean("ktbsMemoryClient");
	}
}
