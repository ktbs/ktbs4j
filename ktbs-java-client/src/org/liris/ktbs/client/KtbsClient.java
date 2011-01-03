package org.liris.ktbs.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
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
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Relation;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.impl.KtbsResourceFactory;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.KtbsConstants;
import org.liris.ktbs.rdf.KtbsResourceReader;
import org.liris.ktbs.rdf.RDFResourceBuilder;


/**
 * An implementation of {@link KtbsClientService} that use the Apache HTTP client to communicate
 * with the KTBS server.
 * 
 * <p/>
 * 
 * 
 * 
 * @author Damien Cram
 * @see KtbsClientService
 */
public class KtbsClient implements KtbsClientService {

	public static final String MESSAGE_DELETE_NOT_SUPPORTED = "DELETE method are not supported by the KTBS server in the current version";
	private static final String MESSAGE_CLIENT_NOT_STARTED = "The HTTP client is not started. Please call KtbsClient.startSession() before calling any KTBS remote service.";
	private static final String GET_MESSAGE_NOT_THE_RIGHT_KTBSROOT_URI(String expectedURI, String actualURI) {
		return "This KTBS client is bound to the uri \""+expectedURI+"\". Requests on \""+actualURI+"\" not allowed.";
	}

	private static Log log = LogFactory.getLog(KtbsClient.class);


	private String ktbsRootURI;

	// instanciated from KTBSClientApplication only
	KtbsClient(String ktbsRootURI) {
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

		/*
		 * Ensure that Jena classes required for model creation are loaded now, so that
		 * there is no need to do it in a middle of a REST request (loading Jena classes
		 * introduces a small delay of a half-second).
		 */
		log.debug("Loading Jena classes.");
		KtbsResourceReader.loadJenaClasses();
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

	/**
	 * Adds the parameter obsel one by one to the KTBS, since the current implementation 
	 * of KTBS does not seem to support grouped resource posting for the moment.
	 * <p>
	 * This implementation will automatically retry to create the obsels when the server returns 
	 * "400 URI Already in use", with a PUT request, instead of a POST request.
	 * </p>
	 */
	@Override
	public KtbsResponse[] addObselsToTrace(String traceURI, Collection<Obsel> obsels) {
		String requestURI = checkAndNormalizeURI(traceURI);

		log.warn("The service addObselsToTrace/2 will perform one POST request per obsel, intead of sending one single request for all obsels. To be optimized soon...");

		KtbsResponse[] response = new KtbsResponse[obsels.size()];
		int k = 0;
		for(Obsel obsel:obsels) {
			RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(getPOSTSyntax());
			builder.addObsels(requestURI, false, obsel);
			String stringRepresentation = builder.getRDFResourceAsString();
			log.debug("Sending the obsel \""+obsel.getTypeURI()+"\" to the KTBS.");
			response[k] = performPostRequest(requestURI, stringRepresentation);
			k++;
		}

		return response;
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
	public KtbsResponse getKtbsRoot() {
		return performGetRequest(ktbsRootURI, KtbsRoot.class, null);
	}

	private KtbsResponse performDeleteRequest(String ktbsResourceURI) {
		checkStarted(); 

		HttpDelete delete = new HttpDelete(ktbsResourceURI);
		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;
		try {
			response = httpClient.execute(delete);
		} catch (ClientProtocolException e) {
			log.warn("HTTP error when trying to delete the resource \""+ktbsResourceURI+"\".",e);
		} catch (IOException e) {
			log.warn("HTTP error when trying to delete the resource \""+ktbsResourceURI+"\".",e);
		}
		return new KtbsResponseImpl(
				null, 
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_DELETED, 
				ktbsResponseStatus, 
				response);
	}


	private void checkStarted() {
		if(!isStarted()) {
			String message = MESSAGE_CLIENT_NOT_STARTED;
			IllegalStateException illegalStateException = new IllegalStateException(message);
			log.error(message, illegalStateException);
			throw illegalStateException;
		}
	}

	private KtbsResponse performGetRequest(String ktbsResourceURI, Class<?> clazz, String restAspect) {
		checkStarted(); 

		HttpGet get = new HttpGet(ktbsResourceURI);
		get.addHeader(HttpHeaders.ACCEPT, getGETMimeType());

		HttpResponse response = null;
		KtbsResource ktbsResource = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
			response = httpClient.execute(get);
			HttpEntity entity;

			if(response == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+ktbsResourceURI+"\".");
				ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
			} else {

				entity = response.getEntity();
				if(entity == null) {
					log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+ktbsResourceURI+"\".");
					ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
				} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

					KtbsResourceReader reader = new KtbsResourceReader();

					if(entity.getContentLength()>0) {
						ktbsResource = reader.deserializeFromStream(
								ktbsResourceURI,
								entity.getContent(), 
								getGETSyntax(), 
								clazz,
								restAspect);
						
					}

					
					if(log.isDebugEnabled()) {
						InputStream contentStream = response.getEntity().getContent();
						String body = IOUtils.toString(contentStream);
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
			log.warn("A HTTP exception occurred when getting the resource \""+ktbsResourceURI+"\" from the KTBS server.", e);

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


	private KtbsResponse performPostRequest(String parentURI,
			String stringRepresentation) {
		checkStarted(); 

		HttpPost post = new HttpPost(parentURI);
		post.addHeader(HttpHeaders.CONTENT_TYPE, getPOSTMimeType());

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
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
				InputStream contentStream = response.getEntity().getContent();
				String body = IOUtils.toString(contentStream);
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
	public KtbsResponse createBase(String baseLocalName, String label) {

		Base b = KtbsResourceFactory.createBase(baseLocalName, ktbsRootURI, label);

		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(getPOSTSyntax());
		builder.addBase(b);


		String stringRepresentation = builder.getRDFResourceAsString(ktbsRootURI);

		/*
		 * !!! Temporary fix until the KTBS supports absolute URIs in POST request
		 *  TODO remove the following lines when Jena supports base URIs
		 */
		String stringRepresentationFixed = stringRepresentation.replaceAll(ktbsRootURI, "");

		return performPostRequest(ktbsRootURI, stringRepresentationFixed);
	}

	@Override
	public KtbsResponse getBase(String baseLocalName) {
		return getBaseFromURI(ktbsRootURI+baseLocalName+"/");
	}

	@Override
	public KtbsResponse getBaseFromURI(String baseURI) {
		String requestURI = checkAndNormalizeURI(baseURI);
		return performGetRequest(requestURI, Base.class, null);
	}

	@Override
	public KtbsResponse createTrace(String baseURI, String traceLocalName, String traceModelURI, Date origin, String label) {
		String baseNormalizedURI = checkAndNormalizeURI(baseURI);
		Base base = KtbsResourceFactory.createBase(baseNormalizedURI, null);
		String traceURI = baseNormalizedURI+traceLocalName+"/";
		String traceNormalizedURI = checkAndNormalizeURI(traceURI);
		Trace trace = KtbsResourceFactory.createTrace(traceNormalizedURI, traceModelURI, label, origin, base, false);

		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(getPOSTSyntax());
		builder.addTrace(trace, true, false);
		String stringRepresentation = builder.getRDFResourceAsString();

		return performPostRequest(baseNormalizedURI, stringRepresentation);
	}

	@Override
	public KtbsResponse getTraceObsels(String traceURI) {
		String normalizedURI = checkAndNormalizeURI(traceURI);
		String uri = withResourceAspect(normalizedURI,KtbsConstants.OBSELS_ASPECT,KtbsConstants.ABOUT_ASPECT);

		return performGetRequest(uri, Trace.class, KtbsConstants.OBSELS_ASPECT);
	}

	private String withResourceAspect(String resourceURI,
			String aspect, String... alternatives) {

		if(aspect == null) {
			String uri = resourceURI;
			for(String alt:alternatives) {
				if(uri.endsWith(alt)) {
					uri = uri.replaceAll(alt, "");
					return uri;
				}
			}
			return uri;
		}
		
		if(resourceURI.endsWith(aspect))
			return resourceURI;

		for(String alt:alternatives) {
			if(resourceURI.endsWith(alt)) {
				String newURI = resourceURI.replaceAll(alt, aspect);
				return newURI;
			}
		}

		return resourceURI+aspect;
	}

	/*
	 * Checks that the parameter uri is a URI with the KTBS root of this KTBS client
	 * and returns a normalized URI.
	 */
	private String checkAndNormalizeURI(String uncheckedURI) {
		if(uncheckedURI== null || !uncheckedURI.startsWith(ktbsRootURI)) {
			String message = GET_MESSAGE_NOT_THE_RIGHT_KTBSROOT_URI(ktbsRootURI, uncheckedURI);
			IllegalStateException illegalStateException = new IllegalStateException(message);
			log.error(message, illegalStateException);
			throw illegalStateException;
		}

		try {
			URI uri = new URI(uncheckedURI);
			return uri.normalize().toString();
		} catch (URISyntaxException e) {
			log.error("Wrong URI.",e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public KtbsResponse getTraceInfo(String traceURI) {
		String requestURI = checkAndNormalizeURI(traceURI);
		String uri = withResourceAspect(requestURI, KtbsConstants.ABOUT_ASPECT, KtbsConstants.OBSELS_ASPECT);

		return performGetRequest(uri, Trace.class, KtbsConstants.ABOUT_ASPECT);
	}

	@Override
	public KtbsResponse getTraceInfo(String baseLocalName, String traceLocalName) {
		return getTraceInfo(ktbsRootURI+baseLocalName+"/"+traceLocalName+"/");
	}

	@Override
	public KtbsResponse deleteBase(String baseLocalName) {
		throw new UnsupportedOperationException(MESSAGE_DELETE_NOT_SUPPORTED);
	}

	@Override
	public KtbsResponse deleteBaseFromURI(String baseURI) {
		String requestURI = checkAndNormalizeURI(baseURI);
		throw new UnsupportedOperationException(MESSAGE_DELETE_NOT_SUPPORTED);
	}

	@Override
	public KtbsResponse deleteTrace(String baseURI, String baseLocalName) {
		String requestURI = checkAndNormalizeURI(baseURI);
		throw new UnsupportedOperationException(MESSAGE_DELETE_NOT_SUPPORTED);
	}

	@Override
	public KtbsResponse deleteTrace(String traceURI) {
		String requestURI = checkAndNormalizeURI(traceURI);
		throw new UnsupportedOperationException(MESSAGE_DELETE_NOT_SUPPORTED);
	}

	@Override
	public KtbsResponse getObsel(String traceURI, String obselLocalName) {
		String requestURI = checkAndNormalizeURI(traceURI+ obselLocalName);
		return getObsel(requestURI);
	}

	@Override
	public KtbsResponse getObsel(String baseLocalName, String traceLocalName, String obselLocalName) {
		return getObsel(ktbsRootURI+baseLocalName+"/"+ traceLocalName+"/",obselLocalName);
	}

	@Override
	public KtbsResponse getObsel(String obselURI) {
		String requestURI = checkAndNormalizeURI(obselURI);
		return performGetRequest(requestURI, Obsel.class, null);
	}

	@Override
	public KtbsResponse getTraceObsels(String baseURI, String traceLocalName) {
		String requestURI = checkAndNormalizeURI(baseURI+traceLocalName+"/");
		return getTraceObsels(requestURI);
	}

	@Override
	public KtbsResponse getTraceObsels(String rootURI, String baseLocalName,
			String traceLocalName) {
		String requestURI = checkAndNormalizeURI(rootURI+baseLocalName+"/");

		return getTraceObsels(requestURI, traceLocalName);
	}

	@Override
	public KtbsResponse createObsel(String traceURI, String obselLocalName, String subject,
			String label, String typeURI, Date beginDT, Date endDT,
			long begin, long end,Map<String, Object> attributes,
			String... outgoingRelations) {
		String traceNormalizedURI = checkAndNormalizeURI(traceURI);
		String traceURIWithoutAspect = withResourceAspect(traceNormalizedURI, null, KtbsConstants.ABOUT_ASPECT, KtbsConstants.OBSELS_ASPECT);
		String traceURIWithObselsAspect = withResourceAspect(traceNormalizedURI, KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT);

		Obsel obsel = KtbsResourceFactory.createObsel(
				traceURIWithoutAspect+obselLocalName, 
				traceURIWithoutAspect, 
				subject,
				beginDT, 
				endDT, 
				begin, 
				end, 
				typeURI, 
				attributes,
				label);
		if(outgoingRelations != null && outgoingRelations.length%2==0) {
			for(int k=0; k<outgoingRelations.length;k+=2) {
				Relation rel = KtbsResourceFactory.createRelation(
						obsel.getURI(), 
						outgoingRelations[k], 
						outgoingRelations[k+1]);
				obsel.addOutgoingRelation(
						rel
				);
			}
		}

		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(getPOSTSyntax());
		builder.addObsels(traceURIWithoutAspect, true, obsel);
		String stringRepresentation = builder.getRDFResourceAsString();
		return performPostRequest(traceURIWithoutAspect, stringRepresentation);
	}

	@Override
	public KtbsResponse createTraceModel(String baseLocalName,
			String traceModelLocalName, String label) {

		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(getPOSTSyntax());
		String baseNormalizedURI = checkAndNormalizeURI(ktbsRootURI+baseLocalName+"/");
		String traceModelNormalizedURI = checkAndNormalizeURI(baseNormalizedURI + traceModelLocalName + "/");
		builder.addTraceModel(baseNormalizedURI, traceModelNormalizedURI, label);
		String stringRepresentation = builder.getRDFResourceAsString();
		log.debug("String representation of trace model: " + stringRepresentation);

		return performPostRequest(baseNormalizedURI, stringRepresentation); 
	}

	private KtbsResponse performPutRequest(String parentURI,
			String stringRepresentation, String eTag) {
		checkStarted(); 

		HttpPut put = new HttpPut(parentURI);
		put.addHeader(HttpHeaders.CONTENT_TYPE, getPOSTMimeType());
		put.addHeader(HttpHeaders.IF_MATCH, eTag);

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
			put.setEntity(new StringEntity(stringRepresentation, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			log.warn("Cannot decode the content of the response sent by the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
		}

		try {
			log.info("Sending PUT request to the KTBS: "+put.getRequestLine());
			response = httpClient.execute(put);
			if(response == null)
				ktbsResponseStatus=KtbsResponseStatus.INTERNAL_ERR0R;
			else {
				int sc = response.getStatusLine().getStatusCode();
				ktbsResponseStatus = sc==HttpStatus.SC_CREATED ?
						KtbsResponseStatus.RESOURCE_CREATED:
							((sc==HttpStatus.SC_OK ||sc==HttpStatus.SC_NO_CONTENT)?
									KtbsResponseStatus.RESOURCE_MODIFIED:
										KtbsResponseStatus.SERVER_EXCEPTION);
			}
			

			if(log.isDebugEnabled()) {
				InputStream contentStream = response.getEntity().getContent();
				String body = IOUtils.toString(contentStream);
				log.debug("Response header:" + response.getStatusLine());
				log.debug("Response body:\n" + body);
			}
			if(ktbsResponseStatus == KtbsResponseStatus.RESOURCE_MODIFIED) {
				
				/*
				 * Cannot read the resource returned since it is written in POST syntax (i.e. turtle) and there 
				 * is a bug reading resource in turtle send by the server.
				 */
//				resource = new KtbsResourceReader().deserializeFromStream(ktbsResourceURI, response.getEntity(), getPOSTSyntax(), ktbsResourceType, restAspect)
				EntityUtils.consume(response.getEntity());
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
				(ktbsResponseStatus==KtbsResponseStatus.RESOURCE_CREATED)||(ktbsResponseStatus==KtbsResponseStatus.RESOURCE_MODIFIED), 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public KtbsResponse putTraceObsels(String traceURI, Collection<Obsel> obsels, String traceETag) {
		String normalizedURI = checkAndNormalizeURI(traceURI);
		String uriWithAspect = withResourceAspect(normalizedURI, KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT);
		String uriWithoutAspect = withResourceAspect(normalizedURI, null, KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT);

		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(getPOSTSyntax());
		builder.addObsels(uriWithoutAspect, true, obsels.toArray(new Obsel[obsels.size()]));
		String stringRepresentation = builder.getRDFResourceAsString();

		return performPutRequest(uriWithAspect, stringRepresentation, traceETag);
	}

	/*
	 * The following service needs to send the complete list of rdf statement about the trace.
	 * This cannot be done properly without performing a GET on trace@about beforehand.
	 * 
	 * The service will be implemented when it is possible to amend trace informations by sending
	 * only the RDF triple with the property to modify.
	 * 
	 * (non-Javadoc)
	 * @see org.liris.ktbs.client.KtbsClientService#putTraceInfo(org.liris.ktbs.core.Trace, java.lang.String)
	 */
	@Override
	public KtbsResponse putTraceInfo(Trace trace, String traceETag) {
		throw new UnsupportedOperationException("This operation cannot be performed properly right now, due to a lack of KTBS specification on modifying trace infos.");
		//
		//					String traceURI = trace.getURI();
		//					String uriWithAspect = new String(traceURI);
		//					String uriWithoutAspect = new String(traceURI);
		//					if(traceURI.endsWith(KtbsConstants.OBSELS_ASPECT))
		//						uriWithoutAspect=uriWithoutAspect.replaceAll(KtbsConstants.OBSELS_ASPECT, "");
		//					else
		//						uriWithAspect+=KtbsConstants.OBSELS_ASPECT;
		//			
		//					RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(getPOSTSyntax());
		//					builder.addTrace(trace, false, false);
		//					String stringRepresentation = builder.getRDFResourceAsString();
		//					return performPutRequest(uriWithAspect, stringRepresentation, traceETag);
	}


	@Override
	public KtbsResponse createKtbsResource(String resourceURI, Reader reader) {
		String requestURI = checkAndNormalizeURI(resourceURI);

		StringWriter writer = new StringWriter();
		int c;
		try {
			while((c=reader.read())!=-1)
				writer.write(c);
		} catch (IOException e) {
			log.warn("Cannot read in the parameter reader", e);
		}

		String stringRepresentation = writer.getBuffer().toString();
		return performPostRequest(requestURI, stringRepresentation);
	}


	@Override
	public KtbsResponse createKtbsResource(String resourceURI,
			InputStream stream) {
		String requestURI = checkAndNormalizeURI(resourceURI);

		return createKtbsResource(requestURI, new InputStreamReader(stream));
	}

	@Override
	public KtbsResponse putKtbsResource(String resourceURI, Reader reader, String etag) {
		
		String requestURI = checkAndNormalizeURI(resourceURI);

		StringWriter writer = new StringWriter();
		int c;
		try {
			while((c=reader.read())!=-1)
				writer.write(c);
		} catch (IOException e) {
			log.warn("Cannot read in the parameter reader", e);
		}

		String stringRepresentation = writer.getBuffer().toString();
		return performPutRequest(requestURI, stringRepresentation, etag);
	}

	@Override
	public KtbsResponse putKtbsResource(String resourceURI, InputStream stream, String etag) {
		return putKtbsResource(resourceURI, new InputStreamReader(stream), etag);
	}

	@Override
	public KtbsResponse getKtbsResource(String resourceURI, Class<?> clazz) {
		String requestURI = checkAndNormalizeURI(resourceURI);
		String restAspect = null;
		if(Trace.class.isAssignableFrom(clazz)) {
			if(resourceURI.endsWith(KtbsConstants.ABOUT_ASPECT))
				restAspect = KtbsConstants.ABOUT_ASPECT;
			else if(resourceURI.endsWith(KtbsConstants.OBSELS_ASPECT))
				restAspect = KtbsConstants.OBSELS_ASPECT;
			else {
				requestURI = withResourceAspect(requestURI, KtbsConstants.ABOUT_ASPECT, KtbsConstants.OBSELS_ASPECT);
				restAspect = KtbsConstants.ABOUT_ASPECT;
			}
		}
		return performGetRequest(requestURI, clazz, restAspect);
	}

	@Override
	public KtbsResponse getETag(String resourceURI) {
		checkStarted(); 

		HttpGet get = new HttpGet(resourceURI);

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
			response = httpClient.execute(get);

			if(response == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+resourceURI+"\".");
				ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
			} else {

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

					if(log.isDebugEnabled()) {
						log.debug("Response header:" + response.getStatusLine());
					}

					ktbsResponseStatus=KtbsResponseStatus.RESOURCE_RETRIEVED;
				} else 
					ktbsResponseStatus=KtbsResponseStatus.SERVER_EXCEPTION;
			}
		} catch (ClientProtocolException e) {
			log.warn("A HTTP exception occurred when getting the resource \""+resourceURI+"\" from the KTBS server.", e);

			ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R; 
		} catch (IOException e) {
			log.warn("A HTTP exception occurred when getting the resource \""+resourceURI+"\" from the KTBS server.", e);
			ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R; 
		}

		return new KtbsResponseImpl(
				null, 
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_RETRIEVED, 
				ktbsResponseStatus, 
				response);
	}
}
