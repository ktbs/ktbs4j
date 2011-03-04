package org.liris.ktbs.domain;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;

public class RelationType extends KtbsResource implements IRelationType {
	
	private Set<IRelationType> superRelationTypes = new HashSet<IRelationType>();

	private Set<IObselType> domains = new HashSet<IObselType>();
	private Set<IObselType> ranges = new HashSet<IObselType>();
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRelationType#getSuperRelationTypes()
	 */
	@Override
	public Set<IRelationType> getSuperRelationTypes() {
		return superRelationTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRelationType#setSuperRelationTypes(java.util.Set)
	 */
	@Override
	public void setSuperRelationTypes(Set<IRelationType> superRelationTypes) {
		this.superRelationTypes = superRelationTypes;
	}
	
	@Override
	public Set<IObselType> getDomains() {
		return domains;
	}
	
	@Override
	public void setDomains(Set<IObselType> domains) {
		this.domains = domains;
	}
	
	@Override
	public Set<IObselType> getRanges() {
		return ranges;
	}
	
	@Override
	public void setRanges(Set<IObselType> ranges) {
		this.ranges = ranges;
	}
}
