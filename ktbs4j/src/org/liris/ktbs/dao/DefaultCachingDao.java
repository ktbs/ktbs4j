package org.liris.ktbs.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.liris.ktbs.dao.rest.KtbsResponse;
import org.liris.ktbs.domain.interfaces.IKtbsResource;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.TreeMultimap;

public class DefaultCachingDao implements CachingDao, UserAwareDao {

    public DefaultCachingDao(ResourceDao daoDelegate, int maxNumberOfResources, long resourceTimeOut) {
	super();
	this.maxNumberOfResources = maxNumberOfResources;
	this.resourceTimeOut = resourceTimeOut;
	this.daoDelegate = daoDelegate;
	initCache();
    }


    private class CachedResource implements Comparable<CachedResource>{
	private IKtbsResource resource;
	private long cacheTimestamp;

	public CachedResource(IKtbsResource resource) {
	    super();
	    this.resource = resource;
	    this.cacheTimestamp = System.currentTimeMillis();
	}

	public IKtbsResource getResource() {
	    return resource;
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof CachedResource) {
		CachedResource r = (CachedResource) obj;
		return resource.equals(r.resource) && cacheTimestamp == r.cacheTimestamp;
	    }
	    return false;
	}

	@Override
	public int compareTo(CachedResource o) {
	    if(cacheTimestamp < o.cacheTimestamp)
		return -1;
	    else if(cacheTimestamp > o.cacheTimestamp)
		return 1;
	    else
		return resource.compareTo(o.resource);
	}

	@Override
	public String toString() {
	    return resource.toString().replaceAll(DefaultCachingDao.this.getRootUri(), "");
	}
    }

    private ResourceDao daoDelegate;


    // Timeout before a resource is considered invalid
    private long resourceTimeOut;

    // The maximum number of resources allowed in the cache queue
    private int maxNumberOfResources; 

    private Map<String, CachedResource> uriIndex;
    private Queue<CachedResource> cacheQueue;
    private Multimap<Long, CachedResource> timestampIndex;

    private synchronized void addToCache(IKtbsResource resource) {
	if(resource == null) {
	    // Not able to cache a null resource
	    return;
	}

	String uri = resource.getUri();
	if(uri == null)
	    return;

	removeFromCache(uri);

	CachedResource cachedResource = new CachedResource(resource);
	cacheQueue.add(cachedResource);
	timestampIndex.put(cachedResource.cacheTimestamp, cachedResource);
	uriIndex.put(uri, cachedResource);

	removeOutdatedResources();
	removeOlderResourcesFromCache();
    }

    private synchronized void initCache() {
	this.uriIndex = new ConcurrentHashMap<String, DefaultCachingDao.CachedResource>();
	this.cacheQueue = new ConcurrentLinkedQueue<DefaultCachingDao.CachedResource>();
	TreeMultimap<Long, CachedResource> map = TreeMultimap.create();
	this.timestampIndex = Multimaps.synchronizedSortedSetMultimap(map);
    }

    @Override
    public synchronized void clearCache() {
	initCache();
    }

    private synchronized void removeOutdatedResources() {
	long currentTime = System.currentTimeMillis();
	Iterator<Long> it = timestampIndex.keySet().iterator();

	long timestamp;

	LinkedList<CachedResource> cachedResourceToBeRemoved = new LinkedList<CachedResource>();
	while(it.hasNext() && (currentTime-(timestamp = it.next()))>resourceTimeOut) {
	    for(CachedResource cachedResource:timestampIndex.get(timestamp)) {
		// remove the resource with that uri from the uriIndex
		uriIndex.remove(cachedResource.getResource().getUri());
		cachedResourceToBeRemoved.add(cachedResource);
	    }
	    // remove all resources with that timestamp from the timestampIndex
	    it.remove();
	}
	// remove all outdated resources from the cache queue
	cacheQueue.removeAll(cachedResourceToBeRemoved);
    }

    private synchronized void removeOlderResourcesFromCache() {
	// remove older ressources from cache
	while(cacheQueue.size() > this.maxNumberOfResources) {
	    CachedResource removedResource = cacheQueue.poll();
	    timestampIndex.get(removedResource.cacheTimestamp).remove(removedResource);
	    uriIndex.remove(removedResource.getResource().getUri());
	}
    }

    @Override
    public ProxyFactory getProxyFactory() {
	return daoDelegate.getProxyFactory();
    }

    @Override
    public void removeFromCache(IKtbsResource resource) {
	removeFromCache(resource.getUri());
    }

    @Override
    public synchronized void removeFromCache(String uri) {
	if(uri == null)
	    /*
	     * anonymous resource, cannot be in cache
	     * do nothing
	     */
	    return;

	CachedResource removedChachedResource = uriIndex.remove(uri);
	if(removedChachedResource != null) {
	    // There was an entry for that resource in the cache
	    cacheQueue.remove(removedChachedResource);
	    long timestamp = removedChachedResource.cacheTimestamp;
	    timestampIndex.get(timestamp).remove(removedChachedResource);
	}
    }



    // BEGIN daoDelegate
    @Override
    public <T extends IKtbsResource> T get(String uri, Class<T> cls) {
	return this.getFromCache(uri, cls);
    }

    @Override
    public IKtbsResource get(String uri) {
	return this.getFromCache(uri);
    }

    @Override
    public <T extends IKtbsResource> T createAndGet(T prototype) {
	T resource = daoDelegate.createAndGet(prototype);
	this.addToCache(resource);
	return resource;
    }

    @Override
    public String create(IKtbsResource prototype) {
	String uri = daoDelegate.create(prototype);
	if(uri != null)
	    this.removeFromCache(uri);
	return uri;
    }


    @Override
    public boolean delete(String uri) {
	boolean delete = daoDelegate.delete(uri);
	if(delete) 
	    this.removeFromCache(uri);
	return delete;
    }


    @Override
    public <T extends IKtbsResource> ResultSet<T> query(String request,
	    Class<T> cls) {
	/*
	 * TODO Optimize this by adding all the ressource to cache at the same time
	 */
	ResultSet<T> resultSet = daoDelegate.query(request, cls);
	for(T resource:resultSet)
	    this.addToCache(resource);
	return resultSet;
    }

    @Override
    public boolean save(IKtbsResource resource) {
	boolean saved = daoDelegate.save(resource);
	if(saved) 
	    this.addToCache(resource);
	return saved;
    }

    @Override
    public boolean save(IKtbsResource resource, boolean cascadeChildren) {
	boolean saved = daoDelegate.save(resource, cascadeChildren);
	if(saved) 
	    this.addToCache(resource);
	return saved;
    }

    @Override
    public boolean saveCollection(String uriToSave,
	    Collection<? extends IKtbsResource> collection) {
	boolean saved = daoDelegate.saveCollection(uriToSave, collection);
	if(saved) {
	    for(IKtbsResource resource:collection)
		this.addToCache(resource);
	}
	return saved;
    }

    @Override
    public boolean postCollection(String uriToSave,
	    List<? extends IKtbsResource> collection) {
	boolean posted = daoDelegate.postCollection(uriToSave, collection);
	if(posted) {
	    /*
	     * TODO optimize this by removing all resource in one shot
	     */
	    for(IKtbsResource resource:collection) {
		this.removeFromCache(resource.getUri());
	    }
	}
	return posted;
    }

    @Override
    public KtbsResponse getLastResponse() {
	return daoDelegate.getLastResponse();
    }
    // END 

    private IKtbsResource getFromCache(String uri) {
	IKtbsResource resource;
	resource = classIndependantReadFromCache(uri, IKtbsResource.class);
	return resource;

    }

    private <T extends IKtbsResource> T getFromCache(String uri, Class<T> cls) {
	T resource;
	resource = classIndependantReadFromCache(uri, cls);
	return resource;
    }

    private <T extends IKtbsResource> T classIndependantReadFromCache(String uri, Class<T> cls) {
	T resource;
	removeOutdatedResources();
	CachedResource cachedResource = uriIndex.get(uri);
	if(cachedResource != null)
	    resource = cls.cast(cachedResource.getResource());
	else {
	    resource = daoDelegate.get(uri, cls);
	}
	this.addToCache(resource);
	return resource;
    }

    @Override
    public <T extends IKtbsResource> T get(String uri, Class<T> cls,
	    boolean fromServer) {
	if(fromServer) 
	    return this.daoDelegate.get(uri, cls);
	else
	    return this.getFromCache(uri, cls);
    }

    @Override
    public void setCredentials(String username, String password) {
	((UserAwareDao)this.daoDelegate).setCredentials(username, password);
    }

    @Override
    public String getRootUri() {
	return daoDelegate.getRootUri();
    }

    public int size() {
	return uriIndex.size();
    }

    public Collection<IKtbsResource> getCache() {
	Collection<IKtbsResource> cache = new LinkedList<IKtbsResource>();
	for(CachedResource cachedResource:uriIndex.values())
	    cache.add(cachedResource.getResource());
	return Collections.unmodifiableCollection(cache);
    }

    public Collection<String> getCacheIds() {
	return Collections.unmodifiableCollection(uriIndex.keySet());
    }

    @Override
    public IKtbsResource get(String uri, boolean ignoreCache) {
	if(ignoreCache) 
	    return this.daoDelegate.get(uri);
	else
	    return this.getFromCache(uri);

    }


}
