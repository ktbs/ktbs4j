package org.liris.ktbs.core.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

public class PojoFactory {

	private static final Log log = LogFactory.getLog(PojoFactory.class);
	
	public <T extends IKtbsResource> T createResource(String uri, Class<T> cls) {
		
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
		else
			throw new IllegalStateException("Cannot create a resource of the unknown type: " + cls.getCanonicalName());
		resource.setURI(uri);
		return cls.cast(resource);
	}
}
