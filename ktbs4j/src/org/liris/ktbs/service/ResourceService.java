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
 * @see CachingResourceService
 * @see TraceModelService
 * @see StoredTraceService
 */
public interface ResourceService extends IRootAwareService, ResponseAwareService {
	
	/**
	 * Retrieve the root of the KTBS.
	 * 
	 * @return the root object, null
	 * if the underlying request failed
	 */
	public IRoot getRoot();
	
	/**
	 * Retrieve a base from the KTBS.
	 * 
	 * @param uri the base uri (either relative to the root or absolute)
	 * @return the base object, null 
	 * if no base exists for that uri or 
	 * if the underlying request failed
	 */
	public IBase getBase(String uri);
	
	/**
	 * Retrieve a method from the KTBS.
	 * 
	 * @param uri the uri of the method (either relative to the root or absolute)
	 * @return the method object, null 
	 * if no method exists for that uri or 
	 * if the underlying request failed
	 */
	public IMethod getMethod(String uri);
	
	/**
	 * Retrieve a stored trace from the KTBS.
	 * 
	 * @param uri the uri of the stored trace (either relative to the root or absolute)
	 * @return the stored trace object, null 
	 * if no stored trace exists for that uri or 
	 * if the underlying request failed
	 */
	public IStoredTrace getStoredTrace(String uri);
	
	/**
	 * Retrieve a computed trace from the KTBS.
	 * 
	 * @param uri the uri of the computed trace (either relative to the root or absolute)
	 * @return the computed trace object, null 
	 * if no computed trace exists for that uri or 
	 * if the underlying request failed
	 */
	public IComputedTrace getComputedTrace(String uri);
	
	
	/**
	 * Retrieve a trace from the KTBS.
	 * 
	 * @param uri the uri of the trace (either relative to the root or absolute)
	 * @return the trace object, null 
	 * if no trace exists for that uri or 
	 * if the underlying request failed
	 */
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
	 * 
	 * @param uri
	 * @return
	 */
	public IKtbsResource getResource(String uri);
	
	/**
	 * Tells if a resource is defined in the KTBS for a given uri.
	 * 
	 * @param uri the uri of the resource
	 * @return true if the uri is defined in the KTBS, false otherwise
	 */
	public boolean exists(String uri);
	
	/**
	 * Creates a new base in the KTBS.
	 * 
	 * @param baseLocalName the base local name
	 * @return the uri of the created base, null if the creation failed
	 */
	public String newBase(String baseLocalName);
	

	/**
	 * Creates a new stored trace in the KTBS.
	 * 
	 * @param baseUri the uri of the parent base containing the new trace (either relative to the root or absolute)
	 * @param traceLocalName the name of the trace to create (null if anonymous)
	 * @param modelUri the absolute uri of the trace model
	 * @param origin the origin of the new strace
	 * @param traceBegin the trace begin date (relative to the origin)
	 * @param traceBeginDT the trace absolute begin date
	 * @param traceEnd the trace end date (relative to the origin)
	 * @param traceEndDT the trace absolute end date
	 * @param defaultSubject the default subject
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
	 * Creates a new computed trace in the KTBS.
	 * 
	 * @param baseUri the uri of the parent base containing the new trace (either relative to the root or absolute)
	 * @param traceLocalName the name of the trace to create (null if anonymous)
	 * @param methodUri the absolute uri of the method used to produce 
	 * the computed trace from the source traces
	 * @param sourceTraces the uris of the traces given as input to 
	 * the transformation process that will produce the computed trace
	 * trace transformation on the KTBS server
	 * @param parameters the KTBS parameters overwriting the transformation
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
	 * Creates a new computed trace in the KTBS.
	 * 
	 * @param baseUri the uri of the parent base containing the new trace (either relative to the root or absolute)
	 * @param traceLocalName the name of the trace to create (null if anonymous)
	 * @param methodUri the absolute uri of the method used to produce 
	 * the computed trace from the source traces
	 * @param sourceTraces the uris of the traces given as input to 
	 * the transformation process that will produce the computed trace
	 * trace transformation on the KTBS server
	 * @param parameters the KTBS parameters overwriting the transformation
	 * @param label
	 * @return the uri of the created trace, null if the creation failed
	 */
	public String newComputedTrace(
			String baseUri, 
			String traceLocalName, 
			String methodUri, 
			Set<String> sourceTraces,
			Map<String,String> parameters,
			String label
			);
	
	/**
	 * Creates a new method in the KTBS.
	 * 
	 * @param baseUri the uri of the parent base containing the new method (either relative to the root or absolute)
	 * @param methodLocalName the name of the method to create (null if anonymous)
	 * @param inheritedMethod the uri of the parent method that the created method will extend
	 * @param parameters the parameters that configures the method
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
	 * @param baseUri the uri of the base containing the created trace model (either relative to the root or absolute)
	 * @param modelLocalName the name of the trace (null if anonymous)
	 * @return the uri of the created trace model, null if the creation failed
	 */
	public String newTraceModel(String baseUri, String modelLocalName);

	/**
	 * Creates a new obsel in the KTBS.
	 * 
	 * @param storedTraceUri the uri of the parent stored trace ((either relative to the root or absolute)
	 * @param obselLocalName the local name of the new obsel (null if anonymous)
	 * @param typeUri the uri of the obsel type
	 * @param beginDT the absolute begin date
	 * @param endDT the absolute end date
	 * @param begin the begin date (relative to the parent trace's origin)
	 * @param end the end date (relative to the parent trace's origin)
	 * @param subject the subject of this obsel
	 * @param attributes the attributes pairs of the obsel
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
	 * Creates a new obsel in the KTBS.
	 * 
	 * @param storedTraceUri the uri of the parent stored trace ((either relative to the root or absolute)
	 * @param obselLocalName the local name of the new obsel (null if anonymous)
	 * @param typeUri the uri of the obsel type
	 * @param beginDT the absolute begin date
	 * @param endDT the absolute end date
	 * @param begin the begin date (relative to the parent trace's origin)
	 * @param end the end date (relative to the parent trace's origin)
	 * @param subject the subject of this obsel
	 * @param attributes the attributes pairs of the obsel
	 * @param labels the labels of obsel
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
		Set<IAttributePair> attributes,
		Set<String> labels
	);


	/**
	 * Delete a resource from the KTBS.
	 * 
	 * @param uri the uri of the resource to be deleted
	 * @return true if the deletion has operated successfully, false otherwise
	 */
	public boolean deleteResource(String uri);
	
	/**
	 * Delete a resource from the KTBS.
	 * 
	 * @param resource the resource to be deleted
	 * @return true if the deletion has operated successfully, false otherwise
	 */
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
