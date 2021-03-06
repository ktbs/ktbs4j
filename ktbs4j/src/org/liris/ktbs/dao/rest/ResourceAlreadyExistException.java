package org.liris.ktbs.dao.rest;

import org.liris.ktbs.dao.DaoException;

@SuppressWarnings("serial")
public class ResourceAlreadyExistException extends DaoException {

	public ResourceAlreadyExistException(String uri) {
		super("The resource " + uri + " already exists.");
	}

}
