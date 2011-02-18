package org.liris.ktbs.core.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.share.MethodParameter;
import org.liris.ktbs.core.api.share.TransformationResource;

public class TransformationResourceImpl implements TransformationResource {
	
	TransformationResourceImpl() {
		super();
	}

	private Set<MethodParameter> parameters = new HashSet<MethodParameter>();
	private class SimpleMethodParameter implements MethodParameter {

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
	
	@Override
	public Iterator<MethodParameter> listMethodParameters() {
		return parameters.iterator();
	}

	@Override
	public void setMethodParameter(String key, String value) {
		parameters.add(new SimpleMethodParameter(key, value));
	}

	@Override
	public String getMethodParameter(String key) {
		Iterator<MethodParameter> it = parameters.iterator();
		while (it.hasNext()) {
			MethodParameter methodParameter = (MethodParameter) it.next();
			if(methodParameter.getName().equals(key))
				return methodParameter.getValue();
		}
		return null;
	}

	@Override
	public void removeMethodParameter(String key) {
		Iterator<MethodParameter> it = parameters.iterator();
		while (it.hasNext()) {
			MethodParameter methodParameter = (MethodParameter) it.next();
			if(methodParameter.getName().equals(key)) {
				it.remove();
				return;
			}
		}
	}

}
