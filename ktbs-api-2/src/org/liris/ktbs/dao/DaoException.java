package org.liris.ktbs.dao;

@SuppressWarnings("serial")
public class DaoException extends RuntimeException {
	private boolean doesNotExist = false;
	
	public DaoException(String message) {
		super(message);
	}
	public DaoException(String message, boolean doesNotExist) {
		super(message);
		this.doesNotExist = true;
	}

	public boolean doesNotExist() {
		return doesNotExist;
	}
}
