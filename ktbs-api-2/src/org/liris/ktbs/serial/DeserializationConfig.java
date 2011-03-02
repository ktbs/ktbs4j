package org.liris.ktbs.serial;

import java.util.HashMap;
import java.util.Map;

public class DeserializationConfig {
	private Map<LinkAxis, DeserializationMode> modes = new HashMap<LinkAxis, DeserializationMode>();

	public DeserializationConfig() {
		super();
		modes.put(LinkAxis.CHILD, DeserializationMode.PROXY);
		modes.put(LinkAxis.LINKED, DeserializationMode.PROXY);
		modes.put(LinkAxis.LINKED_SAME_TYPE, DeserializationMode.PROXY);
		modes.put(LinkAxis.PARENT, DeserializationMode.PROXY);
	}
	
	public DeserializationMode getMode(LinkAxis axis) {
		return modes.get(axis);
	}

	public DeserializationMode getChildMode() {
		return modes.get(LinkAxis.CHILD);
	}
	
	public DeserializationMode getLinkMode() {
		return modes.get(LinkAxis.LINKED);
	}
	
	public DeserializationMode getLinkSameTypeMode() {
		return modes.get(LinkAxis.LINKED_SAME_TYPE);
	}
	
	public void setChildMode(DeserializationMode mode) {
		modes.put(LinkAxis.CHILD, mode);
	}
	
	public void setLinkMode(DeserializationMode mode) {
		modes.put(LinkAxis.LINKED, mode);
	}
	
	public void setLinkSameTypeMode(DeserializationMode mode) {
		modes.put(LinkAxis.LINKED_SAME_TYPE, mode);
	}
	
	public boolean isChildCascade() {
		return modes.get(LinkAxis.CHILD) == DeserializationMode.CASCADE;
	}

	public boolean isChildNull() {
		return modes.get(LinkAxis.CHILD) == DeserializationMode.NULL;
	}
	
	public boolean isChildProxy() {
		return modes.get(LinkAxis.CHILD) == DeserializationMode.PROXY;
	}

	public boolean isChildUriInPlain() {
		return modes.get(LinkAxis.CHILD) == DeserializationMode.URI_IN_PLAIN;
	}

	public boolean isLinkCascade() {
		return modes.get(LinkAxis.LINKED) == DeserializationMode.CASCADE;
	}
	
	public boolean isLinkNull() {
		return modes.get(LinkAxis.LINKED) == DeserializationMode.NULL;
	}
	
	public boolean isLinkProxy() {
		return modes.get(LinkAxis.LINKED) == DeserializationMode.PROXY;
	}
	
	public boolean isLinkUriInPlain() {
		return modes.get(LinkAxis.LINKED) == DeserializationMode.URI_IN_PLAIN;
	}

	public boolean isLinkSameTypeCascade() {
		return modes.get(LinkAxis.LINKED_SAME_TYPE) == DeserializationMode.CASCADE;
	}
	
	public boolean isLinkSameTypeNull() {
		return modes.get(LinkAxis.LINKED_SAME_TYPE) == DeserializationMode.NULL;
	}
	
	public boolean isLinkSameTypeProxy() {
		return modes.get(LinkAxis.LINKED_SAME_TYPE) == DeserializationMode.PROXY;
	}
	
	public boolean isLinkSameTypeUriInPlain() {
		return modes.get(LinkAxis.LINKED_SAME_TYPE) == DeserializationMode.URI_IN_PLAIN;
	}
}
