package org.liris.ktbs.domain;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IRelationStatement;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;

public class PojoFactory extends AbstractResourceFactory implements ResourceFactory {

	@Override
	public <T extends IKtbsResource> T createResource(String uri, Class<T> cls) {
		T resource = createResource(cls);
		((UriResource)resource).setUri(uri);
		return cls.cast(resource);
	}

	@Override
	public <T extends IKtbsResource> T createResource(Class<T> cls) {
		KtbsResource resource;
		if(IRoot.class.isAssignableFrom(cls))
			resource = new Root();
		else if(IBase.class.isAssignableFrom(cls))
			resource = new Base();
		else if(IStoredTrace.class.isAssignableFrom(cls))
			resource = new StoredTrace();
		else if(IComputedTrace.class.isAssignableFrom(cls))
			resource = new ComputedTrace();
		else if(ITraceModel.class.isAssignableFrom(cls))
			resource = new TraceModel();
		else if(IMethod.class.isAssignableFrom(cls))
			resource = new Method();
		else if(IObsel.class.isAssignableFrom(cls))
			resource = new Obsel();
		else if(IAttributeType.class.isAssignableFrom(cls))
			resource = new AttributeType();
		else if(IRelationType.class.isAssignableFrom(cls))
			resource = new RelationType();
		else if(IObselType.class.isAssignableFrom(cls))
			resource = new ObselType();
		else if(IKtbsResource.class.isAssignableFrom(cls))
			resource = new KtbsResource();
		else
			throw new IllegalStateException("Cannot create a resource of the unknown type: " + cls.getCanonicalName());
		return cls.cast(resource);
	}

	@Override
	public IKtbsResource createResource(String uri) {
		return createResource(uri, IKtbsResource.class);
	}

	public IObsel createObsel(IStoredTrace storedTrace, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Map<String, Object> attributes) {
		IObsel obsel = createResource(storedTrace.getUri(), obselLocalName, true, IObsel.class);

		obsel.setBegin(begin);
		obsel.setEnd(end);
		obsel.setBeginDT(beginDT);
		obsel.setEndDT(endDT);
		obsel.setObselType(createResource(typeUri, IObselType.class));
		obsel.setSubject(subject);
		((KtbsResource)obsel).setParentResource(storedTrace);
		if(attributes != null) {
			for(String s:attributes.keySet()) {
				obsel.getAttributePairs().add(new AttributePair(
						createResource(s, IAttributeType.class), 
						attributes.get(s)));
			}
		}

		return obsel;
	}

	public IObsel createObsel(IStoredTrace storedTrace, String obselLocalName,
			String typeUri, String beginDT, String endDT, BigInteger begin,
			BigInteger end, String subject, Set<IAttributePair> attributes) {
		
		IObsel obsel = createResource(storedTrace.getUri(), obselLocalName, true, IObsel.class);

		obsel.setBegin(begin);
		obsel.setEnd(end);
		obsel.setBeginDT(beginDT);
		obsel.setEndDT(endDT);

		if(typeUri != null)
			obsel.setObselType(createResource(typeUri, IObselType.class));
		obsel.setSubject(subject);

		if(attributes != null)
			obsel.getAttributePairs().addAll(attributes);
		return obsel;
	}

	public IAttributeType createAttributeType(String traceModelUri, String attributeLocalName) {
		return createResource(traceModelUri, attributeLocalName, true, IAttributeType.class);
	}
	
	public IRelationType createRelationType(String traceModelUri, String relationLocalName) {
		return createResource(traceModelUri, relationLocalName, true, IRelationType.class);
	}

	public IObselType createObselType(String traceModelUri, String obselLocalName) {
		return createResource(traceModelUri, obselLocalName, true, IObselType.class);
	}

	public ITraceModel createTraceModel(String traceModelUri) {
		return createResource(traceModelUri, ITraceModel.class);
	}

	public IStoredTrace createStoredTrace(String storedTraceUri) {
		return createResource(storedTraceUri, IStoredTrace.class);
	}

	public IComputedTrace createComputedTrace(String computedTraceUri) {
		return createResource(computedTraceUri, IComputedTrace.class);
	}

	public IMethod createMethod(String methodUri) {
		return createResource(methodUri, IMethod.class);
	}

	public IRelationStatement createRelation(String uri, String uri2,
			String obs1Uri) {
		return new RelationStatement(createObsel(uri), createRelationType(uri2), createObsel(obs1Uri));
	}

	public IRelationType createRelationType(String absoluteUri) {
		return createResource(absoluteUri, IRelationType.class);
	}
	public IAttributeType createAttributeType(String absoluteUri) {
		return createResource(absoluteUri, IAttributeType.class);
	}
	public IObselType createObselType(String absoluteUri) {
		return createResource(absoluteUri, IObselType.class);
	}

	public IObsel createAnonObsel() {
		return new Obsel();
	}
	
	public IObsel createObsel(String absoluteUri) {
		if(absoluteUri == null)
			return createAnonObsel();
		Obsel obsel = new Obsel();
		obsel.setUri(absoluteUri);
		return obsel;
	}
}
