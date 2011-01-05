package org.liris.ktbs.core.empty;


import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;

/**
 * 
 * A factory that creates KTBS resources from an URI only.
 * 
 * @author Damien Cram 
 *
 */
public class EmptyResourceFactory {
	private static EmptyResourceFactory  instance;

	@SuppressWarnings("unchecked")
	public <T extends KtbsResource> T createEmptyResource(String uri, Class<T> clazz) {
		if(StoredTrace.class.equals(clazz) || Trace.class.equals(clazz))
			return (T) createStoredTrace(uri);
		else if(ComputedTrace.class.equals(clazz))
			return (T) createComputedTrace(uri);
		else if(Obsel.class.equals(clazz))
			return (T) createObsel(uri);
		else if(Method.class.equals(clazz))
			return (T) createMethod(uri);
		else if(Base.class.equals(clazz))
			return (T) createBase(uri);
		else if(KtbsRoot.class.equals(clazz))
			return (T) createKtbsRoot(uri);
		else if(AttributeType.class.equals(clazz))
			return (T) createAttributeType(uri);
		else if(RelationType.class.equals(clazz))
			return (T) createRelationType(uri);
		else if(ObselType.class.equals(clazz))
			return (T) createObselType(uri);
		else if(TraceModel.class.equals(clazz))
			return (T) createTraceModel(uri);
		else
			throw new UnsupportedOperationException("Cannot create an instance of class \""+clazz.getCanonicalName()+"\"");
	}
	
	public KtbsRoot createKtbsRoot(String uri) {
		return new EmptyKtbsRoot(uri);
	}
	public Base createBase(String uri) {
		return new EmptyBase(uri);
	}
	public Trace createTrace(String uri) {
		return new EmptyTrace(uri);
	}
	public Obsel createObsel(String uri) {
		return new EmptyObsel(uri);
	}
	public StoredTrace createStoredTrace(String uri) {
		return new EmptyStoredTrace(uri);
	}
		
	public ComputedTrace createComputedTrace(String uri) {
		return new EmptyComputedTrace(uri);
	}
		
	public TraceModel createTraceModel(String uri) {
		return new EmptyTraceModel(uri);
	}
		
	public AttributeType createAttributeType(String uri) {
		return new EmptyAttributeType(uri);
	}
		
	public RelationType createRelationType(String uri) {
		return new EmptyRelationType(uri);
	}
		
	public ObselType createObselType(String uri) {
		return new EmptyObselType(uri);
	}
		
	public Method createMethod(String uri) {
		return new EmptyMethod(uri);
	}
	
	
	public static EmptyResourceFactory getInstance() {
		if(instance == null)
			instance = new EmptyResourceFactory();
		return instance;
	}
}
