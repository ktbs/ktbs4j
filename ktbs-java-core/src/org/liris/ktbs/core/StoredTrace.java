package org.liris.ktbs.core;

import java.util.Map;

public interface StoredTrace extends Trace {
	public Obsel newObsel(String obselURI, ObselType type, Map<AttributeType, Object> attributes);
	public void removeObsel(String obselURI);

	public String getDefaultSubject();
	public void setDefaultSubject(String string);
}
