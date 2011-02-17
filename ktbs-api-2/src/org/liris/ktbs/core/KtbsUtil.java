package org.liris.ktbs.core;

import org.liris.ktbs.core.rest.KtbsRestService;

/**
 * The entry point of the KTBS Java API.
 * 
 * @author Damien Cram
 *
 */
public class KtbsUtil {

	/**
	 * Give the repository factory object.
	 * 
	 * @return the repository factory
	 */
	public static KtbsFactory getRepositoryFactory() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * 
	 * Creates a new {@link KtbsRestService} object that will
	 * allow to communicate with remote KTBS through the KTBS REST API.
	 * 
	 * <p>
	 * By default, this service will use native HTTP caching and embed the 
	 * HttpClient of the Apache HttpClient Cache 4.1 API.
	 * </p>
	 * 
	 * @return a new instance of that service, with a new empty cache.
	 * 
	 */
	public static KtbsRestService newRestService() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
