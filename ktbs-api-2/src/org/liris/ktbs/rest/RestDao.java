package org.liris.ktbs.rest;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.ProxyFactory;
import org.liris.ktbs.core.ResultSet;
import org.liris.ktbs.core.domain.ResourceFactory;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.ITrace;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.serial.Deserializer;
import org.liris.ktbs.serial.Serializer;
import org.liris.ktbs.utils.RelativeURITurtleReader;

public class RestDao implements ResourceDao {

	private static final Log log = LogFactory.getLog(RestDao.class);

	private static String sendMimeType = KtbsConstants.MIME_TURTLE;

	private Map<String, String> etags = new HashMap<String, String>();
	private KtbsRestClient client;

	private Serializer serializer;
	private Deserializer deserializer;

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	public void setDeserializer(Deserializer deserializer) {
		this.deserializer = deserializer;
	}

	private ProxyFactory proxyFactory;
	private ResourceFactory pojoFactory;

	public void setLinkedResourceFactory(ProxyFactory linkedResourceFactory) {
		this.proxyFactory = linkedResourceFactory;
	}

	public void setResourceFactory(ResourceFactory resourceFactory) {
		this.pojoFactory = resourceFactory;
	}

	private String username;
	private String passwd;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	private boolean requireAuthentication = false;

	public void setRequireAuthentication(String required) {
		this.requireAuthentication = required!=null && required.equals("required");
	}

	public void setClient(KtbsRestClient client) {
		this.client = client;
	}

	public void destroy() {
		client.endSession();
	}

	public void init() {
		if(requireAuthentication)
			client.startSession(username, passwd);
		else
			client.startSession(null,null);

	}

	@Override
	public <T extends IKtbsResource> T get(String uri, Class<T> cls) {
		String requestUri = uri;

		if(ITrace.class.isAssignableFrom(cls) && !uri.endsWith(KtbsConstants.ABOUT_ASPECT))
			requestUri+=KtbsConstants.ABOUT_ASPECT;

		KtbsResponse response = client.get(requestUri);

		if(!response.hasSucceeded())
			return null;
		else {
			if(response.getHTTPETag() != null)
				etags.put(uri, response.getHTTPETag());
			else
				log.warn("No etag was attached to the resource \""+uri+"\".");

			String bodyAsString = response.getBodyAsString();
			String mimeType = response.getMimeType();

			if(mimeType.equals(KtbsConstants.MIME_TURTLE)) {
				log.info("Resolving relative uris for parsing turtle syntax against the base uri " + uri);
				bodyAsString = new RelativeURITurtleReader().resolve(bodyAsString, uri);
			}

			T resource = deserializer.deserializeResource(
					uri,
					new StringReader(bodyAsString), 
					requestUri, 
					mimeType, 
					cls);

			// This should be connected into the Rdf2Java mapper
			if (resource instanceof ITrace) {
				ITrace trace = (ITrace) resource;
				connectObselsQueryToTrace(trace);
			}

			return resource;
		}
	}

	@Override
	public boolean create(IKtbsResource resource) {
		StringWriter writer = new StringWriter();
		serializer.serializeResource(writer, resource, sendMimeType);
		KtbsResponse response = client.post(resource);
		if(response.hasSucceeded()) {
			if(!response.getHTTPLocation().equals(resource.getUri()))
				log.warn("The resource has been created but in a diffrent uri location than expected.");
			return true;
		} else
			return false;
	}

	@Override
	public boolean save(IKtbsResource resource) {
		StringWriter writer = new StringWriter();
		serializer.serializeResource(writer, resource, sendMimeType);
		String etag = etags.get(resource.getUri());
		if(etag == null)
			// should update the etag
			get(resource.getUri(), resource.getClass());

		etag = etags.get(resource.getUri());
		if(etag == null) {
			log.warn("Could not find an etag for the resource \""+resource.getUri()+"\".");
			return false;
		}

		KtbsResponse response = client.update(resource, etag);
		return response.hasSucceeded();
	}

	@Override
	public boolean delete(String uri) {
		return client.delete(uri).hasSucceeded();
	}


	@Override
	public <T extends IKtbsResource> ResultSet<T> query(String request,
			Class<T> cls) {


		KtbsResponse response = client.get(request);

		if(!response.hasSucceeded())
			return null;
		else {
			if(response.getHTTPETag() != null)
				etags.put(request, response.getHTTPETag());

			String bodyAsString = response.getBodyAsString();
			String mimeType = response.getMimeType();

			if(mimeType.equals(KtbsConstants.MIME_TURTLE)) {
				log.info("Resolving relative uris for parsing turtle syntax against the base uri " + request);
				bodyAsString = new RelativeURITurtleReader().resolve(bodyAsString, request);
			}

			ResultSet<T> resultSet = deserializer.deserializeResourceSet(cls, new StringReader(bodyAsString), request, mimeType);

			for(T r:resultSet) {
				// This should be connected into the Rdf2Java mapper
				if (r instanceof ITrace) {
					ITrace trace = (ITrace) r;
					connectObselsQueryToTrace(trace);
				}
			}
			return resultSet;
		}
	}

	private void connectObselsQueryToTrace(ITrace trace) {
		ProxyFactory fac = (ProxyFactory)proxyFactory;
		String obselsRequest = trace.getUri()+KtbsConstants.OBSELS_ASPECT;
		trace.setObsels(fac.createResourceSetProxy(obselsRequest, IObsel.class));
	}
}
