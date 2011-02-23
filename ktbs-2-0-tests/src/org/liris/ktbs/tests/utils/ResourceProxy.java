package org.liris.ktbs.tests.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.liris.ktbs.core.api.KtbsResource;

public class ResourceProxy {

	private static class EmptyResourceInvocationHandler implements InvocationHandler {

		private String uri;

		public EmptyResourceInvocationHandler(String uri) {
			super();
			this.uri = uri;
		}


		@Override
		public int hashCode() {
			return uri.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof KtbsResource) {
				KtbsResource r = (KtbsResource) obj;
				return r.getUri().equals(uri);
			}
			return false;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {
			if(method.getName().equals("equals")) {
				return equals(args[1]);
			} else if(method.getName().equals("hashCode")) {
				return hashCode();
			} else if(method.getName().equals("getURI")) {
				return uri;
			} else
				return method.invoke(proxy, args);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends KtbsResource> T createProxy(Class<T> cls, String uri) {
		Class<?>[] array = new Class<?>[]{cls};

		return (T) Proxy.newProxyInstance(
				cls.getClassLoader(), 
				array, 
				new EmptyResourceInvocationHandler(uri));
	}
}
