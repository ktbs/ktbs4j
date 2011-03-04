package org.liris.ktbs.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IRoot;

public class Root extends ResourceContainer<IBase> implements IRoot {

	private Set<IBase> bases = new HashSet<IBase>();

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRoot#getBases()
	 */
	@Override
	public Set<IBase> getBases() {
		return bases;
	}

	/* (non-Javadoc)
	 * @see org.liris.ktbs.core.domain.IRoot#setBases(java.util.Set)
	 */
	@Override
	public void setBases(Set<IBase> bases) {
		this.bases = bases;
	}

	protected Collection<Collection<IBase>> getContainedResourceCollections() {
		Collection<Collection<IBase>> c = new LinkedList<Collection<IBase>>();
		c.add(bases);
		return c;
	}
}
