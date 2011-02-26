package org.liris.ktbs.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.liris.ktbs.core.domain.UriResource;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IUriResource;
import org.liris.ktbs.dao.ResourceDao;

public class ResourceProxy<T extends IKtbsResource> implements InvocationHandler {
	
	private IUriResource resource;
	private ResourceDao dao;
	private Class<T> cls;

	public ResourceProxy(
			ResourceDao dao,
			String uri,
			Class<T> cls
			) {
		super();
		this.cls = cls;
		this.dao = dao;
		this.resource = new UriResource(uri);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
	throws Throwable {
		if(resource.getClass().equals(UriResource.class)) {
			if(method.getName().equals("getUri") 
					|| method.getName().equals("equals")
					|| method.getName().equals("hashCode")
					|| method.getName().equals("compareTo")
					)
				return method.invoke(resource, args);
			else {
				resource = dao.get(resource.getUri(), cls);
			}
		} 
		return method.invoke(resource, args);
	}
}