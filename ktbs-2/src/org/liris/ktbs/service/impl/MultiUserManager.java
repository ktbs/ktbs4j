package org.liris.ktbs.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.liris.ktbs.client.ClientFactory;
import org.liris.ktbs.client.KtbsClient;
import org.liris.ktbs.service.IRootAwareService;
import org.liris.ktbs.service.MultiUserRootProvider;

import com.ibm.icu.util.Calendar;

public class MultiUserManager  implements MultiUserRootProvider, IRootAwareService {
		
	private Map<String, Calendar> lastAccess = new HashMap<String, Calendar>();
	private Map<String, KtbsClient> clients = new HashMap<String, KtbsClient>();
	
	// timeout in minutes;
	private int timeout = 150;
	
	// clean period in minutes
	private int cleanPeriod = 5;
	
	private Timer timer;
	
	private String rootUri;
	private ClientFactory clientFactory;
	
	// cache properties
	private boolean caching = false;
	private Integer cacheSize;
	private Long cacheTimeout;
	
	public MultiUserManager(String rootUri, ClientFactory clientFactory) {
		super();
		this.rootUri = rootUri;
		this.clientFactory = clientFactory;
	}

	public MultiUserManager(String rootUri, ClientFactory clientFactory, Integer cacheSize, Long cacheTimeout) {
		this(rootUri, clientFactory);
		this.caching = true;
		this.cacheSize = cacheSize;
		this.cacheTimeout= cacheTimeout;
	}

	// the init method
	public void init() {
		lastAccess = new HashMap<String, Calendar>();
		clients = new HashMap<String, KtbsClient>();
		
		timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				MultiUserManager.this.clean();
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
			
			KtbsClient client ;
			if(caching)
				client = clientFactory.createRestCachingClient(password, user, password, cacheSize, cacheTimeout);
			else
				client = clientFactory.createRestClient(password, user, password);
			
			clients.put(user, client);
			updateAccess(user);
			return true;
		}
	}

	@Override
	public boolean hasClient(String user) {
		return clients.containsKey(user);
	}
	
	@Override
	public String getRootUri() {
		return rootUri;
	}
}
