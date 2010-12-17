package org.liris.ktbs.client;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;

public interface KtbsClientService {

	/*
	 * Services bound to a GET request
	 */
	public KtbsRoot getKtbsRoot(String rootURI);
	public Base getBase(String rootURI, String baseLocalName);
	public Base getBase(String baseURI);
	public Collection<String> listBaseURIs(String rootURI);
	public Collection<String> listTraceURIs(String baseURI);
	public Trace getTrace(String traceURI);
	public Trace getTrace(String baseURI, String traceLocalName);
	public Trace getTrace(String rootURI, String baseLocalName, String traceLocalName);
	public Collection<String> listTraceModelURIs(String baseURI);
	

	/*
	 * Services bound to a POST request
	 */
	public void createBase(String rootURI, String baseLocalName);
	public void addBaseToRoot(String rootURI, Base base);
	public void createTrace(String baseURI, String traceLocalName, String traceModelURI, Date origin);
	public void createTrace(String baseURI, Trace trace);
	public void createObsel(String traceURI, Obsel obsel);
	public void createRelation(String traceURI, String relationName, String fromObselURI, String toObselURI);

	/*
	 * Services bound to a PUT request
	 */
	public void addAttributeToObsel(String obselURI, String attributeName, Serializable value);
	public void deleteRelation(String traceURI, String relationName, String fromObselURI, String toObselURI);

	/*
	 * Services bound to a DELETE request
	 */
	public void deleteBase(String rootURI, String baseLocalName);
	public void deleteBase(String rootURI, Base base);
	public void deleteBase(String baseURI);
	public void deleteTrace(String baseURI, String traceLocalName);
	public void deleteTrace(String traceURI);
	public void deleteObsel(String obselURI);
}