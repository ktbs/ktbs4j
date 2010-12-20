package org.liris.ktbs.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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

	private static Log log = LogFactory.getLog(KtbsClient.class);

	// instanciated from KTBSClientApplication only
	KtbsClient() {
	}


	private HttpClient httpClient;
	private boolean started = false;

	public void startSession() {

		CacheConfig cacheConfig = new CacheConfig();  
		cacheConfig.setMaxCacheEntries(1000);
		cacheConfig.setMaxObjectSizeBytes(10000);

		// configure the HttpClient to handle automatically http redirections (303)
		HttpParams params = new BasicHttpParams();
		params.setParameter(ClientPNames.HANDLE_REDIRECTS, true);

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		// determine the release version from packaged version info
		final VersionInfo vi = VersionInfo.loadVersionInfo("org.apache.http.client", KTBSClientApplication.class.getClassLoader());
		final String release = (vi != null) ? vi.getRelease() : VersionInfo.UNAVAILABLE;
		HttpProtocolParams.setUserAgent(params, "KTBS Client, based on Apache-HttpClient/" + release + " (java 1.5)");

		httpClient = new CachingHttpClient(new DefaultHttpClient(params), cacheConfig);
		this.started = true;
	}


	public boolean isStarted() {
		return started;
	}

	public void closeSession() {
		if(httpClient != null) 
			httpClient.getConnectionManager().shutdown();
		this.started = false;
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
	public KtbsResponse addObselsToTrace(String traceURI, Collection<Obsel> obsels) {
		log.warn("The service addObselsToTrace/2 will perform one POST request per obsel, intead of sending one single request for all obsels. To be optimized soon...");

		KtbsResponse response = null;
		for(Obsel obsel:obsels) {
			RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(JenaConstants.JENA_SYNTAX_TURTLE);
			builder.addObsel(traceURI, obsels.toArray(new Obsel[obsels.size()]));
			String stringRepresentation = builder.getRDFResourceAsString();
			log.debug("Sending the obsel \""+obsel.getTypeURI()+"\" to the KTBS.");
			response = performPostRequest(traceURI, stringRepresentation);
			if(response.getHTTPResponse() != null && response.getHTTPResponse().getStatusLine().equals(HttpStatus.SC_BAD_REQUEST)) {
				/*
				 * 	The URI of this obsel may be already in use. Retry this with a put request.
				 * 
				 *  TODO redirect this to a PUT request.
				 */
			}
		}
		
		return response;
	}

	@Override
	public KtbsResponse createRelation(String traceURI, String relationName, String fromObselURI, String toObselURI) {
		/*
		 * TODO test this service implementation. This probably requires a PUT rather than a POST.
		 */
		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(JenaConstants.JENA_SYNTAX_TURTLE);
		builder.addRelation(fromObselURI, relationName, toObselURI);
		String stringRepresentation = builder.getRDFResourceAsString();
		return performPostRequest(traceURI, stringRepresentation);
	}

	@Override
	public KtbsResponse addAttributeToObsel(String obselURI, String attributeName,
			Serializable value) {
		//TODO put request
		return null;
	}

	@Override
	public KtbsResponse getKtbsRoot(String rootURI) {
		return performGetRequest(rootURI, KtbsRoot.class, null);
	}

	private KtbsResponse performGetRequest(String ktbsResourceURI, Class<?> clazz, String restAspect) {
		HttpGet get = new HttpGet(ktbsResourceURI);
		get.addHeader(HttpHeaders.ACCEPT, KtbsConstants.MIME_NTRIPLES);

		HttpResponse response = null;
		KtbsResource ktbsResource = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
			response = httpClient.execute(get);

			if(response == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+ktbsResourceURI+"\".");
				ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
			}

			HttpEntity entity = response.getEntity();
			if(entity == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+ktbsResourceURI+"\".");
				ktbsResponseStatus = KtbsResponseStatus.INTERNAL_ERR0R;
			} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

				KtbsResourceReader reader = new KtbsResourceReader();

				if(entity.getContentLength()>0) {
					ktbsResource = reader.deserializeFromStream(
							ktbsResourceURI,
							entity.getContent(), 
							JenaConstants.JENA_SYNTAX_N_TRIPLES, 
							clazz,
							restAspect);
				}

				// Release the memory from the entity
				EntityUtils.consume(entity);

				ktbsResponseStatus=KtbsResponseStatus.RESOURCE_RETRIEVED;
			} else 
				ktbsResponseStatus=KtbsResponseStatus.SERVER_EXCEPTION;

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
		HttpPost post = new HttpPost(parentURI);
		post.addHeader(HttpHeaders.CONTENT_TYPE, KtbsConstants.MIME_TURTLE);

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
	public KtbsResponse createBase(String rootURI, String baseLocalName, String label) {
		KtbsRoot createKtbsRoot = KtbsResourceFactory.createKtbsRoot(rootURI, label);
		Base b = KtbsResourceFactory.createBase(createKtbsRoot.getURI()+baseLocalName+"/", createKtbsRoot, label);

		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(JenaConstants.JENA_SYNTAX_TURTLE);
		builder.addBase(b);
		String stringRepresentation = builder.getRDFResourceAsString(rootURI);

		return performPostRequest(rootURI, stringRepresentation);
	}

	@Override
	public KtbsResponse getBase(String rootURI, String baseLocalName) {
		return getBase(rootURI+baseLocalName+"/");
	}

	@Override
	public KtbsResponse getBase(String baseURI) {
		return performGetRequest(baseURI, Base.class, null);
	}

	@Override
	public KtbsResponse createTrace(String baseURI, String traceLocalName, String traceModelURI, Date origin, String label) {
		Base base = KtbsResourceFactory.createBase(baseURI, null);
		Trace trace = KtbsResourceFactory.createTrace(base.getURI()+traceLocalName+"/", traceModelURI, label, origin, base);

		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(JenaConstants.JENA_SYNTAX_TURTLE);
		builder.addTrace(trace, true);
		String stringRepresentation = builder.getRDFResourceAsString();

		return performPostRequest(baseURI, stringRepresentation);
	}

	@Override
	public KtbsResponse getTraceObsels(String traceURI) {
		return performGetRequest(traceURI+KtbsConstants.OBSELS_ASPECT, Trace.class, KtbsConstants.OBSELS_ASPECT);
	}

	@Override
	public KtbsResponse getTraceInfo(String traceURI) {
		return performGetRequest(traceURI+KtbsConstants.ABOUT_ASPECT, Trace.class, KtbsConstants.ABOUT_ASPECT);
	}

	@Override
	public KtbsResponse deleteRelation(String traceURI, String relationName,
			String fromObselURI, String toObselURI) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public KtbsResponse deleteBase(String rootURI, String baseLocalName) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public KtbsResponse deleteBase(String rootURI, Base base) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public KtbsResponse deleteBase(String baseURI) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public KtbsResponse deleteTrace(String baseURI, String traceLocalName) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public KtbsResponse deleteTrace(String traceURI) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public KtbsResponse deleteObsel(String obselURI) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public KtbsResponse getObsel(String traceURI, String obselLocalName) {
		return getObsel(traceURI+obselLocalName+"/");
	}

	@Override
	public KtbsResponse getObsel(String obselURI) {
		return performGetRequest(obselURI, Obsel.class, null);
	}

	@Override
	public KtbsResponse getTraceInfo(String baseURI, String traceLocalName) {
		return getTraceInfo(baseURI+traceLocalName+"/");
	}

	@Override
	public KtbsResponse getTraceInfo(String rootURI, String baseLocalName,
			String traceLocalName) {
		return getTraceInfo(rootURI+baseLocalName+"/", traceLocalName);
	}

	@Override
	public KtbsResponse getTraceObsels(String baseURI, String traceLocalName) {
		return getTraceObsels(baseURI+traceLocalName+"/");
	}

	@Override
	public KtbsResponse getTraceObsels(String rootURI, String baseLocalName,
			String traceLocalName) {
		return getTraceObsels(rootURI+baseLocalName+"/", traceLocalName);
	}


	@Override
	public KtbsResponse createObsel(String traceURI, String obselLocalName, String subject,
			String label, String typeURI, Date begin, Date end,
			Map<String, Serializable> attributes,
			String... outgoingRelations) {

		Obsel obsel = KtbsResourceFactory.createObsel(
				traceURI+obselLocalName+"/", 
				traceURI, 
				subject,
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

		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(JenaConstants.JENA_SYNTAX_TURTLE);
		builder.addObsel(traceURI, obsel);
		String stringRepresentation = builder.getRDFResourceAsString();
		return performPostRequest(traceURI, stringRepresentation);
	}


	@Override
	public KtbsResponse createTraceModel(String baseURI,
			String traceModelLocalName, String label) {
		RDFResourceBuilder builder = RDFResourceBuilder.newBuilder(JenaConstants.JENA_SYNTAX_TURTLE);
		builder.addTraceModel(baseURI, baseURI+traceModelLocalName+"/", label);
		String stringRepresentation = builder.getRDFResourceAsString();
		log.debug("String representation of trace model: " + stringRepresentation);

		return performPostRequest(baseURI, stringRepresentation); 
	}
}
