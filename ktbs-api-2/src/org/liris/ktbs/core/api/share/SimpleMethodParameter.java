package org.liris.ktbs.core.api.share;

import org.liris.ktbs.core.api.MethodParameter;

public class SimpleMethodParameter implements MethodParameter {

	private String name;
	private String value;
	
	public SimpleMethodParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MethodParameter) {
			MethodParameter mp = (MethodParameter) obj;
			return mp.getName().equals(name);
		}
		return false;
	}
}