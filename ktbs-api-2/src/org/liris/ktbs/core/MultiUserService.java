package org.liris.ktbs.core;

public interface MultiUserService {

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
	public KtbsClient getClient(String user);

}