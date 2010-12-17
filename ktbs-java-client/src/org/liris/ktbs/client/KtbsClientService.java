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
	public KtbsResponse createObsel(String traceURI, String obselLocalName, String label, String typeURI, Date begin, Date end, Map<String, Serializable> attributes, String... outgoingRelations);
	public KtbsResponse createRelation(String traceURI, String relationName, String fromObselURI, String toObselURI);

	/*
	 * Services bound to a PUT request
	 */
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