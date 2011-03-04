package org.liris.ktbs.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.liris.ktbs.core.KtbsClient;
import org.liris.ktbs.core.KtbsClientImpl;
import org.liris.ktbs.core.MultiUserService;
import org.liris.ktbs.core.ProxyFactory;
import org.liris.ktbs.core.domain.PojoFactory;
import org.liris.ktbs.rest.ApacheKtbsRestClient;
import org.liris.ktbs.rest.RestDao;
import org.liris.ktbs.serial.RdfDeserializer;
import org.liris.ktbs.serial.RdfSerializer;

import com.ibm.icu.util.Calendar;

public class MultiUserServiceImpl implements MultiUserService {
		
	private Map<String, Calendar> lastAccess;
	private Map<String, KtbsClient> clients;
	
	private String defaultRootUri;
	public void setDefaultRootUri(String defaultRootUri) {
		this.defaultRootUri = defaultRootUri;
	}
	
	// timeout in minutes;
	private int timeout = 150;
	
	// clean period in minutes
	private int cleanPeriod = 5;
	
	private Timer timer;
	
	// the init method
	public void init() {
		lastAccess = new HashMap<String, Calendar>();
		clients = new HashMap<String, KtbsClient>();
		
		timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				MultiUserServiceImpl.this.clean();
			}
		};
		timer.schedule(task, cleanPeriod*60000l, cleanPeriod*60000l);
	}

	// the destroy method
	public void destroy() {
		timer.cancel();
		timer = null;
	}

	@Override
	public KtbsClient getClient(String user) {
		if(clients.get(user) == null) 
			throw new RuntimeException("No KTBS client is opened for the user " + user + ". ");
		 else {
			updateAccess(user);
			return clients.get(user);
			
		}
	}

	private void updateAccess(String user) {
		lastAccess.put(user, Calendar.getInstance());
	}

	private KtbsClientImpl makeClient() {
		KtbsClientImpl client = new KtbsClientImpl();
		
		ApacheKtbsRestClient apacheClient = new ApacheKtbsRestClient(defaultRootUri);
		
		PojoFactory pojoFactory = new PojoFactory();
		ProxyFactory proxyFactory = new ProxyFactory();
		
		RdfSerializer serializer = new RdfSerializer();
		
		RdfDeserializer deserializer = new RdfDeserializer();
		deserializer.setPojoFactory(pojoFactory);
		deserializer.setProxyFactory(proxyFactory);

		
		RestDao dao = new RestDao(apacheClient);
		dao.setSerializer( serializer);
		dao.setDeserializer(deserializer);
		dao.setLinkedResourceFactory(proxyFactory);
		dao.setResourceFactory(pojoFactory);
		dao.setRootUri(defaultRootUri);
		
		proxyFactory.setDao(dao);

		
		DefaultManager resourceService = new DefaultManager();
		resourceService.setPojoFactory(pojoFactory);
		resourceService.setProxyFactory(proxyFactory);
		resourceService.setDao(dao);
		
		TraceModelManager traceModelService = new TraceModelManager();
		traceModelService.setFactory(pojoFactory);
		traceModelService.setDao(dao);
		traceModelService.setResourceService(resourceService);
		
		StoredTraceManager storedTraceManager = new StoredTraceManager();
		storedTraceManager.setDao(dao);
		storedTraceManager.setFactory(pojoFactory);
		storedTraceManager.setResourceService(resourceService);
		
		client.setDao(dao);
		client.setResourceService(resourceService);
		client.setTraceModelService(traceModelService);
		client.setStoredTraceService(storedTraceManager);
		
		dao.init();
		
		return client;
	}
	
	private boolean isTimeout(String user) {
		Calendar calendar = lastAccess.get(user);
		if(calendar == null)
			return true;
		long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
		return diff > ((long)timeout)*60000l;
	}
	
	private void clean() {
		Set<String> allUsers = new HashSet<String>();
		allUsers.addAll(clients.keySet());
		allUsers.addAll(lastAccess.keySet());
		
		Iterator<String> userIt = allUsers.iterator();
		while (userIt.hasNext()) {
			String user = (String) userIt.next();
			if(isTimeout(user)) {
				lastAccess.remove(user);
				clients.remove(user);
			}
		}
	}

	@Override
	public boolean openClient(String user, String password) {
		if(hasClient(user))
			return false; 
		else {
			KtbsClient client = makeClient();
			client.setCredentials(user, password);
			clients.put(user, client);
			updateAccess(user);
			return true;
		}
	}

	@Override
	public boolean hasClient(String user) {
		return clients.containsKey(user);
	}
}
