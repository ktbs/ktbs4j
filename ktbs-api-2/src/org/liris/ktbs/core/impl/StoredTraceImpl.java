package org.liris.ktbs.core.impl;

import java.util.Map;

import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.StoredTrace;

public class StoredTraceImpl extends TraceImpl implements StoredTrace {
	
	StoredTraceImpl(String uri) {
		super(uri);
	}

	@Override
	public Obsel newObsel(String obselLocalName, ObselType type,
			Map<AttributeType, Object> attributes) {
		Obsel obsel = manager.newObsel(this, obselLocalName, type, attributes);
		obselDelegate.add(obsel);
		addContainedResource(obsel);
		return obsel;
	}

	@Override
	public void removeObsel(String obselURI) {
		obselDelegate.remove(obselURI);
	}

	private String defaultSubject;
	
	@Override
	public String getDefaultSubject() {
		return defaultSubject;
	}

	@Override
	public void setDefaultSubject(String subject) {
		this.setDefaultSubject(subject);
	}
}
