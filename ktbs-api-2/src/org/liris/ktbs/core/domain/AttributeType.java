package org.liris.ktbs.core.domain;

import java.util.HashSet;
import java.util.Set;

public class AttributeType extends KtbsResource implements WithDomain<ObselType>, WithRange<String> {

	private Set<ObselType> domains = new HashSet<ObselType>();
	private Set<String> ranges = new HashSet<String>();
	
	@Override
	public Set<ObselType> getDomains() {
		return domains;
	}
	
	@Override
	public void setDomains(Set<ObselType> domains) {
		this.domains = domains;
	}
	
	@Override
	public Set<String> getRanges() {
		return ranges;
	}
	
	@Override
	public void setRanges(Set<String> ranges) {
		this.ranges = ranges;
	}
}
