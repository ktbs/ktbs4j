package org.liris.ktbs.core.impl;

import java.util.Collection;
import java.util.Iterator;

import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.api.share.KtbsResource;

public class TraceModelImpl extends ResourceContainerImpl<KtbsResource> implements
		TraceModel {

	protected TraceModelImpl(String uri) {
		super(uri);
	}

	private ResourceCollectionDelegate<AttributeType> attributeTypeDelegate = new ResourceCollectionDelegate<AttributeType>(manager);
	private ResourceCollectionDelegate<RelationType> relationTypeDelegate = new ResourceCollectionDelegate<RelationType>(manager);
	private ResourceCollectionDelegate<ObselType> obselTypeDelegate = new ResourceCollectionDelegate<ObselType>(manager);
	
	@Override
	public Iterator<AttributeType> listAttributeTypes() {
		return attributeTypeDelegate.list();
	}

	@Override
	public Iterator<RelationType> listRelationTypes() {
		return relationTypeDelegate.list();
	}
	
	@Override
	public Iterator<ObselType> listObselTypes() {
		return obselTypeDelegate.list();
	}

	@Override
	public ObselType newObselType(String localName) {
		ObselType obselType = manager.newObselType(this, localName);
		obselTypeDelegate.add(obselType);
		addContainedResource(obselType);
		return obselType;
	}

	@Override
	public ObselType getObselType(String obselTypeUri) {
		return obselTypeDelegate.get(obselTypeUri);
	}

	@Override
	public RelationType newRelationType(String localName,
			Collection<ObselType> domains, Collection<ObselType> ranges) {
		RelationType relationType = manager.newRelationType(this, localName,
				domains, ranges);
		relationTypeDelegate.add(relationType);
		addContainedResource(relationType);
		return relationType;
	}

	@Override
	public RelationType getRelationType(String relationTypeUri) {
		return relationTypeDelegate.get(relationTypeUri);
	}

	@Override
	public AttributeType newAttributeType(String localName,
			Collection<ObselType> domain) {
		AttributeType attributeType = manager.newAttributeType(this, localName, domain);
		attributeTypeDelegate.add(attributeType);
		addContainedResource(attributeType);
		return attributeType;
	}

	@Override
	public AttributeType getAttributeType(String attributeTypeUri) {
		return attributeTypeDelegate.get(attributeTypeUri);
	}
}
