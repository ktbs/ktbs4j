package org.liris.ktbs.examples;

import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.ITraceModel;

public abstract class TraceModelFiller {
	public abstract void fill(ITraceModel model, PojoFactory factory);
}
