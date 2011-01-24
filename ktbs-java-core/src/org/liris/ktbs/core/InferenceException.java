package org.liris.ktbs.core;

/**
 * An exception thrown whenever the inference process 
 * encounters a problem (e.g. cycles in a type hierarchy).
 * 
 * @author Damien Cram
 *
 */
@SuppressWarnings("serial")
public class InferenceException extends RuntimeException {


	public InferenceException(String message) {
		super(message);
	}
}
