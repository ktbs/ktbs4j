package org.liris.ktbs.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.ITraceModel;

@SuppressWarnings("serial")
public class TraceModel extends ResourceContainer<IKtbsResource> implements ITraceModel {
	
	private Set<IObselType> obselTypes = new HashSet<IObselType>();
	private Set<IRelationType> relationTypes = new HashSet<IRelationType>();
	private Set<IAttributeType> attributeTypes = new HashSet<IAttributeType>();
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITraceModel#getObselTypes()
	 */
	@Override
	public Set<IObselType> getObselTypes() {
		return obselTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITraceModel#setObselTypes(java.util.Set)
	 */
	@Override
	public void setObselTypes(Set<IObselType> obselTypes) {
		this.obselTypes = obselTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITraceModel#getRelationTypes()
	 */
	@Override
	public Set<IRelationType> getRelationTypes() {
		return relationTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITraceModel#setRelationTypes(java.util.Set)
	 */
	@Override
	public void setRelationTypes(Set<IRelationType> relationTypes) {
		this.relationTypes = relationTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITraceModel#getAttributeTypes()
	 */
	@Override
	public Set<IAttributeType> getAttributeTypes() {
		return attributeTypes;
	}
	
	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.ITraceModel#setAttributeTypes(java.util.Set)
	 */
	@Override
	public void setAttributeTypes(Set<IAttributeType> attributeTypes) {
		this.attributeTypes = attributeTypes;
	}
	
	@Override
	protected Collection<? extends Collection<? extends IKtbsResource>> getContainedResourceCollections() {
		List<Set<? extends IKtbsResource>> c = new LinkedList<Set<? extends IKtbsResource>>();
		c.add(obselTypes);
		c.add(attributeTypes);
		c.add(relationTypes);
		return c;
	}
}
