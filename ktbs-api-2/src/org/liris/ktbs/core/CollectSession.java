package org.liris.ktbs.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.liris.ktbs.core.domain.Root;
import org.liris.ktbs.core.domain.TraceModel;

import com.ibm.icu.util.Calendar;

public class CollectSession {
		
	private Map<String, Calendar> lastAccess = new HashMap<String, Calendar>();
	private Map<String, UserCollectSession> sessions = new HashMap<String, UserCollectSession>();
	
	private String defaultTraceModelUri;
	private String rootUri;
	
	private ResourceManager manager;
	
	private String defaultUser = "default";
	private String defaultPassword = "defaultPwd";

	// timeout in minutes;
	private int timeout = 150;
	
	// clean period in minutes
	private int cleanPeriod = 5;
	
	private Timer timer;
	
	private Root root;
	
	// the init method
	public void init() {
		timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				CollectSession.this.clean();
			}
		};
		timer.schedule(task, cleanPeriod*60000l, cleanPeriod*60000l);
	}
	
	
	private UserCollectSession openUserSession(String user, String password, String baseLocalName) {
		UserCollectSession session = new UserCollectSession(user, rootUri +baseLocalName + "/");
		session.setDefaultTraceModelUri(defaultTraceModelUri);
		session.authenticate(password);
		return session;
	}
	
	private boolean hasOpenedSession(String user) {
		return sessions.get(user) == null;
	}
	
	public UserCollectSession getUserSession(String user) {
		if(!hasOpenedSession(user))
			throw new IllegalStateException("No session is open for the user: " + user);
		return sessions.get(user);
	}
	
	public String getDefaultTraceModelUri() {
		return defaultTraceModelUri;
	}

	public void setDefaultTraceModelUri(String defaultTraceModelUri) {
		this.defaultTraceModelUri = defaultTraceModelUri;
	}
	public String getDefaultUser() {
		return defaultUser;
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
		allUsers.addAll(sessions.keySet());
		allUsers.addAll(lastAccess.keySet());
		
		Iterator<String> userIt = allUsers.iterator();
		while (userIt.hasNext()) {
			String user = (String) userIt.next();
			if(isTimeout(user)) {
				lastAccess.remove(user);
				UserCollectSession session = sessions.remove(user);
				session.close();
			}
		}
	}
	
	public void setDefaultUser(String defaultUser, String defaultPassword) {
		this.defaultPassword = defaultPassword;
		this.defaultUser = defaultUser;
	}
	
	public TraceModel getDefaultTraceModel() {
		UserCollectSession session = getDefaultSession();
		return session.getDefaultTraceModel();
	}

	private UserCollectSession getDefaultSession() {
		if(!hasOpenedSession(defaultUser))
			openUserSession(defaultUser, defaultPassword, defaultUser);
		UserCollectSession session = sessions.get(defaultUser);
		return session;
	}
	
	public Root getRoot() {
		if(root == null)
			root = (Root)manager.getKtbsResource(rootUri);
		return root;
	}
	
}
