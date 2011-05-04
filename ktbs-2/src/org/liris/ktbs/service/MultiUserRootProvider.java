package org.liris.ktbs.service;

import org.liris.ktbs.client.KtbsRootClient;

public interface MultiUserRootProvider {

	public boolean openClient(String user, String password);
	public boolean hasClient(String user);
	
	/**
	 * Give the client instance mapped to this user, and fails
	 * if no client is instanciated for this user.
	 * 
	 * @param user the name of the user who owns the client
	 * @return the client for this user.
	 * @throws RuntimeException when no client is instanciated for the user.
	 */
	public KtbsRootClient getClient(String user);

}