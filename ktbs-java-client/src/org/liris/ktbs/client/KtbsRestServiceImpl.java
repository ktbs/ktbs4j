package org.liris.ktbs.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.JenaConstants;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.StringableResource;
import org.liris.ktbs.utils.KtbsUtils;

public class KtbsRestServiceImpl implements KtbsRestService {

	public static final String MESSAGE_DELETE_NOT_SUPPORTED = "DELETE method are not supported by the KTBS server in the current version";
	private static final String MESSAGE_CLIENT_NOT_STARTED = "The HTTP client is not started. Please call KtbsClient.startSession() before calling any KTBS remote service.";
	private static final String GET_MESSAGE_NOT_THE_RIGHT_KTBSROOT_URI(String expectedURI, String actualURI) {
		return "This KTBS client is bound to the uri \""+expectedURI+"\". Requests on \""+actualURI+"\" not allowed.";
	}

	private static Log log = LogFactory.getLog(KtbsRestServiceImpl.class);


	private String ktbsRootURI;

	// instanciated from KTBSClientApplication only
	KtbsRestServiceImpl(String ktbsRootURI) {
		this.ktbsRootURI = ktbsRootURI;
	}

	private HttpClient httpClient;
	private boolean started = false;

	private HttpParams httpParams;


	/**
	 * Starts the underlying HTTP client with default parameters for caching 
	 * and general HTTP protocol matters, and loads Jena classes.
	 */
	public void startSession() {

		log.info("Starting KtbsClient session for the KTBS root URI \""+ktbsRootURI+"\".");

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
		final VersionInfo vi = VersionInfo.loadVersionInfo("org.apache.http.client", KtbsClientApplication.class.getClassLoader());
		final String release = (vi != null) ? vi.getRelease() : VersionInfo.UNAVAILABLE;
		HttpProtocolParams.setUserAgent(httpParams, "KTBS Client, based on Apache-HttpClient/" + release + " (java 1.5)");

		log.debug("Creating the caching HTTP client.");
		httpClient = new CachingHttpClient(new DefaultHttpClient(httpParams), cacheConfig);
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
	public void closeSession() {
		log.info("Closing KtbsClient session for the KTBS root URI \""+ktbsRootURI+"\".");
		if(httpClient != null) 
			httpClient.getConnectionManager().shutdown();
		this.started = false;
		log.info("Session closed.");
	}

	@Override
	protected void finalize() throws Throwable {
		closeSession();
		super.finalize();
	}

	private void checkStarted() {
		if(!isStarted()) {
			String message = MESSAGE_CLIENT_NOT_STARTED;
			IllegalStateException illegalStateException = new IllegalStateException(message);
			log.error(message, illegalStateException);
			throw illegalStateException;
		}
	}

	private String getGETSyntax() {
		return JenaConstants.JENA_SYNTAX_N_TRIPLES;
	}
	private String getPOSTSyntax() {
		return JenaConstants.JENA_SYNTAX_TURTLE;
	}
	private String getGETMimeType() {
		return KtbsConstants.MIME_NTRIPLES;
	}
	private String getPOSTMimeType() {
		return KtbsConstants.MIME_TURTLE;
	}

	@Override
	public KtbsResponse retrieve(String uri) {
		checkStarted(); 

		HttpGet get = new HttpGet(uri);
		get.addHeader(HttpHeaders.ACCEPT, getGETMimeType());

		HttpResponse response = null;
		KtbsResource ktbsResource = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
			response = httpClient.execute(get);
			HttpEntity entity;

			if(response == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+uri+"\".");
				ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
			} else {

				entity = response.getEntity();
				if(entity == null) {
					log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+uri+"\".");
					ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
				} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					if(log.isDebugEnabled()) {
						String body = EntityUtils.toString(response.getEntity());
						log.debug("Response header:" + response.getStatusLine());
						log.debug("Response body:\n" + body);
					}

					ktbsResponseStatus=KtbsResponseStatus.RESOURCE_RETRIEVED;
				} else 
					ktbsResponseStatus=KtbsResponseStatus.SERVER_EXCEPTION;

				// Release the memory from the entity
				EntityUtils.consume(entity);
			}
		} catch (ClientProtocolException e) {
			log.warn("A HTTP exception occurred when getting the resource \""+uri+"\" from the KTBS server.", e);

			ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R; 
		} catch (IOException e) {
			log.warn("Failed to read the returned content stream", e);

			ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R; 
		} 

		return new KtbsResponseImpl(
				ktbsResource, 
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_RETRIEVED, 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public KtbsResponse create(KtbsResource resource) {
		checkStarted(); 
		if (!(resource instanceof StringableResource)) 
			throw new NotAPostableResourceException(resource);

		String postURI = KtbsUtils.getParentResource(resource);
		HttpPost post = new HttpPost(postURI);
		post.addHeader(HttpHeaders.CONTENT_TYPE, getPOSTMimeType());

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
			String stringRepresentation = ((StringableResource)resource).toPostableString(getPOSTMimeType());
			
			log.info("POST Request content: \n" + stringRepresentation);
			post.setEntity(new StringEntity(stringRepresentation, HTTP.UTF_8));

		} catch (UnsupportedEncodingException e) {
			log.warn("Cannot decode the content of the response sent by the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
		}

		try {
			log.info("Sending POST request to the KTBS: "+post.getRequestLine());
			response = httpClient.execute(post);
			if(response == null)
				ktbsResponseStatus=KtbsResponseStatus.INTERNAL_ERR0R;
			else {
				ktbsResponseStatus = response.getStatusLine().getStatusCode()==HttpStatus.SC_CREATED ?
						KtbsResponseStatus.RESOURCE_CREATED:
							KtbsResponseStatus.SERVER_EXCEPTION;
			}

			if(log.isDebugEnabled()) {
				String body = EntityUtils.toString(response.getEntity());
				log.debug("Response header:" + response.getStatusLine());
				log.debug("Response body:\n" + body);
			}
		} catch (ClientProtocolException e) {
			log.error("An error occured when communicating with the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
		} catch (IOException e) {
			log.error("An exception occurred when reading the content of the HTTP response", e);
			ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
		}

		return new KtbsResponseImpl(
				null, 
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_CREATED, 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public KtbsResponse update(KtbsResource resource, String eTag) {
		
		return null;
	}

	@Override
	public KtbsResponse delete(String uri) {
		throw new UnsupportedOperationException("Not yet supported by the KTBS server");
	}
}
