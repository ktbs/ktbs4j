package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

public class AttributeTypePojo extends ResourcePojo implements WithDomain<ObselTypePojo>, WithRange<String> {

	private Set<ObselTypePojo> domains = new HashSet<ObselTypePojo>();
	private Set<String> ranges = new HashSet<String>();
	
	@Override
	public Set<ObselTypePojo> getDomains() {
		return domains;
	}
	
	@Override
	public void setDomains(Set<ObselTypePojo> domains) {
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
