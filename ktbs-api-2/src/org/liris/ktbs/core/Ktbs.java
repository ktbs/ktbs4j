package org.liris.ktbs.core;

import java.io.Reader;
import java.io.Writer;

import org.liris.ktbs.core.api.Root;

/**
 * 
 * @author Damien Cram
 *
 */
public interface Ktbs {
	
	/**
	 * Give the KTBS root element of this repository.
	 * 
	 * @return the KTBS root of this repository
	 * @throws AuthenticationException when authentication is required 
	 * in order to get the root of the repository.
	 *  
	 */
	public Root getRoot();
	
	/**
	 * Do the authentication.
	 * 
	 * @param login the KTBS user id
	 * @param password the password
	 */
	public void authenticate(String login, String password);
	
	/**
	 * Persist all changes that has been operated on this 
	 * repository since the last change.
	 * 
	 * @param writer the writer where the KTBS resources to be loaded can be read
	 * @param rdfSyntax the RDF syntax to use to serialize the KTBS resources 
	 * (see KtbsConstants.MIME* constants to give an idea of available constants)
	 * @param baseURI the base URI to use when relativizing URIs (can be null)
	 */
	public void save(Writer writer, String rdfSyntax, String baseURI);
	
	/**
	 * Load KTBS resources from a stream into this repository.
	 * 
	 * @param reader the reader where the KTBS resources to be loaded can be read
	 * @param rdfSyntax the RDF syntax to use to deserialize the KTBS resources 
	 * (see KtbsConstants.MIME* constants to give an idea of available constants)
	 * @param baseURI the base URI to use when resolving URIs (can be null)
	 */
	public void read(Reader reader, String rdfSyntax, String baseURI);
	
}
