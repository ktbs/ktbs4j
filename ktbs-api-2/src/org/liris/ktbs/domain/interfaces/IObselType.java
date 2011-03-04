package org.liris.ktbs.domain.interfaces;

import java.util.Set;

public interface IObselType extends IKtbsResource {

	public Set<IObselType> getSuperObselTypes();

	public void setSuperObselTypes(Set<IObselType> superObselTypes);

}