package org.liris.ktbs.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IKtbsResource;

import com.google.common.collect.ForwardingSet;

public class ResultSet<T extends IKtbsResource> extends ForwardingSet<T> {

	private Set<T> set = new HashSet<T>();
	
	@Override
	protected Set<T> delegate() {
		return set;
	}
	
	public Set<String> getResultUris() {
		Set<String> set = new HashSet<String>();
		for(T t:this)
			set.add(t.getUri());
		return Collections.unmodifiableSet(set);
	}
}
