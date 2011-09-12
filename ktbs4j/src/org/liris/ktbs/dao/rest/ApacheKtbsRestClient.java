package org.liris.ktbs.dao.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApacheKtbsRestClient implements KtbsRestClient {

	public static final String MESSAGE_DELETE_NOT_SUPPORTED = "DELETE method are not supported by the KTBS server in the current version";
	private static final String MESSAGE_CLIENT_NOT_STARTED = "The HTTP client is not started. Please call KtbsClient.startSession() before calling any KTBS remote service.";

	private static Logger log = LoggerFactory.getLogger(ApacheKtbsRestClient.class);

	private static Map<String, KtbsResponse> uriCacheEtagBody = new HashMap<String, KtbsResponse>();
	
	private String rootUri;

	public ApacheKtbsRestClient(String rootUri) {
		super();
		this.rootUri = rootUri;
	}

	private CredentialsProvider credentialsProvider;
	private HttpClient httpClient;
	private boolean started = false;

	private HttpParams httpParams;

	/**
	 * Starts the underlying HTTP client with default parameters for caching 
	 * and general HTTP protocol matters, and loads Jena classes.
	 */
	@Override
	public void startSession() {

		log.info("Starting KtbsClient session for the KTBS root URI \""+rootUri+"\".");


		CacheConfig cacheConfig = new CacheConfig();  
		cacheConfig.setMaxCacheEntries(1000);
		cacheConfig.setMaxObjectSizeBytes(10000);

		httpParams = new BasicHttpParams();

		/*
		 * In order to always being aware of what resource was requested from the uri
		 */
		httpParams.setParameter(ClientPNames.HANDLE_REDIRECTS, false);

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
		this.credentialsProvider = defaultHttpClient.getCredentialsProvider();

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

	@Override
	protected void finalize() throws Throwable {
		try {
			endSession();
		} finally {
			super.finalize();
		}
	}

	private void checkStarted() {
		if(!isStarted()) {
			String message = MESSAGE_CLIENT_NOT_STARTED;
			IllegalStateException illegalStateException = new IllegalStateException(message);
			log.error(message, illegalStateException);
			throw illegalStateException;
		}
	}

	@Override
	public synchronized KtbsResponse get(String uri, String mimeType) {
		checkStarted(); 

		HttpGet get = new HttpGet(uri);
		get.addHeader(HttpHeaders.ACCEPT,mimeType);
		
		KtbsResponse responseCached = getUriCacheEtagBody().get(uri);
		if(responseCached != null){
		    if(responseCached.getHTTPETag() != null){
			get.addHeader(HttpHeaders.IF_NONE_MATCH,responseCached.getHTTPETag());			
		    }
		}
		
		
		

		HttpResponse response = null;
		IKtbsResource ktbsResource = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		String body = null;
		boolean mustCache = false;
		try {
			response = httpClient.execute(get);
			HttpEntity entity;
			
			
			if(response == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+uri+"\".");
				ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
			} else {

				entity = response.getEntity();
				
				
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_SEE_OTHER) {
					// redirect to the right URL
					if(entity != null){
					    EntityUtils.consume(entity);
					}
					String location = response.getHeaders(HttpHeaders.LOCATION)[0].getValue();
					KtbsResponse ktbsResponse = get(location, mimeType);
					return ktbsResponse;
				} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
					// use body from cache
				    	if(entity != null){
					    EntityUtils.consume(entity);
					}
					return responseCached;
				}else if(entity == null) {
					log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+uri+"\".");
					ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
				} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					body = EntityUtils.toString(response.getEntity(), "UTF-8");
					Header[] headers = response.getHeaders(HttpHeaders.ETAG);
					if( headers != null && headers.length > 0){
					    if(headers.length > 1){
						log.error("Multiple etag not implemented yet");
					    }else{
						mustCache = true;
					    }
					}
					
					
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
		
		KtbsResponseImpl ktbsResponseImpl =  new KtbsResponseImpl(
				ktbsResource,
				uri,
				body,
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_RETRIEVED, 
				ktbsResponseStatus, 
				response);
		if(mustCache){
		    getUriCacheEtagBody().put(uri, ktbsResponseImpl);
		}
		return ktbsResponseImpl;
		
	}



	@Override
	public synchronized KtbsResponse post(String uri, String resourceAsString, String mimeType) {
		checkStarted(); 

		HttpPost post = new HttpPost(uri);
		post.addHeader(HttpHeaders.CONTENT_TYPE, mimeType);

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {

			log.debug("POST Request content: \n" + resourceAsString);
			post.setEntity(new StringEntity(resourceAsString, HTTP.UTF_8));

		} catch (UnsupportedEncodingException e) {
			log.warn("Cannot decode the content of the response sent by the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		}

		String body = null;

		try {
			log.debug("Sending POST request to the KTBS: "+post.getRequestLine());
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
			EntityUtils.consume(response.getEntity());
		} catch (ClientProtocolException e) {
			log.error("An error occured when communicating with the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		} catch (IOException e) {
			log.error("An exception occurred when reading the content of the HTTP response", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		}

		return new KtbsResponseImpl(
				null, 
				uri,
				body,
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_CREATED, 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public synchronized KtbsResponse delete(String resourceURI) {
		checkStarted(); 

		HttpDelete delete = new HttpDelete(resourceURI);

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		String body = null;

		try {
			log.debug("Sending DELETE request to the KTBS: " + delete.getRequestLine());
			response = httpClient.execute(delete);
			if(response == null)
				ktbsResponseStatus=KtbsResponseStatus.CLIENT_ERR0R;
			else {
				int statusCode = response.getStatusLine().getStatusCode();
				ktbsResponseStatus = statusCode==HttpStatus.SC_NO_CONTENT ?
						KtbsResponseStatus.RESOURCE_DELETED:
							KtbsResponseStatus.REQUEST_FAILED;
			}

			if(ktbsResponseStatus != KtbsResponseStatus.RESOURCE_DELETED)
				// a content may exists
				body = EntityUtils.toString(response.getEntity());
			if(log.isDebugEnabled()) {
				log.debug("Response header:" + response.getStatusLine());
				log.debug("Response body:\n" + body);
			}
			EntityUtils.consume(response.getEntity());
		} catch (ClientProtocolException e) {
			log.error("An error occured when communicating with the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		} catch (IOException e) {
			log.error("An exception occurred when reading the content of the HTTP response", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		}

		return new KtbsResponseImpl(
				null,
				resourceURI,
				body,
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_DELETED, 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public synchronized KtbsResponse update(String updateUri, String resourceAsString, String mimeType, String etag) {

		checkStarted(); 

		HttpPut put = new HttpPut(updateUri);
		put.addHeader(HttpHeaders.CONTENT_TYPE, mimeType);
		put.addHeader(HttpHeaders.IF_MATCH, etag);

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;
		String body = null;

		try {


			put.setEntity(new StringEntity(resourceAsString, HTTP.UTF_8));


			log.debug("Sending PUT request to the KTBS: "+put.getRequestLine());
			if(log.isDebugEnabled()) {
				log.debug("PUT body: "+resourceAsString);
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
				updateUri,
				body, 
				(ktbsResponseStatus==KtbsResponseStatus.RESOURCE_UPDATED || ktbsResponseStatus==KtbsResponseStatus.RESOURCE_CREATED), 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public void setCredentials(String username, String password) {
		if(username != null && credentialsProvider != null) {
			URI asJavaUri = URI.create(rootUri);
			log.info("Sarting Http authentication");
			HttpHost targetHost = new HttpHost(
					asJavaUri.getHost(), 
					asJavaUri.getPort()==-1?80:asJavaUri.getPort(),
							asJavaUri.getScheme()); 

			credentialsProvider.setCredentials(
					new AuthScope(targetHost.getHostName(), targetHost.getPort()), 
					new UsernamePasswordCredentials(username, password)
			);
		}
	}

	public static void setUriCacheEtagBody(Map<String, KtbsResponse> uriCacheEtagBody) {
	    ApacheKtbsRestClient.uriCacheEtagBody = uriCacheEtagBody;
	}

	public static Map<String, KtbsResponse> getUriCacheEtagBody() {
	    return uriCacheEtagBody;
	}

}
