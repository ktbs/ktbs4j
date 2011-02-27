package org.liris.ktbs.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.domain.UriResource;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IUriResource;
import org.liris.ktbs.dao.ResourceDao;


public class ResourceProxy<T extends IKtbsResource> implements InvocationHandler {

	private static final Log log = LogFactory.getLog(ResourceProxy.class);

	private boolean loaded = false;
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
		if(!loaded) {
			if(method.getName().equals("getUri") 
					|| method.getName().equals("equals")
					|| method.getName().equals("hashCode")
					|| method.getName().equals("compareTo")
			) {
				if(log.isDebugEnabled())
					log.debug("The method "+ method.getName() +" is called on the proxy resource " + resource.getUri() + " (no need to retrieve content from the dao).");
				return method.invoke(resource, args);
			} else {
				log.info("The proxy resource " + resource.getUri() + " is requesting for its content through to the dao (method required: "+method.getName()+").");
				T t = dao.get(resource.getUri(), cls);
				if(t!=null) {
					if(log.isDebugEnabled())
						log.debug("The proxy resource " + resource.getUri() + " was retrieved from the dao (no proxy anymore).");
					resource = t;
					loaded = true;
				} else {
					log.warn("Could not retrieve the content of the resource "+resource.getUri()+" from the dao.");
					return null;
				}
			}
		} 
		return method.invoke(resource, args);
	}
}