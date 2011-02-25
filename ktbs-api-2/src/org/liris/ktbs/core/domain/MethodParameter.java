package org.liris.ktbs.core.domain;

import org.liris.ktbs.core.domain.interfaces.IMethodParameter;


public class MethodParameter implements IMethodParameter {

	private String name;
	private String value;
	
	public MethodParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IMethodParameter#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IMethodParameter#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof MethodParameter) {
			IMethodParameter mp = (IMethodParameter) obj;
			return mp.getName().equals(name);
		}
		return false;
	}
}