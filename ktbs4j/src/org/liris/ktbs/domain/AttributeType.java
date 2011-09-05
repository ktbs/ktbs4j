package org.liris.ktbs.domain;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IUriResource;

@SuppressWarnings("serial")
public class AttributeType extends KtbsResource implements IAttributeType {

	private Set<IObselType> domains = new HashSet<IObselType>();
	private Set<IUriResource> ranges = new HashSet<IUriResource>();
	
	@Override
	public Set<IObselType> getDomains() {
		return domains;
	}
	
	@Override
	public void setDomains(Set<IObselType> domains) {
		this.domains = domains;
	}
	
	@Override
	public Set<IUriResource> getRanges() {
		return ranges;
	}
	
	@Override
	public void setRanges(Set<IUriResource> ranges) {
		this.ranges = ranges;
	}
}
