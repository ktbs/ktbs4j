package org.liris.ktbs.core;

import org.liris.ktbs.core.api.KtbsParameter;

/**
 * A simple implementation of the {@link KtbsParameter} interface.
 * 
 * @author Damien Cram
 *
 */
public class SimpleKtbsParameter implements KtbsParameter {

	private String name;
	private String value;
	
	public SimpleKtbsParameter(String name, String value) {
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
		if (obj instanceof KtbsParameter) {
			KtbsParameter p = (KtbsParameter) obj;
			return p.getName().equals(name) && p.getValue().equals(value);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "[key: "+name+",value: "+value+"]";
	}
}
