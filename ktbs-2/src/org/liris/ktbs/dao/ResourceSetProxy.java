package org.liris.ktbs.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceSetProxy<T extends IKtbsResource> implements InvocationHandler {

	private static final Logger log = LoggerFactory.getLogger(ResourceSetProxy.class);
	
	private boolean loaded;
	private String request;
	private ResourceDao dao;
	private Set<T> resources = new HashSet<T>();
	private Class<T> cls; 

	public ResourceSetProxy(
			ResourceDao dao, 
			String request, 
			Class<T> cls 
	) {
		super();
		this.request = request;
		this.dao = dao;
		this.cls = cls;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
	throws Throwable {
		if(!loaded) {
			ResultSet<T> results =  dao.query(request, cls);
			if(results == null) {
				String message = "Could not retrieve the resource set with the query " + request;
				log.error(message);
				throw new DaoException(message);
			}
			resources = new HashSet<T>();
			resources.addAll(results);
			loaded = true;
		}

		return method.invoke(resources, args);
	}

}
