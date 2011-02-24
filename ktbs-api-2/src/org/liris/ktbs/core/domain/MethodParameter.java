package org.liris.ktbs.core.domain;


public class MethodParameter {

	private String name;
	private String value;
	
	public MethodParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof MethodParameter) {
			MethodParameter mp = (MethodParameter) obj;
			return mp.getName().equals(name);
		}
		return false;
	}
}