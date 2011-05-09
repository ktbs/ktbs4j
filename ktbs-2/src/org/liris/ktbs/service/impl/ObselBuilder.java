package org.liris.ktbs.service.impl;

import org.liris.ktbs.domain.AttributePair;
import org.liris.ktbs.domain.Obsel;
import org.liris.ktbs.domain.PojoFactory;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.utils.KtbsUtils;

public class ObselBuilder {

	private Obsel obsel;
	private PojoFactory factory;
	private StoredTraceManager storedTraceManager;
	
	public void setSubject(String subject) {
		obsel.setSubject(subject);
	}

	public void setBegin(long begin) {
		obsel.setBegin(KtbsUtils.longToBigInt(begin));
	}

	public void setEnd(long end) {
		obsel.setEnd(KtbsUtils.longToBigInt(end));
	}

	public void addLabel(String label) {
		obsel.addLabel(label);
	}

	public void setBeginDT(String beginDT) {
		obsel.setBeginDT(beginDT);
	}

	public void setEndDT(String endDT) {
		obsel.setEndDT(endDT);
	}

	public void addProperty(String propertyName, Object value) {
		obsel.addProperty(propertyName, value);
	}
	
	public ObselBuilder(StoredTraceManager manager, IStoredTrace trace, PojoFactory factory) {
		this.factory = factory;
		obsel = (Obsel)factory.createResource(IObsel.class);
		obsel.setParentResource(trace);
		this.storedTraceManager = manager;
	}
	
	public ObselBuilder(StoredTraceManager manager, String storedTraceUri,
			PojoFactory factory) {
		this(manager, factory.createStoredTrace(storedTraceUri), factory);
	}

	public void setUri(String uri) {
		obsel.setUri(uri);
	}

	public void addAttribute(String attUri, Object value) {
		IAttributeType attType = factory.createResource(attUri, IAttributeType.class);
		addAttribute(attType, value);
	}

	public void addAttribute(IAttributeType att, Object value) {
		obsel.getAttributePairs().add(new AttributePair(att, value));
	}
	
	public String create() {
		IStoredTrace parentResource = (IStoredTrace)obsel.getParentResource();
		
		return storedTraceManager.createObsel(
				parentResource==null ? null : parentResource, 
				obsel.getLocalName(), 
				obsel.getTypeUri(), 
				obsel.getBeginDT(), 
				obsel.getEndDT(), 
				obsel.getBegin(), 
				obsel.getEnd(), 
				obsel.getSubject(), 
				obsel.getAttributePairs());
	}

	public void setType(IObselType obsType) {
		obsel.setObselType(obsType);
	}
	
	public void setType(String obsTypeUri) {
		obsel.setObselType(factory.createResource(obsTypeUri, IObselType.class));
	}

	public void addRelation(IRelationType relationType, String obs1Uri) {
		obsel.getOutgoingRelations().add(factory.createRelation(obsel.getUri(), relationType.getUri(), obs1Uri));
	}
}
