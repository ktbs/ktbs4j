package org.liris.ktbs.core;

import org.liris.ktbs.core.rest.KtbsRestService;

/**
 * A factory that creates KTBS repositories.
 * 
 * @author Damien Cram
 *
 */
public interface KtbsFactory {
	
	/**
	 * Create a local repository that manipulates KTBS resources in-memory without saving them.

	 * @param rootURI the absolute URI of the root for the repository to be created.
	 *
	 * @return the created repository
	 */
	public Ktbs createLocalRepository(String rootURI);
	
	/**
	 * Creates a repository that will manipulate KTBS resources remotely 
	 * through the REST API defined by the KTBS. All resources handled by 
	 * the created repository are Java objects representing KTBS resources 
	 * stored an a remote KTBS server. 
	 * <p>
	 * To communicate with the remote KTBS server, this repository makes use
	 * of a {@link KtbsRestService} that uses native HTTP caching to optimize 
	 * the communication.
	 * </p>
	 * 
	 * <p>
	 * By default:
	 *  <ul>
	 *  <li>invoking a set* method on these objects will result in an 
	 *  underlying UPDATE request,</il>
	 *  <li>invoking a new* method in these objects will result in an 
	 *  underlying POST request,</il>
	 *  <li>invoking a get* method on these object will result in an 
	 *  underlying GET request</il>
	 *  <li>invoking a delete* method to these object will result 
	 *  in an underlying DELETE request</il>
	 * </ul>
	 * </p>
	 * 
	 * 
	 * @param rootURI the absolute URI of the root for the repository to be created.
	 * @return the created repository
	 */
	public Ktbs createRemoteRepository(String rootURI);
}
