package org.liris.ktbs.core.domain;

import java.util.Set;

public interface WithDomain<T> {
	public Set<T> getDomains();
	public void setDomains(Set<T> domains);
}
