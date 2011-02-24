package org.liris.ktbs.core.domain;

import java.util.Set;

public interface WithRange<T> {
	public Set<T> getRanges();
	public void setRanges(Set<T> ranges);
}
