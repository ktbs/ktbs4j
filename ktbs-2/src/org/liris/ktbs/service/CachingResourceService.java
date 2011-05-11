package org.liris.ktbs.service;

import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;

public interface CachingResourceService extends ResourceService {
	
	public IRoot getRoot(boolean ignoreCache);
	
	public IBase getBase(String uri, boolean ignoreCache);
	
	public IMethod getMethod(String uri, boolean ignoreCache);
	
	public IStoredTrace getStoredTrace(String uri, boolean ignoreCache);
	
	public IComputedTrace getComputedTrace(String uri, boolean ignoreCache);
	
	public ITrace getTrace(String uri, boolean ignoreCache);
	
	public ITraceModel getTraceModel(String uri, boolean ignoreCache);
	
	public <T extends IKtbsResource> T getResource(String uri, Class<T> cls, boolean ignoreCache);
	
	public IKtbsResource getResource(String uri, boolean ignoreCache);
}
