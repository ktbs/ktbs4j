package org.liris.ktbs.core;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.ObselType;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;

public interface ResourceRepository {
	
	public static final KtbsResource[] BUILTIN_RESOURCES = new KtbsResource[]{
		Method.FILTER,
		Method.FUSION
	};
	                                                                        
	
	public <T extends KtbsResource> T getResource(String uri, Class<T> clazz);
	public KtbsResource getResource(String uri);
	
	public boolean exists(String uri);
	public  <T extends KtbsResource> boolean existsOfType(String uri, Class<T> class1);
	
	/**
	 * Registers a Ktbs resource to this repository.
	 * <p>
	 * If the resource is a {@link TraceModel}, a {@link Trace}, a {@link StoredTrace} 
	 * or a {@link ComputedTrace}, all existing resources with the same URI and all children 
	 * of the existing resource are removed, then the parameter resource and all its children are
	 * added, overriding any existing resources with same uris.
	 * </p>
	 * <p>
	 * If the resource is a {@link Obsel}, a {@link ObselType}, a {@link AttributeType} 
	 * or a {@link RelationType}, the resource is not added, and an exception is thrown.
	 * </p>
	 * <p>
	 * If the resource is a {@link Base}, a {@link KtbsRoot} or a {@link Method}, the resource 
	 * is registered, with no other processing.
	 * </p>
	 * @param resource the resource to be registered in the repository
	 * @throws UnsupportedOperationException when the parameter resource is a {@link Obsel}, 
	 * a {@link ObselType}, a {@link AttributeType} or a {@link RelationType}.
	 */
//	public <T extends KtbsResource> T putResource(T resource);

	/**
	 * Removes a resource from the repository.
	 * 
	 * <p>
	 * If the resource is a {@link TraceModel}, a {@link Trace}, a {@link StoredTrace} 
	 * or a {@link ComputedTrace}, the resource and all its children are removed.
	 * </p>
	 * <p>
	 * If the resource is a {@link Obsel}, a {@link ObselType}, a {@link AttributeType} 
	 * or a {@link RelationType}, the resource is not removed, and an exception is thrown.
	 * </p>
	 * <p>
	 * If the resource is a {@link Base}, a {@link KtbsRoot} or a {@link Method}, the resource 
	 * is removed, with no other processing.
	 * </p>
	 * @param <T> the type of the resource to be removed
	 * @param resource the resource to be removed
	 * @throws UnsupportedOperationException when the parameter resource is a {@link Obsel}, 
	 * a {@link ObselType}, a {@link AttributeType} or a {@link RelationType}.
	 */
//	public <T extends KtbsResource> void removeResource(T resource);

	/**
	 * Create an empty base and register it to the repository.
	 * 
	 * @param baseURI the URI of the base
	 * @return the created base
	 */
	public Base createBase(String baseURI);
	
	/**
	 * 
	 * @param base
	 * @param traceURI
	 * @param tm
	 * @return
	 * @throws ResourceNotFoundException when any required resource 
	 * (base or trace model) is not in the repository;
	 */
	public StoredTrace createStoredTrace(Base base, String traceURI, TraceModel tm);
	
	/**
	 * 
	 * @param base
	 * @param traceURI
	 * @param tm
	 * @param m
	 * @param sources
	 * @return
	 * @throws ResourceNotFoundException when any required resource 
	 * (base, trace model, method, or source traces) is not in the repository;
	 */
	public ComputedTrace createComputedTrace(Base base, String traceURI,
			TraceModel tm, Method m, Collection<Trace> sources);

	
	/**
	 * 
	 * @param base
	 * @param methodURI
	 * @param inherits
	 * @return
	 * @throws ResourceNotFoundException when any required resource 
	 * (the base) is not in the repository;
	 */
	public Method createMethod(Base base, String methodURI, String inherits);
	
	/**
	 * 
	 * @param base
	 * @param modelURI
	 * @return
	 * @throws ResourceNotFoundException when any required resource 
	 * (the base) is not in the repository;
	 */
	public TraceModel createTraceModel(Base base, String modelURI);
	
	/**
	 * 
	 * @param trace
	 * @param obselURI
	 * @param hasTrace
	 * @param type
	 * @param attributes
	 * @return
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace, obsel type, attribute type) is not in the repository;
	 */
	public Obsel createObsel(StoredTrace trace, String obselURI,
			ObselType type, Map<AttributeType, Object> attributes);
	
	
	/**
	 * 
	 * @param traceModel
	 * @param localName
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace model) is not in the repository;
	 */
	public ObselType createObselType(TraceModel traceModel, String localName);

	/**
	 * 
	 * @param traceModel
	 * @param localName
	 * @param domain
	 * @param range
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace model, obsel types for range and domain) is not in the repository;
	 */
	public RelationType createRelationType(TraceModel traceModel, String localName, ObselType domain, ObselType range);
	
	/**
	 * 
	 * @param traceModel
	 * @param localName
	 * @param domain
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace model, obsel type for the domain) is not in the repository;
	 */
	public AttributeType createAttributeType(TraceModel traceModel, String localName, ObselType domain);

	/**
	 * 
	 * @param trace
	 * @param obsel
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace, obsel) is not in the repository;
	 */
	public void removeObsel(StoredTrace trace, Obsel obsel);
	
	/**
	 * Check if resources are present in the repository, throws an 
	 * {@link ResourceNotFoundException} if not.
	 * 
	 * @param resources the resources to be checked
	 * @throws ResourceNotFoundException when any of the parameter 
	 * resources is not found in the repository
	 */
	public void checkExistency(KtbsResource... resources);
	
	
	/**
	 * Check if there exists a ktbs resource in the repository
	 * for each of the parameter uri, throws an 
	 * {@link ResourceNotFoundException} if not.
	 * 
	 * @param uris the uris to be checked
	 * @throws ResourceNotFoundException when any of the parameter 
	 * uris is not found in the repository.
	 */
	public void checkExistency(String... uris);
	
	
	/**
	 * Reads a Ktbs resource from an input stream and register it.
	 * 
	 * <p>
	 * If the resource loaded is an Obsel, the containing trace got by the 
	 * ktbs:hasTrace property is updated with this new obsel.
	 * </p>
	 * <p>
	 * If the resource loaded is an AttributeType, a RelationType, or an ObselType, 
	 * the containing trace model is obtained by resolving the parent URI of the resource.
	 * </p>
	 * 
	 * @param stream
	 * @param lang
	 * @return
	 * @throws MultipleResourcesInStreamException when the content of the stream cannot be interpreted as 
	 * a valid Ktbs resource.
	 */
	public void loadResource(InputStream stream, String lang) throws ResourceLoadException;
}
