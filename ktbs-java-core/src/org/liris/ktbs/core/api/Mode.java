package org.liris.ktbs.core.api;

/**
 * The possible exploration modes when requesting for
 * resources in the context of a trace model.
 * 
 * @author Damien Cram
 * @see ObselType#hasSuperType(ObselType, Mode)
 * @see ObselType#listOutgoingRelations(Mode)
 * @see ObselType#listIncomingRelations(Mode)
 * @see ObselType#listAttributes(Mode)
 */
public enum Mode {
	ASSERTED,
	INFERRED
}
