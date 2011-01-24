package org.liris.ktbs.core.empty;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.Root;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;

/**
 * 
 * A factory that creates KTBS resources from an URI only. Any invocation 
 * on a method that is different from {@link KtbsResource#getURI()} will throw
 * an {@link UnsupportedOperationException}.
 * 
 * @author Damien Cram 
 *
 */
public class EmptyResourceFactory {
	private static EmptyResourceFactory  instance;
	private EmptyResourceFactory(){};
	public static EmptyResourceFactory getInstance() {
		if(instance == null)
			instance = new EmptyResourceFactory();
		return instance;
	}

	private class EmptyResourceInvocationHandler implements InvocationHandler {
		private String uri;
		
		public EmptyResourceInvocationHandler(String uri) {
			super();
			this.uri = uri;
		}

		@Override
		public Object invoke(
				Object proxy, 
				java.lang.reflect.Method method,
				Object[] args) throws Throwable {
			if(method.getName().equals("getURI"))
				return uri;
			else if(method.getName().equals("hashCode"))
				return uri.hashCode();
			else if(method.getName().equals("equals") && args.length==1) {
				if (args[0] instanceof KtbsResource) {
					KtbsResource resource = (KtbsResource) args[0];
					return uri.equals(resource.getURI());
				} else
					return false;
			}
			else if(method.getName().equals("toString"))
				return uri;
			else if(Arrays.asList(proxy.getClass().getMethods()).contains(method))
				throw new UnsupportedOperationException("The method "+method.getName()+" is not supported (Empty KtbsResource)");
			else
				throw new IllegalStateException("Method "+method.getName());
		}
	}
	
	public <T extends KtbsResource> T createEmptyResource(String uri, Class<T> cls) {
		
		return cls.cast(Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
				new Class<?>[]{cls}, 
				new EmptyResourceInvocationHandler(uri)));
	}
	
	public KtbsResource createResource(String uri) {
		return createEmptyResource(uri, KtbsResource.class);
	}
	
	public Root createKtbsRoot(String uri) {
		return createEmptyResource(uri, Root.class);
	}
	
	public Base createBase(String uri) {
		return createEmptyResource(uri, Base.class);
	}
	
	public Trace createTrace(String uri) {
		return createEmptyResource(uri, Trace.class);
	}
	
	public Obsel createObsel(String uri) {
		return createEmptyResource(uri, Obsel.class);
	}
	
	public StoredTrace createStoredTrace(String uri) {
		return createEmptyResource(uri, StoredTrace.class);
	}
		
	public ComputedTrace createComputedTrace(String uri) {
		return createEmptyResource(uri, ComputedTrace.class);
	}
		
	public TraceModel createTraceModel(String uri) {
		return createEmptyResource(uri, TraceModel.class);
	}
		
	public AttributeType createAttributeType(String uri) {
		return createEmptyResource(uri, AttributeType.class);
	}
		
	public RelationType createRelationType(String uri) {
		return createEmptyResource(uri, RelationType.class);
	}
		
	public ObselType createObselType(String uri) {
		return createEmptyResource(uri, ObselType.class);
	}
		
	public Method createMethod(String uri) {
		return createEmptyResource(uri, Method.class);
	}
}
