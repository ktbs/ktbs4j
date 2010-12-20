package org.liris.ktbs.client;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.Obsel;

public interface KtbsClientService {

	/*
	 * Services bound to a GET request
	 */
	public KtbsResponse getKtbsRoot(String rootURI);
	public KtbsResponse getBase(String rootURI, String baseLocalName);
	public KtbsResponse getBase(String baseURI);
	public KtbsResponse getObsel(String traceURI, String obselLocalName);
	public KtbsResponse getObsel(String obselURI);
	public KtbsResponse getTraceInfo(String traceURI);
	public KtbsResponse getTraceInfo(String baseURI, String traceLocalName);
	public KtbsResponse getTraceInfo(String rootURI, String baseLocalName, String traceLocalName);
	public KtbsResponse getTraceObsels(String traceURI);
	public KtbsResponse getTraceObsels(String baseURI, String traceLocalName);
	public KtbsResponse getTraceObsels(String rootURI, String baseLocalName, String traceLocalName);
	

	/*
	 * Services bound to a POST request
	 */
	public KtbsResponse createBase(String rootURI, String baseLocalName, String label);
	public KtbsResponse createTraceModel(String baseURI, String traceModelLocalName, String label);
	public KtbsResponse createTrace(String baseURI, String traceLocalName, String traceModelURI, Date origin, String label);
	public KtbsResponse addObselsToTrace(String traceURI, Collection<Obsel> obsels);
	
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
	 * @param begin the absolute begin date of the obsel (bound to ktbs:hasBeginDT)
	 * @param end the end date of the obsel (bound to ktbs:hasEndDT)
	 * @param attributes the attributes of the obsel. Key values are attribute URIs.
	 * @param outgoingRelations an array that holds alternatively the outgoing relation URIs
	 * and URIs of the target obsels. The number of elements in this array must be an even number, 
	 * otherwise this parameter is ignored. This array is interpreted according to this scheme: 
	 * ["relationURI_1", "targetObselURI_1", "relationURI_2", "targetObselURI_2", etc].
	 * @return the {@link KtbsResponse} object of this request
	 * @see method addObselToTace()
	 */
	public KtbsResponse createObsel(String traceURI, String obselLocalName, String subject, String label, String typeURI, Date begin, Date end, Map<String, Serializable> attributes, String... outgoingRelations);

	/*
	 * Services bound to a PUT request
	 */
	public KtbsResponse createRelation(String traceURI, String relationName, String fromObselURI, String toObselURI);
	public KtbsResponse addAttributeToObsel(String obselURI, String attributeName, Serializable value);
	public KtbsResponse deleteRelation(String traceURI, String relationName, String fromObselURI, String toObselURI);

	/*
	 * Services bound to a DELETE request
	 */
	public KtbsResponse deleteBase(String rootURI, String baseLocalName);
	public KtbsResponse deleteBase(String rootURI, Base base);
	public KtbsResponse deleteBase(String baseURI);
	public KtbsResponse deleteTrace(String baseURI, String traceLocalName);
	public KtbsResponse deleteTrace(String traceURI);
	public KtbsResponse deleteObsel(String obselURI);
}