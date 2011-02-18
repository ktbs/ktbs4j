package org.liris.ktbs.core.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.api.share.Mode;
import org.liris.ktbs.utils.KtbsUtils;

public class ObselTypeImpl extends ResourceImpl implements ObselType {

	protected ObselTypeImpl(String uri) {
		super(uri);
	}

	private ResourceCollectionDelegate<ObselType> superObselTypeDelegate = new ResourceCollectionDelegate<ObselType>(manager);
	
	@Override
	public TraceModel getTraceModel() {
		return (TraceModel) getParentResource();
	}

	@Override
	public boolean hasSuperObselType(ObselType type, Mode mode) {
		if(mode == Mode.ASSERTED)
			return superObselTypeDelegate.contains(type);
		else
			throw new UnsupportedOperationException("Not yet implemented");
	}


	@Override
	public ObselType getSuperObselType() {
		return superObselTypeDelegate.get();
	}

	@Override
	public Iterator<ObselType> listSuperObselTypes() {
		return superObselTypeDelegate.list();
	}

	@Override
	public void addSuperObselType(ObselType type) {
		superObselTypeDelegate.add(type);
	}

	@Override
	public Iterator<AttributeType> listAttributes(Mode mode) {
		if(mode == Mode.ASSERTED) {
			Set<AttributeType> attributeTypes = new HashSet<AttributeType>();
			for(AttributeType a:KtbsUtils.toLinkedList(getTraceModel().listAttributeTypes())) {
				if(KtbsUtils.toLinkedList(a.listDomains()).contains(this))
					attributeTypes.add(a);
			}
			return attributeTypes.iterator();
		} else
			throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Iterator<RelationType> listOutgoingRelations(Mode mode) {
		if(mode == Mode.ASSERTED) {
			Set<RelationType> relationTypes = new HashSet<RelationType>();
			for(RelationType a:KtbsUtils.toLinkedList(getTraceModel().listRelationTypes())) {
				if(KtbsUtils.toLinkedList(a.listDomains()).contains(this))
					relationTypes.add(a);
			}
			return relationTypes.iterator();
		} else
			throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Iterator<RelationType> listIncomingRelations(Mode mode) {
		if(mode == Mode.ASSERTED) {
			Set<RelationType> relationTypes = new HashSet<RelationType>();
			for(RelationType a:KtbsUtils.toLinkedList(getTraceModel().listRelationTypes())) {
				if(KtbsUtils.toLinkedList(a.listRanges()).contains(this))
					relationTypes.add(a);
			}
			return relationTypes.iterator();
		} else
			throw new UnsupportedOperationException("Not yet implemented");
	}

}
