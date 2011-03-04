package org.liris.ktbs.domain.interfaces;

import java.util.Set;

public interface ITraceModel extends IKtbsResource, IResourceContainer<IKtbsResource> {

	public Set<IObselType> getObselTypes();

	public void setObselTypes(Set<IObselType> obselTypes);

	public Set<IRelationType> getRelationTypes();

	public void setRelationTypes(Set<IRelationType> relationTypes);

	public Set<IAttributeType> getAttributeTypes();

	public void setAttributeTypes(Set<IAttributeType> attributeTypes);

}