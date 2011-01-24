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

/**
 * A container for KTBS resources. 
 * 
 * <p>
 * The {@link ResourceRepository} helps accessing the resources, performing global 
 * operations over the resources and maintaining the coherence between resources.
 * </p>
 * 
 * @author Damien Cram
 *
 */
public interface ResourceRepository {
	
	/**
	 * The KTBS built-in resources, always contained in the repository.
	 */
	public static final KtbsResource[] BUILTIN_RESOURCES = new KtbsResource[]{
		Method.FILTER,
		Method.FUSION
	};
	                                                                        
	/**
	 * Give the resource of a given uri and a given type contained in this repository.
	 * 
	 * @param <T> the type of the resource to find and return
	 * @param uri the uri of the requested resource
	 * @param clazz the expected {@link KtbsResource} class of the requested
	 * @return the resource with the given uri contained in this repository if any
	 * @throws ResourceNotFoundException when no resource with the requested uri can be found
	 * @throws ClassCastException when the resource has been found, but is not of the requested Java class
	 */
	public <T extends KtbsResource> T getResource(String uri, Class<T> clazz);
	
	/**
	 * Give the resource of a given uri contained in this repository.
	 * 
	 * @param uri the uri of the requested resource
	 * @return the resource with the given uri contained in this repository if any
	 * @throws ResourceNotFoundException when no resource with the requested uri can be found
	 */
	public KtbsResource getResource(String uri);
	
	/**
	 * Tell if there exists a KTBS resource with a given uri in this repository.
	 * 
	 * @param uri the requested uri
	 * @return true if there exists a resource with the param uri in this repository, false otherwise
	 */
	public boolean exists(String uri);
	
	/**
	 * Tell if there exists a KTBS resource with a given uri and of 
	 * a given type in this repository.
	 * 
	 * @param <T> the requested resource type
	 * @param uri the requested uri
	 * @param cls the class of the requested resource type
	 * @return true if there exists a resource with the param uri and of the 
	 * requested type in this repository, false otherwise
	 */
	public  <T extends KtbsResource> boolean existsOfType(String uri, Class<T> cls);

	/**
	 * Create a new base in this repository.
	 * 
	 * @param baseURI the uri of the base
	 * @return the created base
	 */
	public Base createBase(String baseURI);
	
	/**
	 * Create a new stored trace in this repository.
	 * 
	 * @param base the parent base that will own the created stored trace
	 * @param traceUri the uri of the created trace
	 * @param traceModel the trace model of the created trace
	 * @return the created stored trace
	 * @throws ResourceNotFoundException when any required resource 
	 * (base or trace model) is not found in the repository
	 */
	public StoredTrace createStoredTrace(Base base, String traceUri, TraceModel traceModel);
	
	/**
	 * Create a new computed trace in this repository.
	 * 
	 * @param base the parent base that will own the created stored trace
	 * @param traceUri the uri of the created trace
	 * @param traceModel the trace model of the created trace
	 * @param method the method used by a transformation engine to produce the content of the created trace
	 * @param sources the collection of traces used by a transformation engine to produce the content of the created trace
	 * @return the created computed trace
	 * @throws ResourceNotFoundException when any required resource 
	 * (base, trace model, method, or source traces) is not found in the repository.
	 */
	public ComputedTrace createComputedTrace(Base base, String traceUri,
			TraceModel traceModel, Method method, Collection<Trace> sources);

	
	/**
	 * Create a new method in this repository.
	 * 
	 * @param base the parent base that will own the created stored trace
	 * @param methodUri the uri of the created method
	 * @param inherits the uri of the inherited method
	 * @return the created method
	 * @throws ResourceNotFoundException when any required resource 
	 * (the base) is not found in the repository.
	 */
	public Method createMethod(Base base, String methodUri, String inherits);
	
	/**
	 * Create a new trace model in this repository.
	 * 
	 * @param base the parent base that will own the created stored trace
	 * @param modelUri the uri of the created trace model
	 * @return the created trace model
	 * @throws ResourceNotFoundException when any required resource 
	 * (the base) is not found in the repository.
	 */
	public TraceModel createTraceModel(Base base, String modelUri);
	
	/**
	 * Create a new obsel in this repository.
	 * 
	 * @param trace the trace containing the new obsel
	 * @param obselUri the uri of the created obsel
	 * @param type the obsel type of the created obsel
	 * @param attributes the map of attributes to be set to the created obsel
	 * @return the created obsel
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace, obsel type, attribute type) is not found in the repository.
	 */
	public Obsel createObsel(StoredTrace trace, String obselUri,
			ObselType type, Map<AttributeType, Object> attributes);
	
	
	/**
	 * Create a new obsel type in the repository.
	 * 
	 * @param traceModel the parent trace model of the new obsel type
	 * @param localName the local name of the created obsel type
	 * @return the created obsel type
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace model) is not found in the repository.
	 */
	public ObselType createObselType(TraceModel traceModel, String localName);

	/**
	 * Create a new relation type in the repository.
	 * 
	 * @param traceModel the parent trace model of the new relation type
	 * @param localName the local name of the created relation type
	 * @param domain the domain of the created relation type
	 * @param range the range of the created relation type
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace model, obsel types for range and domain) is not in the repository;
	 */
	public RelationType createRelationType(TraceModel traceModel, String localName, ObselType domain, ObselType range);
	
	/**
	 * Create a new attribute type in the repository.
	 * 
	 * @param traceModel the parent trace model of the new attribute type
	 * @param localName the local name of the created attribute type
	 * @param domain the domain of the created attribute type
	 * @throws ResourceNotFoundException when any required resource 
	 * (trace model, obsel type for the domain) is not found in the repository.
	 */
	public AttributeType createAttributeType(TraceModel traceModel, String localName, ObselType domain);

	/**
	 * Remove an obsel from a trace.
	 * 
	 * @param trace the parent trace  of the obsel to remove
	 * @param obsel the obsel to be removed
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
	 * @param stream the input stream containing the KTBS resources
	 * @param lang the Jena syntax that was used to serialize the resource into the stream
	 * @throws MultipleResourcesInStreamException when the content of the stream cannot be interpreted as 
	 * a valid Ktbs resource.
	 */
	public void loadResource(InputStream stream, String lang) throws ResourceLoadException;
}
