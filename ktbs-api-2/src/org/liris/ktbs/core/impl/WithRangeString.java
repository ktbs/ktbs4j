package org.liris.ktbs.core.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.api.share.WithRangeResource;

public class WithRangeString implements WithRangeResource<String> {

	private Set<String> ranges = new HashSet<String>();

	@Override
	public void addRange(String range) {
		ranges.add(range);
	}

	@Override
	public Iterator<String> listRanges() {
		return ranges.iterator();
	}

	@Override
	public String getRange() {
		if(ranges.isEmpty())
			return null;
		else
			return  ranges.iterator().next();
	}

}
