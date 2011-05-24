package org.liris.ktbs.domain.interfaces;

import java.util.Set;

public interface IRelationType extends IKtbsResource, WithDomain<IObselType>, WithRange<IObselType> {

	public Set<IRelationType> getSuperRelationTypes();

	public void setSuperRelationTypes(Set<IRelationType> superRelationTypes);

}