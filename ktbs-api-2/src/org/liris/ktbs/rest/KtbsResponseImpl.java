package org.liris.ktbs.rest;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;


public class KtbsResponseImpl implements KtbsResponse {

	private static Log log = LogFactory.getLog(ApacheKtbsRestClient.class);

	private IKtbsResource resource;
	private boolean executedWithSuccess;
	private KtbsResponseStatus ktbsResponseStatus;
	private HttpResponse httpResponse;
	private String contentAsString;

	public KtbsResponseImpl(IKtbsResource resource, String body, boolean executedWithSuccess,
			KtbsResponseStatus ktbsResponseStatus,
			HttpResponse httpResponse) {
		super();
		this.resource = resource;
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
		HttpEntity entity = httpResponse.getEntity();
		if(entity == null)
			return "";
		InputStream content;
		try {
			content = entity.getContent();
			if (content == null) {
				return "";
			}
			return EntityUtils.toString(entity);
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
		return "";
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
}
