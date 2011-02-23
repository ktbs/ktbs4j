package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

public class RootPojo extends ResourcePojo  {

	private Set<BasePojo> bases = new HashSet<BasePojo>();

	public Set<BasePojo> getBases() {
		return bases;
	}

	public void setBases(Set<BasePojo> bases) {
		this.bases = bases;
	}
}
