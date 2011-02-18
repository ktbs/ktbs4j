package org.liris.ktbs.core.api;

import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.WithDomainResource;
import org.liris.ktbs.core.api.share.WithRangeResource;

/**
 * A KTBS attribute type.
 * <p>
 * Ranges of attribute types are uris representing XSD data types.
 * </p>
 * @author Damien Cram
 *
 */
public interface AttributeType extends KtbsResource, WithDomainResource<ObselType>, WithRangeResource<String> {
	
	
}
