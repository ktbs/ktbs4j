package org.liris.ktbs.core;

import org.liris.ktbs.core.domain.Base;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;

import com.ibm.icu.util.Calendar;

public class UserCollectSession {

	private ResourceManager manager;
	private String user;
	private String baseUri;
	private IStoredTrace currentTrace;
	private String defaultTraceModelUri;

	// should be access only throw getBase
	private Base _base;

	public void setDefaultTraceModelUri(String defaultTraceModelUri) {
		this.defaultTraceModelUri = defaultTraceModelUri;
	}

	public ITraceModel getDefaultTraceModel() {
		return (ITraceModel)manager.getKtbsResource(defaultTraceModelUri);
	}

	public UserCollectSession(String user, String baseUri) {
		super();
		this.user = user;
		this.baseUri = baseUri;
		this.manager = new DefaultManager();
	}

	public String getUser() {
		return user;
	}

	public IStoredTrace getCurrentStoredTrace(String user) {
		if(currentTrace == null)
			startNewStoredTrace(user, defaultTraceModelUri);
		return currentTrace;
	}


	public IStoredTrace startNewStoredTrace(String user, String traceLocalName) {
		return startNewStoredTrace(user, traceLocalName, defaultTraceModelUri, getDefaultOrigin());
	}

	private static String getDefaultOrigin() {
		return Calendar.getInstance().toString();
	}

	public IStoredTrace startNewStoredTrace(String user, String origin, String traceLocalName) {
		return startNewStoredTrace(user, traceLocalName, defaultTraceModelUri, origin);
	}

	public IStoredTrace startNewStoredTrace(String user, String traceLocalName, String traceModelUri, String origin) {
		if(traceModelUri == null)
			throw new IllegalStateException("A non null trace model uri must be specified");
		else {
			IStoredTrace trace = manager.newStoredTrace(
					getBase().getUri(),
					traceLocalName, 
					traceModelUri, 
					origin, 
					user);
			if(trace != null)
				currentTrace = trace;
			else
				throw new IllegalStateException("A stored trace for the user " + user + " could not be created." );
		}

		return currentTrace;
	}

	private Base getBase() {
		if(_base == null)
			_base = (Base) manager.getKtbsResource(baseUri);
		return _base;
	}

	public void close() {

	}


	public void authenticate(String password) {

	}
}
