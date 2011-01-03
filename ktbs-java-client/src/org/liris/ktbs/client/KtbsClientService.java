package org.liris.ktbs.client;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;

/**
 * 
 * An interface specifying all method to resources of a KTBS Server. In this interface, 
 * one method is bound to one REST query, and thus to one underlying HTTP request (maybe 
 * to when HTTP redirection are followed automatically).
 * 
 * @author Damien Cram
 *
 */
public interface KtbsClientService {
	public KtbsResponse getETag(String resourceURI);

	/*
	 * Services bound to a GET request
	 */
	public KtbsResponse getKtbsRoot();
	public KtbsResponse getBase(String baseLocalName);
	public KtbsResponse getBaseFromURI(String baseURI);
	public KtbsResponse getObsel(String baseLocalName, String traceLocalName, String obselLocalName);
	public KtbsResponse getObsel(String traceURI, String obselLocalName);
	public KtbsResponse getObsel(String obselURI);
	public KtbsResponse getTraceInfo(String traceURI);
	public KtbsResponse getTraceInfo(String baseLocalName, String traceLocalName);
	public KtbsResponse getTraceObsels(String traceURI);
	public KtbsResponse getTraceObsels(String baseURI, String traceLocalName);
	public KtbsResponse getTraceObsels(String rootURI, String baseLocalName, String traceLocalName);
	public KtbsResponse getKtbsResource(String resourceURI, Class<?> clazz);

	/*
	 * Services bound to a POST request
	 */
	public KtbsResponse createBase(String baseLocalName, String label);
	public KtbsResponse createTraceModel(String baseLocalName, String traceModelLocalName, String label);
	public KtbsResponse createTrace(String baseLocalName, String traceLocalName, String traceModelLocalName, Date origin, String label);
	public KtbsResponse createKtbsResource(String resourceURI, Reader reader);
	public KtbsResponse createKtbsResource(String resourceURI, InputStream stream);
	
	/**
	 * Creates a collection of obsels remotely on the KTBS server in a unique trace. The local names
	 * of created obsels are set automatically by the KTBS server, meaning that the value returned by 
	 * obsel.getURI() when requesting for the creation of new obsels.
	 * 
	 * <p>
	 * In the current version of the KTBS API, the only way to create multiple 
	 * obsels is to send as many POST requests to the KTBS server as obsels to create.
	 * </p>
	 * 
	 * @param traceURI the URI of the unique parent trace contained all the created obsels
	 * @param obsels the collection of obsels to be created 
	 * @return the {@link KtbsResponse} objects sent back by the KTBS server. Since each obsel creation 
	 * is performed by one POST request, there are as many objects in the returned array as there
	 * are obsels in the obsels parameter.
	 */
	public KtbsResponse[] addObselsToTrace(String traceURI, Collection<Obsel> obsels);
	
	/**
	 * Creates an obsel on the KTBS server remotely, using a POST request.
	 * <p>
	 * Fails with a "HTTP 400 Already in use" response in some of the following cases:
	 *  - the URI of the obsel has already been posted as a target obsel for an inter-obsel relation.
	 *  
	 *  In such cases, retry submitting this obsel with a put request.
	 * </p>
	 * @param traceURI the URI of tha parent trace of the obsel
	 * @param obselLocalName the local name of the obsel
	 * @param subject the subject of the obsel, can be null
	 * @param label any comment about the obsel, can be null
	 * @param typeURI the URI of the obsel type, must already exist in the KTBS server
	 * @param beginDT the absolute begin date of the obsel (bound to ktbs:hasBeginDT), default is null
	 * @param endDT the end date of the obsel (bound to ktbs:hasEndDT), default is null
	 * @param begin the begin date in milliseconds relative to the trace origin, default is null
	 * @param end the end date in milliseconds relative to the trace origin, default is null
	 * @param attributes the attributes of the obsel. Key values are attribute URIs.
	 * @param outgoingRelations an array that holds alternatively the outgoing relation URIs
	 * and URIs of the target obsels. The number of elements in this array must be an even number, 
	 * otherwise this parameter is ignored. This array is interpreted according to this scheme: 
	 * ["relationURI_1", "targetObselURI_1", "relationURI_2", "targetObselURI_2", etc].
	 * @return the {@link KtbsResponse} object of this request
	 * @see method addObselsToTrace()
	 */
	public KtbsResponse createObsel(
			String traceURI, 
			String obselLocalName, 
			String subject, 
			String label, 
			String typeURI, 
			Date beginDT, 
			Date endDT, 
			long begin, 
			long end, 
			Map<String, Object> attributes, 
			String... outgoingRelations);

	/*
	 * Services bound to a PUT request
	 */

	/**
	 * 
	 * Resubmit a trace and all its obsels to a KTBS server, by the mean
	 * of an underlying  PUT request.
	 * 
	 * @param traceURI the trace URI to be resubmitted to the KTBS server
	 * @param obsels the collection of modified obsels to send to the server
	 * @param traceETag the ETag (the value of the etag header in the HTTP response 
	 * sent by the KTBS server when getting the trace) of the trace resource when 
	 * the last GET was performed.
	 * @return the {@link KtbsResponse} object of this request
	 * @return
	 */
	public KtbsResponse putTraceObsels(String traceURI, Collection<Obsel> obsels,String traceETag);

	/**
	 * Resubmit trace metadata to a KTBS server, by the mean
	 * of an underlying  PUT request.
	 * 
	 * @param trace the trace to be resubmitted to the KTBS server
	 * @param traceETag the ETag (the value of the etag header in the HTTP response 
	 * sent by the KTBS server when getting the trace) of the trace resource when 
	 * the last GET was performed.
	 * @return the {@link KtbsResponse} object of this request
	 */
	public KtbsResponse putTraceInfo(Trace trace, String traceETag);

	
	public KtbsResponse putKtbsResource(String resourceURI, Reader reader, String etag);
	public KtbsResponse putKtbsResource(String resourceURI, InputStream stream, String etag);
	
	/*
	 * Services bound to a DELETE request
	 */
	public KtbsResponse deleteBase(String baseLocalName);
	public KtbsResponse deleteBaseFromURI(String baseURI);
	public KtbsResponse deleteTrace(String baseLocalName, String traceLocalName);
	public KtbsResponse deleteTrace(String traceURI);
}