package org.liris.ktbs.core.pojo;

import java.util.HashSet;
import java.util.Set;

public class AttributeTypePojo extends ResourcePojo {

	private Set<ObselTypePojo> domains = new HashSet<ObselTypePojo>();
	private Set<String> ranges = new HashSet<String>();
	
	public Set<ObselTypePojo> getDomains() {
		return domains;
	}
	
	public void setDomains(Set<ObselTypePojo> domains) {
		this.domains = domains;
	}
	
	public Set<String> getRanges() {
		return ranges;
	}
	
	public void setRanges(Set<String> ranges) {
		this.ranges = ranges;
	}
}
