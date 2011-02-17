package org.liris.ktbs.core.api.share;

import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;

/**
 * The possible exploration modes when requesting for
 * resources in the context of a trace model.
 * 
 * @author Damien Cram
 * @see ObselType#hasSuperObselType(ObselType, Mode)
 * @see ObselType#listOutgoingRelations(Mode)
 * @see ObselType#listIncomingRelations(Mode)
 * @see ObselType#listAttributes(Mode)
 * @see RelationType#hasSuperRelationType(ObselType, Mode)
 */
public enum Mode {
	ASSERTED,
	INFERRED
}
