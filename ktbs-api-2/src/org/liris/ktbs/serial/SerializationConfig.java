package org.liris.ktbs.serial;

import java.util.HashMap;
import java.util.Map;

public class SerializationConfig {

	private Map<LinkAxis, SerializationMode> modes = new HashMap<LinkAxis, SerializationMode>();

	public SerializationConfig() {
		super();
		modes.put(LinkAxis.CHILD, SerializationMode.URI);
		modes.put(LinkAxis.LINKED, SerializationMode.URI);
		modes.put(LinkAxis.LINKED_SAME_TYPE, SerializationMode.URI);
		modes.put(LinkAxis.PARENT, SerializationMode.URI);
	}
	
	public SerializationMode configure(LinkAxis key, SerializationMode value) {
		return modes.put(key, value);
	}

	public SerializationMode getMode(LinkAxis axis) {
		return modes.get(axis);
	}

	public SerializationMode getChildMode() {
		return modes.get(LinkAxis.CHILD);
	}
	
	public SerializationMode getLinkMode() {
		return modes.get(LinkAxis.LINKED);
	}
	
	public SerializationMode getLinkSameTypeMode() {
		return modes.get(LinkAxis.LINKED_SAME_TYPE);
	}
	
	public void setChildMode(SerializationMode mode) {
		modes.put(LinkAxis.CHILD, mode);
	}
	
	public void setLinkMode(SerializationMode mode) {
		modes.put(LinkAxis.LINKED, mode);
	}
	
	public void setLinkSameTypeMode(SerializationMode mode) {
		modes.put(LinkAxis.LINKED_SAME_TYPE, mode);
	}
	
	
}
