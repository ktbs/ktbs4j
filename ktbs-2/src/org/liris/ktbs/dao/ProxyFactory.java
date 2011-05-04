package org.liris.ktbs.dao;

import java.lang.reflect.Proxy;
import java.util.Set;

import org.liris.ktbs.domain.AbstractResourceFactory;
import org.liris.ktbs.domain.ResourceFactory;
import org.liris.ktbs.domain.interfaces.IKtbsResource;

public class ProxyFactory extends AbstractResourceFactory implements ResourceFactory{
	
	private ResourceDao dao;
	
	public void setDao(ResourceDao dao) {
		this.dao = dao;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IKtbsResource> T createResource(String uri, Class<T> cls) {
		Class<?>[] interfaces = new Class<?>[]{cls};
		return (T) Proxy.newProxyInstance(
				ProxyFactory.class.getClassLoader(), 
				interfaces, 
				new ResourceProxy<T>(dao, uri, cls));
	}

	@SuppressWarnings("unchecked")
	public <T extends IKtbsResource> Set<T> createResourceSetProxy(String request, Class<T> cls) {
		Class<?>[] interfaces = new Class<?>[]{Set.class};
		return (Set<T>) Proxy.newProxyInstance(
				ProxyFactory.class.getClass().getClassLoader(), 
				interfaces, 
				new ResourceSetProxy<T>(dao, request, cls));
	}

	@Override
	public <T extends IKtbsResource> T createResource(Class<T> cls) {
		throw new IllegalStateException("Cannot create an anonym proxy resource");
	}

	@Override
	public IKtbsResource createResource(String uri) {
		return createResource(uri, IKtbsResource.class);
	}

}
