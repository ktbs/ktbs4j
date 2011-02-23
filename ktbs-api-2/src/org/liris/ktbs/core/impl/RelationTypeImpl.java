package org.liris.ktbs.core.impl;

import java.util.Iterator;

import org.liris.ktbs.core.api.Mode;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.WithDomainResource;
import org.liris.ktbs.core.api.WithRangeResource;

public class RelationTypeImpl extends ResourceImpl implements RelationType {

	RelationTypeImpl(String uri) {
		super(uri);
	}
	
	/*
	 * The delegate for range management
	 */
	private WithRangeResource<ObselType> withRangeDelegate = new WithRangeResourceImpl<ObselType>(manager);

	/*
	 * The delegate for domain management
	 */
	private WithDomainResource<ObselType> withDomainDelegate = new WithDomainResourceImpl<ObselType>(manager);

	@Override
	public void addDomain(ObselType domain) {
		withDomainDelegate.addDomain(domain);
	}

	@Override
	public Iterator<ObselType> listDomains() {
		return withDomainDelegate.listDomains();
	}

	@Override
	public ObselType getDomain() {
		return withDomainDelegate.getDomain();
	}

	@Override
	public void addRange(ObselType range) {
		withRangeDelegate.addRange(range);
	}

	@Override
	public Iterator<ObselType> listRanges() {
		return withRangeDelegate.listRanges();
	}

	@Override
	public ObselType getRange() {
		return withRangeDelegate.getRange();
	}

	private ResourceCollectionDelegate<RelationType> superRelationTypeDelegate = new ResourceCollectionDelegate<RelationType>(manager);
	
	@Override
	public boolean hasSuperRelationType(RelationType type, Mode mode) {
		if(mode == Mode.ASSERTED) {
			return superRelationTypeDelegate.contains(type);
		} else
			throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public RelationType getSuperRelationType() {
		return superRelationTypeDelegate.get();
	}

	@Override
	public Iterator<RelationType> listSuperRelationTypes() {
		return superRelationTypeDelegate.list();
	}

	@Override
	public void addSuperRelationType(RelationType superRelationType) {
		superRelationTypeDelegate.add(superRelationType);
	}
}
