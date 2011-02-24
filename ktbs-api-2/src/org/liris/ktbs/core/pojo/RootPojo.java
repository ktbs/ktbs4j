package org.liris.ktbs.core.pojo;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class RootPojo extends ResourceContainerPojo<BasePojo> {

	private Set<BasePojo> bases = new HashSet<BasePojo>();

	public Set<BasePojo> getBases() {
		return bases;
	}

	public void setBases(Set<BasePojo> bases) {
		this.bases = bases;
	}

	protected Collection<Collection<BasePojo>> getContainedResourceCollections() {
		Collection<Collection<BasePojo>> c = new LinkedList<Collection<BasePojo>>();
		c.add(bases);
		return c;
	}
}
