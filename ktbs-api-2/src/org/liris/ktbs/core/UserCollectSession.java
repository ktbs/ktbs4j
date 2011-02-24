package org.liris.ktbs.core;

import org.liris.ktbs.core.impl.ResourceManager;
import org.liris.ktbs.core.nocache.DefaultManager;
import org.liris.ktbs.core.pojo.BasePojo;
import org.liris.ktbs.core.pojo.StoredTracePojo;
import org.liris.ktbs.core.pojo.TraceModelPojo;

import com.ibm.icu.util.Calendar;

public class UserCollectSession {

	private ResourceManager manager;
	private String user;
	private String baseUri;
	private StoredTracePojo currentTrace;
	private String defaultTraceModelUri;

	// should be access only throw getBase
	private BasePojo _base;

	public void setDefaultTraceModelUri(String defaultTraceModelUri) {
		this.defaultTraceModelUri = defaultTraceModelUri;
	}

	public TraceModelPojo getDefaultTraceModel() {
		return (TraceModelPojo)manager.getKtbsResource(defaultTraceModelUri);
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

	public StoredTracePojo getCurrentStoredTrace(String user) {
		if(currentTrace == null)
			startNewStoredTrace(user, defaultTraceModelUri);
		return currentTrace;
	}


	public StoredTracePojo startNewStoredTrace(String user, String traceLocalName) {
		return startNewStoredTrace(user, traceLocalName, defaultTraceModelUri, getDefaultOrigin());
	}

	private static String getDefaultOrigin() {
		return Calendar.getInstance().toString();
	}

	public StoredTracePojo startNewStoredTrace(String user, String origin, String traceLocalName) {
		return startNewStoredTrace(user, traceLocalName, defaultTraceModelUri, origin);
	}

	public StoredTracePojo startNewStoredTrace(String user, String traceLocalName, String traceModelUri, String origin) {
		if(traceModelUri == null)
			throw new IllegalStateException("A non null trace model uri must be specified");
		else {
			StoredTracePojo trace = manager.newStoredTrace(
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

	private BasePojo getBase() {
		if(_base == null)
			_base = (BasePojo) manager.getKtbsResource(baseUri);
		return _base;
	}

	public void close() {

	}


	public void authenticate(String password) {

	}
}
