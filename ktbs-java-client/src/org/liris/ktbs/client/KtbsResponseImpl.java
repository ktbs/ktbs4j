package org.liris.ktbs.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.liris.ktbs.core.KtbsResource;

public class KtbsResponseImpl implements KtbsResponse {


	private KtbsResource resource;
	private boolean executedWithSuccess;
	private KtbsResponseStatus ktbsResponseStatus;
	private HttpResponse httpResponse;
	private Object value;

	public KtbsResponseImpl(KtbsResource resource, boolean executedWithSuccess,
			KtbsResponseStatus ktbsResponseStatus,
			HttpResponse httpResponse) {
		super();
		this.resource = resource;
		this.executedWithSuccess = executedWithSuccess;
		this.ktbsResponseStatus = ktbsResponseStatus;
		this.httpResponse = httpResponse;
	}

	@Override
	public KtbsResource getBodyAsKtbsResource() {
		return resource;
	}

	@Override
	public boolean executedWithSuccess() {
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
			return IOUtils.toString(content);
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
	public HttpResponse getHTTPResponse() {
		return httpResponse;
	}

	@Override
	public String toString() {
		String sep = System.getProperty ( "line.separator" );
		sep=(sep==null)||sep.equals("")?"\n":sep;
		String s = (executedWithSuccess()?"## Request succeeded ":"## Request failed " + "| "+ktbsResponseStatus+" ##");
		s+=sep;
		if(httpResponse != null)
			s+=httpResponse.getStatusLine() + "" + sep;
		try {
			if(httpResponse.getEntity() != null)
				s+=IOUtils.toString(httpResponse.getEntity().getContent())+sep;
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
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
	}
}
