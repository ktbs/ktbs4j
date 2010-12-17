package org.liris.ktbs.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
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
import org.apache.http.util.VersionInfo;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.impl.KtbsResourceFactory;
import org.liris.ktbs.rdf.JenaConstants;
import org.liris.ktbs.rdf.KtbsConstants;
import org.liris.ktbs.rdf.KtbsResourceReader;
import org.liris.ktbs.rdf.KtbsResourceWriter;


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
	}

	public void closeSession() {
		if(httpClient != null) 
			httpClient.getConnectionManager().shutdown();
	}
	@Override
	protected void finalize() throws Throwable {
		closeSession();
		super.finalize();
	}


	@Override
	public void createTrace(String baseURI, Trace trace) {
		postKtbsResource(baseURI, trace);
	}

	@Override
	public void addBaseToRoot(String rootURI, Base base) {
		postKtbsResource(rootURI, base);
	}

	private void postKtbsResource(String parentURI, KtbsResource ktbsResource) {
		String stringRepresentation = new KtbsResourceWriter(ktbsResource).serializeToString(JenaConstants.JENA_SYNTAX_TURTLE,true);
		log.debug("String representation of resource: " + stringRepresentation);

		performPostRequest(parentURI, stringRepresentation);
	}

	private void performPostRequest(String parentURI,
			String stringRepresentation) {
		HttpPost post = new HttpPost(parentURI);
		post.addHeader(HttpHeaders.CONTENT_TYPE, KtbsConstants.MIME_TURTLE);

		try {
			post.setEntity(new StringEntity(stringRepresentation));
		} catch (UnsupportedEncodingException e) {
			log.error("Cannot decode the content of the response sent by the KTBS", e);
		}

		try {
			log.info("Sending POST request to the KTBS: "+post.getRequestLine());
			HttpResponse response = httpClient.execute(post);

			if(log.isDebugEnabled()) {
				InputStream contentStream = response.getEntity().getContent();
				String body = IOUtils.toString(contentStream);
				log.debug("Response header:" + response.getStatusLine());
				log.debug("Response body:\n" + body);
			}
		} catch (ClientProtocolException e) {
			log.error("An error occured when communicating with the KTBS", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("An exception occurred when reading the content of the HTTP response", e);
		}
	}

	@Override
	public void createObsel(String traceURI, Obsel obsel) {
		postKtbsResource(traceURI, obsel);
	}

	@Override
	public void createRelation(String traceURI, String relationName, String fromObselURI, String toObselURI) {
		String stringRepresentation = KtbsResourceWriter.relationToString(fromObselURI, relationName, toObselURI, JenaConstants.JENA_SYNTAX_N_TRIPLES);
		performPostRequest(traceURI, stringRepresentation);
	}

	@Override
	public void addAttributeToObsel(String obselURI, String attributeName,
			Serializable value) {
		//TODO put request
	}

	@Override
	public KtbsRoot getKtbsRoot(String rootURI) {
		return performGetRequest(rootURI, KtbsRoot.class, null);
	}

	private <T extends KtbsResource> T performGetRequest(String ktbsResourceURI, Class<T> clazz, String restAspect) {
		HttpGet get = new HttpGet(ktbsResourceURI);
		get.addHeader(HttpHeaders.ACCEPT, KtbsConstants.MIME_NTRIPLES);

		try {
			HttpResponse response = httpClient.execute(get);
			if(response == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+ktbsResourceURI+"\".");
				return null;
			}

			HttpEntity entity = response.getEntity();
			if(entity == null) {
				log.warn("Impossible to read the resource in the response sent by the KTBS server for the resource URI \""+ktbsResourceURI+"\".");
				return null;
			}
			KtbsResourceReader reader = new KtbsResourceReader();

			T ktbsResource = reader.deserializeFromStream(
					ktbsResourceURI,
					entity.getContent(), 
					JenaConstants.JENA_SYNTAX_N_TRIPLES, 
					clazz,
					restAspect);
			return ktbsResource;
		} catch (ClientProtocolException e) {
			log.warn("A HTTP exception occurred when getting the resource \""+ktbsResourceURI+"\" from the KTBS server.", e);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void createBase(String rootURI, String baseLocalName) {
		KtbsRoot createKtbsRoot = KtbsResourceFactory.createKtbsRoot(rootURI);
		Base b = KtbsResourceFactory.createBase(createKtbsRoot.getURI()+baseLocalName+"/", createKtbsRoot);
		postKtbsResource(rootURI, b);
	}

	@Override
	public Base getBase(String rootURI, String baseLocalName) {
		return getBase(rootURI+baseLocalName+"/");
	}

	@Override
	public Base getBase(String baseURI) {
		return performGetRequest(baseURI, Base.class, null);
	}

	@Override
	public void createTrace(String baseURI, String traceLocalName, String traceModelURI, Date origin) {
		Base base = KtbsResourceFactory.createBase(baseURI, null);
		Trace trace = KtbsResourceFactory.createTrace(base.getURI()+traceLocalName+"/", traceModelURI, origin, base);
		postKtbsResource(baseURI, trace);
	}

	@Override
	public Collection<String> listBaseURIs(String rootURI) {
		KtbsRoot root = performGetRequest(rootURI, KtbsRoot.class, null);
		return Collections.unmodifiableCollection(root.getBaseURIs());
	}

	@Override
	public Trace getTrace(String traceURI) {
		Trace obsels  = performGetRequest(traceURI+KtbsConstants.OBSELS_ASPECT, Trace.class, KtbsConstants.OBSELS_ASPECT);
		Trace traceInfo = performGetRequest(traceURI+KtbsConstants.ABOUT_ASPECT, Trace.class, KtbsConstants.ABOUT_ASPECT);

		for(Obsel obsel:obsels.getObsels())
			traceInfo.addObsel(obsel);
		return traceInfo;
	}

	@Override
	public Collection<String> listTraceURIs(String baseURI) {
		Base base = performGetRequest(baseURI, Base.class, null);
		return Collections.unmodifiableCollection(base.getTraceURIs());
	}

	@Override
	public Collection<String> listTraceModelURIs(String baseURI) {
		Base base = performGetRequest(baseURI, Base.class, null);
		return Collections.unmodifiableCollection(base.getTraceModelURIs());
	}

	@Override
	public Trace getTrace(String baseURI, String traceLocalName) {
		return getTrace(baseURI+traceLocalName+"/");
	}

	@Override
	public Trace getTrace(String rootURI, String baseLocalName,
			String traceLocalName) {
		return getTrace(rootURI+baseLocalName+"/", traceLocalName);
	}

	@Override
	public void deleteRelation(String traceURI, String relationName,
			String fromObselURI, String toObselURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBase(String rootURI, String baseLocalName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBase(String rootURI, Base base) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBase(String baseURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTrace(String baseURI, String traceLocalName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteTrace(String traceURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteObsel(String obselURI) {
		// TODO Auto-generated method stub
		
	}
}
