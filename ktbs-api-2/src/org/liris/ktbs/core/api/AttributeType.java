package org.liris.ktbs.core.api;


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
