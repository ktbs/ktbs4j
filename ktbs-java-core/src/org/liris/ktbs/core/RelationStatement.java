package org.liris.ktbs.core;

/**
 * A class that represents an inter-obsel relation between two {@link Obsel} inside a KTBS.
 * @author dcram
 *
 */
public interface RelationStatement {
	public Obsel getFromObsel();
	public String getRelationURI();
	public Obsel getToObsel();
}
