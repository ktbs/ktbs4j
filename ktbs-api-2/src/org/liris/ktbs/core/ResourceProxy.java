package org.liris.ktbs.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ResourceProxy {

	private ResourceManager manager;

	private class ProxyResourceInvokationHandler implements InvocationHandler {
		private UriResource resource;

		private ResourceManager manager;
		private String uri;

		public ProxyResourceInvokationHandler(ResourceManager manager,
				String uri) {
			super();
			this.manager = manager;
			this.uri = uri;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {
			if(resource == null) {
				if(method.getName().equals("getUri"))
					return uri;
				else 
					resource = manager.getKtbsResource(uri);
			} 
			return method.invoke(resource, args);
		}
	};

	public UriResource newProxy(String uri) {
		Class<?>[] interfaces = new Class<?>[1];
		interfaces[0] = UriResource.class;

		return (UriResource)Proxy.newProxyInstance(
				ResourceProxy.class.getClassLoader(), 
				interfaces, 
				new ProxyResourceInvokationHandler(manager, uri));
	}

}
