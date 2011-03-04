package org.liris.ktbs.service;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IObsel;
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
public interface ResourceService {
	
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
	
	/**
	 * Creates a new base in the KTBS
	 * @param baseLocalName the base local name
	 * @param owner the user name of the base's owner
	 * @return the base object
	 */
	public IBase newBase(String baseLocalName, String owner);
	
	/**
	 * 
	 * @param baseUri
	 * @param traceLocalName
	 * @param model
	 * @param origin
	 * @param defaultSubject
	 * @return
	 */
	public IStoredTrace newStoredTrace(String baseUri, String traceLocalName, String model, String origin, String defaultSubject);
	
	/**
	 * 
	 * @param baseUri
	 * @param traceLocalName
	 * @param methodUri
	 * @param sourceTraces
	 * @param parameters
	 * @return
	 */
	public IComputedTrace newComputedTrace(
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
	 * @param etag
	 * @param parameters
	 * @return
	 */
	public IMethod newMethod(
			String baseUri, 
			String methodLocalName, 
			String inheritedMethod, 
			String etag,
			Map<String,String> parameters
			);
	
	/**
	 * Creates a new trace model in the KTBS.
	 * 
	 * @param baseUri the uri of the base that will contain the created trace model
	 * @param modelLocalName the name of the trace 
	 * @return the created trace model, or null if none is created
	 */
	public ITraceModel newTraceModel(String baseUri, String modelLocalName);

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
	 * @return
	 */
	public IObsel newObsel(
			String storedTraceUri,
			String obselLocalName,
			String typeUri,
			String beginDT,
			String endDT,
			BigInteger begin,
			BigInteger end,
			String subject,
			Map<String, Object> attributes
			);


	/**
	 * Delete a resource from the KTBS.
	 * 
	 * @param uri the uri of the resource to be deleted
	 * @param cascadeLinked true if linked resources should be deleted in cascade too
	 * @param cascadeChildren true if children resources should be deleted in cascade too
	 * @return true if the deletion is operated successfully, false otherwise
	 */
	public boolean deleteResource(String uri, boolean cascadeLinked, boolean cascadeChildren);

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
