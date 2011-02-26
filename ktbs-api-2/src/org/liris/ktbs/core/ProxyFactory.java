package org.liris.ktbs.core;

import java.lang.reflect.Proxy;
import java.util.Set;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.dao.ResourceDao;

public class ProxyFactory {
	
	private ResourceDao dao;
	
	@SuppressWarnings("unchecked")
	public <T extends IKtbsResource> T createResourceProxy(String uri, Class<T> cls) {
		Class<?>[] interfaces = new Class<?>[]{cls};
		return (T) Proxy.newProxyInstance(
				dao.getClass().getClassLoader(), 
				interfaces, 
				new ResourceProxy<T>(dao, uri, cls));
	}

	@SuppressWarnings("unchecked")
	public <T extends IKtbsResource> Set<T> createResourceSetProxy(String request, Class<T> cls) {
		Class<?>[] interfaces = new Class<?>[]{Set.class};
		return (Set<T>) Proxy.newProxyInstance(
				dao.getClass().getClassLoader(), 
				interfaces, 
				new ResourceSetProxy<T>(dao, request, cls));
	}
}
