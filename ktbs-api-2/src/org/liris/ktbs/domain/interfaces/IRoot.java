package org.liris.ktbs.domain.interfaces;

import java.util.Set;

public interface IRoot extends IKtbsResource, IResourceContainer<IBase> {

	public Set<IBase> getBases();

	public void setBases(Set<IBase> bases);

}