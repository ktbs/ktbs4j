package org.liris.ktbs.domain;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IObselType;

public class ObselType extends KtbsResource implements IObselType {
	
	private Set<IObselType> superObselTypes = new HashSet<IObselType>();

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObselType#getSuperObselTypes()
	 */
	@Override
	public Set<IObselType> getSuperObselTypes() {
		return superObselTypes;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IObselType#setSuperObselTypes(java.util.Set)
	 */
	@Override
	public void setSuperObselTypes(Set<IObselType> superObselTypes) {
		this.superObselTypes = superObselTypes;
	}
}
