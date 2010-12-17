package org.liris.ktbs.core;

/**
 * A class that represents an inter-obsel relation between two {@link Obsel} inside a KTBS.
 * @author dcram
 *
 */
public interface Relation {
	public Obsel getFromObsel();
	public String getFromObselURI();
	public String getRelationName();
	public Obsel getToObsel();
	public String getToObselURI();
}
