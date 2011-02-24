package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;

public class ObselType extends KtbsResource {
	
	private Set<ObselType> superObselTypes = new HashSet<ObselType>();

	public Set<ObselType> getSuperObselTypes() {
		return superObselTypes;
	}

	public void setSuperObselTypes(Set<ObselType> superObselTypes) {
		this.superObselTypes = superObselTypes;
	}
}
