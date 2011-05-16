package org.liris.ktbs.service.impl;

import org.liris.ktbs.dao.CachingDao;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.service.CachingResourceService;

public class CachingResourceManager extends DefaultResourceManager implements CachingResourceService {

	public CachingResourceManager(ResourceDao dao, PojoFactory pojoFactory) {
		super(dao, pojoFactory);
	}

	@Override
	public IRoot getRoot(boolean ignoreCache) {
		return getResource(getRootUri(), IRoot.class, ignoreCache);
	}

	@Override
	public IBase getBase(String uri, boolean ignoreCache) {
		return getResource(uri, IBase.class, ignoreCache);
	}

	@Override
	public IMethod getMethod(String uri, boolean ignoreCache) {
		return getResource(uri, IMethod.class, ignoreCache);
	}

	@Override
	public IStoredTrace getStoredTrace(String uri, boolean ignoreCache) {
		return getResource(uri, IStoredTrace.class, ignoreCache);
	}

	@Override
	public IComputedTrace getComputedTrace(String uri, boolean ignoreCache) {
		return getResource(uri, IComputedTrace.class, ignoreCache);
	}

	@Override
	public ITrace getTrace(String uri, boolean ignoreCache) {
		return getResource(uri, ITrace.class, ignoreCache);
	}

	@Override
	public ITraceModel getTraceModel(String uri, boolean ignoreCache) {
		return getResource(uri, ITraceModel.class, ignoreCache);
	}

	@Override
	public <T extends IKtbsResource> T getResource(String uri, Class<T> cls,
			boolean ignoreCache) {
		return ((CachingDao)dao).get(uri, cls, ignoreCache);
	}

	@Override
	public IKtbsResource getResource(String uri, boolean ignoreCache) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}
