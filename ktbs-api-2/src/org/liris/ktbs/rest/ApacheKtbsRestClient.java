package org.liris.ktbs.rest;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.VersionInfo;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.serial.RdfSerializer;

public class ApacheKtbsRestClient implements KtbsRestClient {

	public static final String MESSAGE_DELETE_NOT_SUPPORTED = "DELETE method are not supported by the KTBS server in the current version";
	private static final String MESSAGE_CLIENT_NOT_STARTED = "The HTTP client is not started. Please call KtbsClient.startSession() before calling any KTBS remote service.";

	private static Log log = LogFactory.getLog(ApacheKtbsRestClient.class);

	private String rootUri;

	@Override
	public void setRootUri(String rootUri) {
		this.rootUri = rootUri;
	}
	
	private HttpClient httpClient;
	private boolean started = false;

	private HttpParams httpParams;

	@Override
	public void startSession() {
		startSession(null, null);
	}
	
	/**
	 * Starts the underlying HTTP client with default parameters for caching 
	 * and general HTTP protocol matters, and loads Jena classes.
	 */
	@Override
	public void startSession(String user, String password) {

		log.info("Starting KtbsClient session for the KTBS root URI \""+rootUri+"\".");

		
		CacheConfig cacheConfig = new CacheConfig();  
		cacheConfig.setMaxCacheEntries(1000);
		cacheConfig.setMaxObjectSizeBytes(10000);

		httpParams = new BasicHttpParams();
		httpParams.setParameter(ClientPNames.HANDLE_REDIRECTS, true);

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		// determine the release version from packaged version info
		final VersionInfo vi = VersionInfo.loadVersionInfo(
				"org.apache.http.client", 
				ApacheKtbsRestClient.class.getClassLoader());
		final String release = (vi != null) ? vi.getRelease() : VersionInfo.UNAVAILABLE;
		HttpProtocolParams.setUserAgent(httpParams, "KTBS Client, based on Apache-HttpClient/" + release + " (java 1.5)");

		log.debug("Creating the caching HTTP client.");
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParams);
		if(user != null) {
			URI asJavaUri = URI.create(rootUri);
			log.info("Sarting Http authentication");
			HttpHost targetHost = new HttpHost(
					asJavaUri.getHost(), 
					asJavaUri.getPort()==-1?80:asJavaUri.getPort(),
							asJavaUri.getScheme()); 
			
			defaultHttpClient.getCredentialsProvider().setCredentials(
					new AuthScope(targetHost.getHostName(), targetHost.getPort()), 
			        new UsernamePasswordCredentials(user, password)
					);
		}
		
		httpClient = new CachingHttpClient(defaultHttpClient, cacheConfig);
		
		this.started = true;

		log.info("Session started.");
	}

	/**
	 * Checks if the underlying HTTP client is started.
	 * 
	 * @return true if started, false if never started or closed
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * Shuts down the connection manager of the underlying HTTP client.
	 */
	public void endSession() {
		log.info("Closing KtbsClient session for the KTBS root URI \""+rootUri+"\".");
		if(httpClient != null) 
			httpClient.getConnectionManager().shutdown();
		this.started = false;
		log.info("Session closed.");
	}

	private void checkStarted() {
		if(!isStarted()) {
			String message = MESSAGE_CLIENT_NOT_STARTED;
			IllegalStateException illegalStateException = new IllegalStateException(message);
			log.error(message, illegalStateException);
			throw illegalStateException;
		}
	}

	private String getGETMimeType() {
		return KtbsConstants.MIME_RDF_XML;
	}
	private String getPOSTMimeType() {
		return KtbsConstants.MIME_TURTLE;
	}

	@Override
	public KtbsResponse get(String uri) {
		checkStarted(); 

		HttpGet get = new HttpGet(uri);
		get.addHeader(HttpHeaders.ACCEPT, getGETMimeType());

		HttpResponse response = null;
		IKtbsResource ktbsResource = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		String body = null;
		try {
			response = httpClient.execute(get);
			HttpEntity entity;
			
			if(response == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+uri+"\".");
				ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
			} else {

				entity = response.getEntity();
				if(entity == null) {
					log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+uri+"\".");
					ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
				} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					body = EntityUtils.toString(response.getEntity());
					if(log.isDebugEnabled()) {
						log.debug("Response header:" + response.getStatusLine());
						log.debug("Response body:\n" + body);
					}

					ktbsResponseStatus=KtbsResponseStatus.RESOURCE_RETRIEVED;
				} else 
					ktbsResponseStatus=KtbsResponseStatus.REQUEST_FAILED;

				// Release the memory from the entity
				EntityUtils.consume(entity);
			}
		} catch (ClientProtocolException e) {
			log.warn("A HTTP exception occurred when getting the resource \""+uri+"\" from the KTBS server.", e);

			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R; 
		} catch (IOException e) {
			log.warn("Failed to read the returned content stream", e);

			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R; 
		} 

		return new KtbsResponseImpl(
				ktbsResource,
				body,
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_RETRIEVED, 
				ktbsResponseStatus, 
				response);
	}



	@Override
	public KtbsResponse post(IKtbsResource resource) {
		checkStarted(); 
		
		String postURI = resource.getUri();
		
		HttpPost post = new HttpPost(postURI);
		post.addHeader(HttpHeaders.CONTENT_TYPE, getPOSTMimeType());

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
			StringWriter writer = new StringWriter();
			new RdfSerializer().serializeResource(writer, resource, getPOSTMimeType());
			String string = writer.toString();
			
			log.info("POST Request content: \n" + string);
			post.setEntity(new StringEntity(string, HTTP.UTF_8));

		} catch (UnsupportedEncodingException e) {
			log.warn("Cannot decode the content of the response sent by the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		}

		String body = null;
		
		try {
			log.info("Sending POST request to the KTBS: "+post.getRequestLine());
			response = httpClient.execute(post);
			if(response == null)
				ktbsResponseStatus=KtbsResponseStatus.CLIENT_ERR0R;
			else {
				ktbsResponseStatus = response.getStatusLine().getStatusCode()==HttpStatus.SC_CREATED ?
						KtbsResponseStatus.RESOURCE_CREATED:
							KtbsResponseStatus.REQUEST_FAILED;
			}

			body = EntityUtils.toString(response.getEntity());
			if(log.isDebugEnabled()) {
				log.debug("Response header:" + response.getStatusLine());
				log.debug("Response body:\n" + body);
			}
		} catch (ClientProtocolException e) {
			log.error("An error occured when communicating with the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		} catch (IOException e) {
			log.error("An exception occurred when reading the content of the HTTP response", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		}

		return new KtbsResponseImpl(
				null, 
				body,
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_CREATED, 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public KtbsResponse delete(String resourceURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	public KtbsResponse update(IKtbsResource resource, String etag) {
		final String putURI = resource.getUri();

		checkStarted(); 

		HttpPut put = new HttpPut(putURI);
		put.addHeader(HttpHeaders.CONTENT_TYPE, getPOSTMimeType());
		put.addHeader(HttpHeaders.IF_MATCH, etag);

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;
		String body = null;

		try {
			
			StringWriter writer = new StringWriter();
			new RdfSerializer().serializeResource(writer, resource, getPOSTMimeType());
			String string = writer.toString();
			
			put.setEntity(new StringEntity(string, HTTP.UTF_8));

		
			log.info("Sending PUT request to the KTBS: "+put.getRequestLine());
			if(log.isDebugEnabled()) {
				log.debug("PUT body: "+string);
			}

			response = httpClient.execute(put);
			if(response == null)
				ktbsResponseStatus=KtbsResponseStatus.CLIENT_ERR0R;
			else {
				int sc = response.getStatusLine().getStatusCode();
				ktbsResponseStatus = sc==HttpStatus.SC_CREATED ?
						KtbsResponseStatus.RESOURCE_CREATED:
							((sc==HttpStatus.SC_OK ||sc==HttpStatus.SC_NO_CONTENT)?
									KtbsResponseStatus.RESOURCE_UPDATED:
										KtbsResponseStatus.REQUEST_FAILED);
			}

			body = EntityUtils.toString(response.getEntity());
			
			if(log.isDebugEnabled()) {
				log.debug("Response header:" + response.getStatusLine());
				log.debug("Response body:\n" + body);
			}
				/*
				 * Cannot read the resource returned since it is written in POST syntax (i.e. turtle) and there 
				 * is a bug reading resource in turtle send by the server.
				 */
				EntityUtils.consume(response.getEntity());
		} catch (ClientProtocolException e) {
			log.error("An error occured when communicating with the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		} catch (UnsupportedEncodingException e) {
			log.warn("Cannot decode the content of the response sent by the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		} catch (IOException e) {
			log.error("An exception occurred when reading the content of the HTTP response", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		}

		return new KtbsResponseImpl(
				null, 
				body, 
				(ktbsResponseStatus==KtbsResponseStatus.RESOURCE_UPDATED || ktbsResponseStatus==KtbsResponseStatus.RESOURCE_CREATED), 
				ktbsResponseStatus, 
				response);
	}

}
