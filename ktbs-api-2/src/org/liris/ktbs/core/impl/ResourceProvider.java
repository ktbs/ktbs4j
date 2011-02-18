package org.liris.ktbs.core.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.Root;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.api.share.KtbsResource;

public interface ResourceProvider {
	
	public KtbsResource getKtbsResource(String uri);

	public StoredTrace newStoredTrace(Base base, String traceLocalName, TraceModel model, String origin);

	public ComputedTrace newComputedTrace(Base base,
			String traceLocalName, Method method, Set<Trace> sources);

	public Method newMethod(Base base, String methodLocalName,
			String inheritedMethod);

	public TraceModel newTraceModel(Base base, String modelLocalName);

	public Base newBase(Root root, String baseLocalName, String owner);

	public Obsel newObsel(StoredTrace storedTrac,
			String obselLocalName, ObselType type,
			Map<AttributeType, Object> attributes);

	public ObselType newObselType(TraceModel traceModel,
			String localName);

	public RelationType newRelationType(TraceModel traceModel,
			String localName, Collection<ObselType> domains,
			Collection<ObselType> ranges);

	public AttributeType newAttributeType(TraceModel traceModel,
			String localName, Collection<ObselType> domain);
}
