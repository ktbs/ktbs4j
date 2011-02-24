package org.liris.ktbs.manager;

import java.util.Set;

import org.liris.ktbs.core.pojo.BasePojo;
import org.liris.ktbs.core.pojo.RootPojo;

public interface RootManager {
	public void newBase(RootPojo root, String baseLocalName, String owner);
	public Set<BasePojo> getBases(String rootUri);
	public Set<BasePojo> getBases(String rootUri, String owner);
}
