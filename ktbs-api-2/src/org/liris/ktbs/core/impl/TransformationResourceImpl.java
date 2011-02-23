package org.liris.ktbs.core.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.MethodParameter;
import org.liris.ktbs.core.api.TransformationResource;
import org.liris.ktbs.core.api.share.SimpleMethodParameter;

public class TransformationResourceImpl implements TransformationResource {
	
	TransformationResourceImpl() {
		super();
	}

	private Set<MethodParameter> parameters = new HashSet<MethodParameter>();
	
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
