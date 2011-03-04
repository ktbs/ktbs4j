package org.liris.ktbs.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.liris.ktbs.core.ResultSet;
import org.liris.ktbs.core.domain.ResourceFactory;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.dao.DaoException;
import org.liris.ktbs.dao.ResourceDao;

public class MemoryDao implements ResourceDao {

	private Map<String, IKtbsResource> resources = new HashMap<String, IKtbsResource>();

	private String rootUri;
	private ResourceFactory factory;
	
	public void setFactory(ResourceFactory factory) {
		this.factory = factory;
	}
	
	public void setRootUri(String rootUri) {
		this.rootUri = rootUri;
	}
	
	public void init() {
		resources.put(rootUri, factory.createResource(rootUri, IRoot.class));
	}
	
	@Override
	public <T extends IKtbsResource> T get(String uri, Class<T> cls) {
		if(resources.containsKey(uri))
			return cls.cast(resources.get(uri));
		else
			return null;
	}

	@Override
	public <T extends IKtbsResource> T create(T resource) {
		resources.put(resource.getUri(), resource);
		return resource;
	}

	@Override
	public boolean save(IKtbsResource resource) {
		return create(resource) != null;
	}

	@Override
	public boolean delete(String uri) {
		return resources.remove(uri) != null;
	}

	@Override
	public <T extends IKtbsResource> ResultSet<T> query(String request,
			Class<T> cls) {
		throw new UnsupportedOperationException("The memory Dao does not support any query language");
	}

	@Override
	public boolean save(IKtbsResource resource, boolean cascadeChildren) {
		return false;
	}

	@Override
	public boolean saveCollection(String uriToSave,
			Collection<? extends IKtbsResource> collection) {
		boolean saved = true;
		for(IKtbsResource r:collection) 
			saved&=save(r);
		return saved;
		
	}

	@Override
	public boolean postCollection(String uriToSave,
			List<? extends IKtbsResource> collection) {
		throw new DaoException("Not yet implemented");
	}

}
