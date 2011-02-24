package org.liris.ktbs.core.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Root extends ResourceContainer<Base> {

	private Set<Base> bases = new HashSet<Base>();

	public Set<Base> getBases() {
		return bases;
	}

	public void setBases(Set<Base> bases) {
		this.bases = bases;
	}

	protected Collection<Collection<Base>> getContainedResourceCollections() {
		Collection<Collection<Base>> c = new LinkedList<Collection<Base>>();
		c.add(bases);
		return c;
	}
}
