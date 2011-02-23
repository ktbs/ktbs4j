package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

public class ObselTypePojo extends ResourcePojo {
	
	private Set<ObselTypePojo> superObselTypes = new HashSet<ObselTypePojo>();

	public Set<ObselTypePojo> getSuperObselTypes() {
		return superObselTypes;
	}

	public void setSuperObselTypes(Set<ObselTypePojo> superObselTypes) {
		this.superObselTypes = superObselTypes;
	}
}
