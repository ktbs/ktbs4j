package org.liris.ktbs.dao.rest;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.liris.ktbs.domain.interfaces.IKtbsResource;


public class KtbsResponseImpl implements KtbsResponse {

	private IKtbsResource resource;
	private boolean executedWithSuccess;
	private KtbsResponseStatus ktbsResponseStatus;
	private HttpResponse httpResponse;
	private String contentAsString;
	private String requestUri;
	
	@Override
	public String getRequestUri() {
		return requestUri;
	}

	public KtbsResponseImpl(IKtbsResource resource, String requestUri, String body, boolean executedWithSuccess,
			KtbsResponseStatus ktbsResponseStatus,
			HttpResponse httpResponse) {
		super();
		this.resource = resource;
		this.requestUri = requestUri;
		this.executedWithSuccess = executedWithSuccess;
		this.ktbsResponseStatus = ktbsResponseStatus;
		this.httpResponse = httpResponse;
		this.contentAsString = body;
	}

	@Override
	public boolean hasSucceeded() {
		return executedWithSuccess;
	}


	@Override
	public String getServerMessage() {
		return contentAsString;
	}

	@Override
	public KtbsResponseStatus getKtbsStatus() {
		return ktbsResponseStatus;
	}

	@Override
	public String toString() {
		String sep = System.getProperty ( "line.separator" );
		sep=(sep==null)||sep.equals("")?"\n":sep;
		String s = (hasSucceeded()?"## Request succeeded ":"## Request failed " + "| "+ktbsResponseStatus+" ##");
		s+=sep;
		if(httpResponse != null)
			s+=httpResponse.getStatusLine() + "" + sep;
		try {
			if(httpResponse.getEntity() != null)
				s+=EntityUtils.toString(httpResponse.getEntity())+sep;
		} catch (Exception e) {
		}
		s+=resource==null?"(Resource is null)":"(Resource is not null)"+sep;
		return s;
	}

	@Override
	public String getHTTPETag() {
		return readHeader(HttpHeaders.ETAG);
	}

	public String readHeader(String headerName) {
		if(httpResponse==null)
			return null;

		Header[] headers = httpResponse.getHeaders(headerName);

		if(headers==null || headers.length == 0)
			return null;
		return headers[0].getValue();
	}

	@Override
	public String getHTTPLocation() {
		return readHeader(HttpHeaders.LOCATION);
	}

	@Override
	public String getBodyAsString() {
		return contentAsString;

	}

	@Override
	public String getMimeType() {
		Header contentType = httpResponse.getEntity().getContentType();
		if(contentType == null)
			return null;
		else 
			return contentType.getValue();
	}

	@Override
	public int getHttpStatusCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}

}
