package org.liris.ktbs.domain;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IObselType;

public class AttributeType extends KtbsResource implements IAttributeType {

	private Set<IObselType> domains = new HashSet<IObselType>();
	private Set<String> ranges = new HashSet<String>();
	
	@Override
	public Set<IObselType> getDomains() {
		return domains;
	}
	
	@Override
	public void setDomains(Set<IObselType> domains) {
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
