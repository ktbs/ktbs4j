package org.liris.ktbs.client;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

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
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.JenaUtils;
import org.liris.ktbs.rdf.KtbsJenaResource;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class KtbsRestServiceImpl implements KtbsRestService {

	public static final String MESSAGE_DELETE_NOT_SUPPORTED = "DELETE method are not supported by the KTBS server in the current version";
	private static final String MESSAGE_CLIENT_NOT_STARTED = "The HTTP client is not started. Please call KtbsClient.startSession() before calling any KTBS remote service.";

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
				ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
			} else {

				entity = response.getEntity();
				if(entity == null) {
					log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+uri+"\".");
					ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
				} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					if(log.isDebugEnabled()) {
						String body = EntityUtils.toString(response.getEntity());
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
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_RETRIEVED, 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public KtbsResponse update(KtbsResource resource, String eTag, String... traceAspects) {

		final String postURI = resource.getURI();

		Model model = ((KtbsJenaResource)resource).getCopyOfModel();

		Selector selector;

		if (resource instanceof Trace) {
			if(traceAspects.length != 1)
				throw new IllegalArgumentException("There must be one resource aspect " +
				"specified for a trace.");
			String traceAspect = traceAspects[0];
			if(!traceAspect.equals(KtbsConstants.ABOUT_ASPECT) 
					|| !traceAspect.equals(KtbsConstants.OBSELS_ASPECT)) 
				throw new IllegalArgumentException("Unknown trace aspect: " + traceAspect);
			if(traceAspect.equals(KtbsConstants.ABOUT_ASPECT)) {
				// @about aspect
				selector = new SimpleSelector() {
					@Override
					public boolean selects(Statement s) {
						if(s.getSubject().getURI().equals(postURI))  
							return true;
						if(s.getSubject().getURI().equals(KtbsUtils.addAspect(postURI, KtbsConstants.ABOUT_ASPECT, KtbsConstants.OBSELS_ASPECT))) 
							return true;
						return false;
					}
				};

			} else {
				// @obsels aspect
				Iterator<Obsel> it = ((Trace)resource).listObsels();
				final Collection<String> obselURIs = new LinkedList<String>();
				while (it.hasNext()) 
					obselURIs.add(it.next().getURI());

				selector = new SimpleSelector() {
					@Override
					public boolean selects(Statement s) {
						if(obselURIs.contains(s.getSubject().getURI()))  
							return true;
						if(s.getObject().isResource() && 
								obselURIs.contains(s.getObject().asResource().getURI()))
							return true;
						return false;
					}
				};
			}
		} else if(resource instanceof TraceModel) {
			selector = new SimpleSelector() {
				@Override
				public boolean selects(Statement s) {
					return true;
				}
			};
		} else {
			if(traceAspects.length != 0)
				throw new IllegalArgumentException("There must be no resource aspect specified " +
						"for a resource of type " + resource.getClass().getCanonicalName());
			selector = new SimpleSelector() {
				@Override
				public boolean selects(Statement s) {
					if(postURI.equals(s.getSubject().getURI()))  
						return true;
					if(s.getObject().isResource() && 
							postURI.equals(s.getObject().asResource().getURI()))
						return true;
					return false;
				}
			};
		}

		Model filteredModel = JenaUtils.filterModel(model, selector);
		String stringRepresentation = writeToString(filteredModel);

		return updateResource(postURI,stringRepresentation, eTag);
	}

	private KtbsResponse updateResource(String postURI,
			String stringRepresentation,
			String eTag) {

		checkStarted(); 

		HttpPut put = new HttpPut(postURI);
		put.addHeader(HttpHeaders.CONTENT_TYPE, getPOSTMimeType());
		put.addHeader(HttpHeaders.IF_MATCH, eTag);

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {
			put.setEntity(new StringEntity(stringRepresentation, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			log.warn("Cannot decode the content of the response sent by the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		}

		try {
			log.info("Sending PUT request to the KTBS: "+put.getRequestLine());
			if(log.isDebugEnabled()) {
				log.debug("PUT body: "+stringRepresentation);
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

			if(log.isDebugEnabled()) {
				String body = EntityUtils.toString(response.getEntity());
				log.debug("Response header:" + response.getStatusLine());
				log.debug("Response body:\n" + body);
			}
			if(ktbsResponseStatus == KtbsResponseStatus.RESOURCE_UPDATED) {

				/*
				 * Cannot read the resource returned since it is written in POST syntax (i.e. turtle) and there 
				 * is a bug reading resource in turtle send by the server.
				 */
				EntityUtils.consume(response.getEntity());
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
				(ktbsResponseStatus==KtbsResponseStatus.RESOURCE_UPDATED || ktbsResponseStatus==KtbsResponseStatus.RESOURCE_CREATED), 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public KtbsResponse delete(String uri) {
		throw new UnsupportedOperationException("The service DELETE is not yet available.");
	}

	@Override
	public KtbsResponse createBase(String rootURI, String baseURI) {
		Model model = createModelWithType(baseURI, KtbsConstants.BASE);
		model.getResource(rootURI).addProperty(model.getProperty(KtbsConstants.P_HAS_BASE), model.getResource(baseURI));
		String asString = writeToString(model);
		return postResource(rootURI, asString);
	}

	private String writeToString(Model model) {
		model.setNsPrefix("ktbs",KtbsConstants.NAMESPACE);
		model.setNsPrefix("xsd",XSD.getURI());
		model.setNsPrefix("rdfs",RDFS.getURI());


		StringWriter writer = new StringWriter();

		model.write(writer, KtbsUtils.getJenaSyntax(getPOSTMimeType()), null);
		String asString = writer.toString();
		return asString;
	}

	private Model createModelWithType(String baseURI, String rdfType) {
		Model model = ModelFactory.createDefaultModel();
		model.getResource(baseURI).addProperty(
				RDF.type, 
				model.getProperty(rdfType));
		return model;
	}

	private KtbsResponse postResource(String postURI, String string) {
		checkStarted(); 

		HttpPost post = new HttpPost(postURI);
		post.addHeader(HttpHeaders.CONTENT_TYPE, getPOSTMimeType());

		HttpResponse response = null;
		KtbsResponseStatus ktbsResponseStatus = null;

		try {

			log.info("POST Request content: \n" + string);
			post.setEntity(new StringEntity(string, HTTP.UTF_8));

		} catch (UnsupportedEncodingException e) {
			log.warn("Cannot decode the content of the response sent by the KTBS", e);
			ktbsResponseStatus = KtbsResponseStatus.CLIENT_ERR0R;
		}

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

			if(log.isDebugEnabled()) {
				String body = EntityUtils.toString(response.getEntity());
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
				ktbsResponseStatus==KtbsResponseStatus.RESOURCE_CREATED, 
				ktbsResponseStatus, 
				response);
	}

	@Override
	public KtbsResponse createMethod(String baseURI, String methodURI, String inheritedMethodUri, Map<String,String> parameters) {
		Model model = createModelWithType(methodURI, KtbsConstants.METHOD);
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(methodURI));
		model.getResource(methodURI).addProperty(model.getProperty(KtbsConstants.P_INHERITS), model.getResource(inheritedMethodUri));
		if(parameters != null) {
			for(Entry<String, String> entry:parameters.entrySet())
				model.getResource(methodURI).addLiteral(model.getProperty(KtbsConstants.P_HAS_PARAMETER), entry.getKey()+"="+entry.getValue());
		}
		String asString = writeToString(model);
		return postResource(baseURI, asString);
	}

	@Override
	public KtbsResponse createTraceModel(String baseURI,String modelURI) {
		Model model = createModelWithType(modelURI, KtbsConstants.TRACE_MODEL);
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(modelURI));
		String asString = writeToString(model);
		return postResource(baseURI, asString);
	}

	@Override
	public KtbsResponse createStoredTrace(String baseURI, String traceURI, String modelURI, String origin) {
		Model model = createModelWithType(traceURI, KtbsConstants.STORED_TRACE);
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(traceURI));
		model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_MODEL), model.getResource(modelURI));
		model.getResource(traceURI).addLiteral(model.getProperty(KtbsConstants.P_HAS_ORIGIN), origin);
		String asString = writeToString(model);
		return postResource(baseURI, asString);
	}

	@Override
	public KtbsResponse createComputedTrace(String baseURI, String traceURI, 
			String methodURI, Collection<String> sourceTracesURIs) {
		Model model = createModelWithType(traceURI, KtbsConstants.COMPUTED_TRACE);
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(traceURI));
		model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_METHOD), model.getResource(methodURI));
		for(String sourceTraceURI:sourceTracesURIs) 
			model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_SOURCE), model.getResource(sourceTraceURI));

		String asString = writeToString(model);
		return postResource(baseURI, asString);
	}

	@Override
	public KtbsResponse createObsel(
			String traceURI, 
			String obselURI, 
			String typeURI, 
			String subject,
			String beginDT, 
			String endDT, 
			BigInteger begin, 
			BigInteger end,
			Map<String, Object> attributes) {
		String traceURIWithoutAspect = KtbsUtils.addAspect(traceURI, null, KtbsConstants.OBSELS_ASPECT, KtbsConstants.ABOUT_ASPECT);

		Model model = createModelWithType(obselURI, typeURI);
		Resource or = model.getResource(obselURI);
		or.addProperty(model.getProperty(KtbsConstants.P_HAS_TRACE), model.getResource(traceURIWithoutAspect));
		if(beginDT != null)
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_BEGIN_DT), model.createTypedLiteral(beginDT, XSDDatatype.XSDdateTime));
		if(endDT != null)
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_END_DT),  model.createTypedLiteral(endDT, XSDDatatype.XSDdateTime));
		if(begin != null)
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_BEGIN), begin);
		if(end != null) {
			Literal endTyped = model.createTypedLiteral(end);
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_END), endTyped);
		}
		if(subject != null)
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_SUBJECT), subject);

		if(attributes != null) {
			for(Entry<String, Object> entry:attributes.entrySet()) {
				Property attribute = model.getProperty(entry.getKey());
				RDFNode value = JenaUtils.asJenaLiteral(model, entry.getValue());
				or.addProperty(attribute, value);
			}
		}

		String asString = writeToString(model);
		return postResource(traceURIWithoutAspect, asString);
	}
}
