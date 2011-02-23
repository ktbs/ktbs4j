package org.liris.ktbs.core.nocache;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import org.liris.ktbs.core.impl.ResourceFactory;
import org.liris.ktbs.core.impl.ResourceImpl;
import org.liris.ktbs.core.impl.ResourceManager;
import org.liris.ktbs.dao.ResourceDao;


public class DefaultManager implements ResourceManager {

	/*
	 * Injected by Spring
	 */
	private ResourceDao dao;
	private ResourceFactory factory = new ResourceFactory();
	
	private Map<String, KtbsResource> cachedResources = new HashMap<String, KtbsResource>();
	
	
	@Override
	public KtbsResource getKtbsResource(String uri) {
		KtbsResource resource = dao.get(uri);
		if(resource == null)
			return cachedResources.get(uri);
		else {
			cachedResources.put(uri, resource);
			return resource;
		}
	}

	@Override
	public StoredTrace newStoredTrace(Base base, String traceLocalName,
			TraceModel model, String origin) {
		
		StoredTrace storedTrace = factory.createStoredTrace(base, traceLocalName, model, origin);

		try {
			storedTrace.getClass().getDeclaredField("manager").set(storedTrace, this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if(dao.create(storedTrace)) {
			cachedResources.put(storedTrace.getUri(), storedTrace);
			return storedTrace;
		} else
			return null;
	}

	@Override
	public ComputedTrace newComputedTrace(Base base, String traceLocalName,
			Method method, Set<Trace> sources) {
		ComputedTrace computedTrace = factory.createComputedTrace(base, null, traceLocalName, null, method, sources);

		try {
			Field declaredField = ResourceImpl.class.getDeclaredField("manager");
			declaredField.setAccessible(true);
			declaredField.set(computedTrace, this);
			declaredField.setAccessible(false);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		if(dao.create(computedTrace)) {
			cachedResources.put(computedTrace.getUri(), computedTrace);
			return computedTrace;
		} else
			return null;
	}

	@Override
	public Method newMethod(Base base, String methodLocalName,
			String inheritedMethod) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TraceModel newTraceModel(Base base, String modelLocalName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Base newBase(Root root, String baseLocalName, String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Obsel newObsel(StoredTrace storedTrac, String obselLocalName,
			ObselType type, Map<AttributeType, Object> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObselType newObselType(TraceModel traceModel, String localName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RelationType newRelationType(TraceModel traceModel,
			String localName, Collection<ObselType> domains,
			Collection<ObselType> ranges) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeType newAttributeType(TraceModel traceModel,
			String localName, Collection<ObselType> domain) {
		// TODO Auto-generated method stub
		return null;
	}
}
