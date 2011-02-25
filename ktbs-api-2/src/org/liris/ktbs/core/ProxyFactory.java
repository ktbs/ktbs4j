package org.liris.ktbs.core;

import java.lang.reflect.Proxy;

import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.dao.ResourceDao;

public class ProxyFactory {
	@SuppressWarnings("unchecked")
	public <T extends IKtbsResource> T createResourceProxy(String uri, Class<T> cls, ResourceDao dao) {
		Class<?>[] interfaces = new Class<?>[]{cls};
		return (T) Proxy.newProxyInstance(
				dao.getClass().getClassLoader(), 
				interfaces, 
				new ResourceProxy<T>(dao, uri, cls));
	}
}
