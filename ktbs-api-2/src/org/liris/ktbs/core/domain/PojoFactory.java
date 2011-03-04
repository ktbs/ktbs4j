package org.liris.ktbs.core.domain;

import java.math.BigInteger;
import java.util.Map;

import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IMethod;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;

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
}
