package org.liris.ktbs.service;

public interface AuthenticationService {
	public boolean authenticate(String rootUri, String user, String password);
}
