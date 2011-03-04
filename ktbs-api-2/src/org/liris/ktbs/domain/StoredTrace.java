package org.liris.ktbs.domain;

import org.liris.ktbs.domain.interfaces.IStoredTrace;

public class StoredTrace extends Trace implements IStoredTrace {
	
	private String defaultSubject;

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IStoredTrace#getDefaultSubject()
	 */
	@Override
	public String getDefaultSubject() {
		return defaultSubject;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IStoredTrace#setDefaultSubject(java.lang.String)
	 */
	@Override
	public void setDefaultSubject(String defaultSubject) {
		this.defaultSubject = defaultSubject;
	}
}
