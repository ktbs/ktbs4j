package org.liris.ktbs.service;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;

/**
 * An interface for the manipulation of KTBS resources: creation, deletion, 
 * update, and retrieve.
 * 
 * <p>
 * All resource uris passed to these methods can be either absolute or 
 * relative to the KTBS root uri, or to the contextual parent resource of each method.
 * </p>
 * 
 * @author Damien Cram
 *
 */
public interface ResourceService extends IRootAwareService, ResponseAwareService {
	
	/**
	 * Give the root resource.
	 * 
	 * @return the root object
	 */
	public IRoot getRoot();
	
	/**
	 * Retrieve a base from the KTBS.
	 * 
	 * @param uri the base uri
	 * @return the base object
	 */
	public IBase getBase(String uri);
	
	/**
	 * Retrieve a method from the KTBS.
	 * 
	 * @param uri the uri of the method
	 * @return the method object
	 */
	public IMethod getMethod(String uri);
	
	public IStoredTrace getStoredTrace(String uri);
	
	public IComputedTrace getComputedTrace(String uri);
	
	public ITrace getTrace(String uri);
	
	public ITraceModel getTraceModel(String uri);
	
	/**
	 * Retrieve a resource from the KTBS.
	 * 
	 * @param <T> the type of the resource to retrieve
	 * @param uri the uri of the resource
	 * @param cls the class object of the resource type
	 * @return the retrieved resource object
	 */
	public <T extends IKtbsResource> T getResource(String uri, Class<T> cls);
	public IKtbsResource getResource(String uri);
	public boolean exists(String uri);
	
	/**
	 * Creates a new base in the KTBS.
	 * 
	 * @param baseLocalName the base local name
	 * @return the uri of the created base, null if the creation failed
	 */
	public String newBase(String baseLocalName);
	
	/**
	 * 
	 * @param baseUri
	 * @param traceLocalName
	 * @param modelUri
	 * @param origin
	 * @param traceBegin
	 * @param traceBeginDT
	 * @param traceEnd
	 * @param traceEndDT
	 * @param defaultSubject
	 * @return the uri of the created trace, null if the creation failed
	 */
	public String newStoredTrace(
			String baseUri, 
			String traceLocalName, 
			String modelUri, 
			String origin,
			BigInteger traceBegin,
			String traceBeginDT,
			BigInteger traceEnd,
			String traceEndDT,
			String defaultSubject
			);
	
	/**
	 * 
	 * @param baseUri
	 * @param traceLocalName
	 * @param methodUri
	 * @param sourceTraces
	 * @param parameters
	 * @return the uri of the created trace, null if the creation failed
	 */
	public String newComputedTrace(
			String baseUri, 
			String traceLocalName, 
			String methodUri, 
			Set<String> sourceTraces,
			Map<String,String> parameters
			);
	
	/**
	 * 
	 * @param baseUri
	 * @param methodLocalName
	 * @param inheritedMethod
	 * @param parameters
	 * @return the uri of the created method, null if the creation failed
	 */
	public String newMethod(
			String baseUri, 
			String methodLocalName, 
			String inheritedMethod, 
			Map<String, String> parameters
			);
	
	/**
	 * Creates a new trace model in the KTBS.
	 * 
	 * @param baseUri the uri of the base that will contain the created trace model
	 * @param modelLocalName the name of the trace 
	 * @return the uri of the created trace model, null if the creation failed
	 */
	public String newTraceModel(String baseUri, String modelLocalName);

	/**
	 * 
	 * @param storedTraceUri
	 * @param obselLocalName
	 * @param typeUri
	 * @param beginDT
	 * @param endDT
	 * @param begin
	 * @param end
	 * @param subject
	 * @param attributes
	 * @return the uri of the created obsel, null if none was created
	 */
	public String newObsel(
			String storedTraceUri,
			String obselLocalName,
			String typeUri,
			String beginDT,
			String endDT,
			BigInteger begin,
			BigInteger end,
			String subject,
			Set<IAttributePair> attributes
			);


	/**
	 * Delete a resource from the KTBS.
	 * 
	 * @param uri the uri of the resource to be deleted
	 * @return true if the deletion is operated successfully, false otherwise
	 */
	public boolean deleteResource(String uri);
	public boolean deleteResource(IKtbsResource resource);

	/**
	 * Save a resource in the KTBS.
	 * 
	 * @param resource the resource to be saved
	 * @return true if the resource has been saved successfully, false otherwise
	 */
	public boolean saveResource(IKtbsResource resource);

	/**
	 * Save a resource in the KTBS.
	 * 
	 * @param resource the resource to be saved
	 * @param cascadeChildren true if the children resources should saved in cascade too
	 * @return true if the resource has been saved successfully, false otherwise
	 */
	public boolean saveResource(IKtbsResource resource, boolean cascadeChildren);
}
