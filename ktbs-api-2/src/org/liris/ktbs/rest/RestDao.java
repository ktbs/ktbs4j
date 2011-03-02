package org.liris.ktbs.rest;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import org.liris.ktbs.rdf.Java2Rdf;
import org.liris.ktbs.serial.Deserializer;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.serial.Serializer;
import org.liris.ktbs.utils.KtbsUtils;
import org.liris.ktbs.utils.RelativeURITurtleReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

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
	public <T extends IKtbsResource> T create(T resource) {
		StringWriter writer = new StringWriter();
		serializer.serializeResource(writer, resource, sendMimeType);
		KtbsResponse response = client.post(resource.getParentUri(), writer.toString());
		if(response.hasSucceeded()) {
			return get(response.getHTTPLocation(), (Class<T>)resource.getClass());
		} else
			return null;
	}

	@Override
	public boolean save(IKtbsResource resource) {
		/*
		 * Manage special case if obsel or trace model element.
		 * If stored trace or trace model, cascade to children automatically
		 */
		
		
		String etag = etags.get(resource.getUri());
		if(etag == null)
			// should update the etag
			get(resource.getUri(), resource.getClass());

		etag = etags.get(resource.getUri());
		if(etag == null) {
			log.warn("Could not find an etag for the resource \""+resource.getUri()+"\".");
			return false;
		}


		String updateUri = resource.getUri();

		if(ITrace.class.isAssignableFrom(resource.getClass()) && !resource.getUri().endsWith(KtbsConstants.ABOUT_ASPECT))
			updateUri+=KtbsConstants.ABOUT_ASPECT;

		
		StringWriter writer = new StringWriter();

		/*
		 * KTBS FIX should use the serializer when KTBS is debugged for update resource: 
		 * 
		 * serializer.serializeResource(writer, resource, sendMimeType, config);
		 */
		temporaryFix(resource, new SerializationConfig(), writer);
		/*
		 * END of fix
		 */
		

		KtbsResponse response = client.update(updateUri, writer.toString(), etag);
		return response.hasSucceeded();
	}

	private void temporaryFix(IKtbsResource resource,
			SerializationConfig config, StringWriter writer) {
		Java2Rdf java2Rdf = new Java2Rdf(config);
		Model model = java2Rdf.getModel(resource);
		Model editableModel = getEditable(model, resource.getUri(), resource.getClass());
		if(editableModel != null) {
			Set<String> editableProperties = new TreeSet<String>();
			StmtIterator it = editableModel.listStatements();
			while (it.hasNext()) {
				Statement statement = (Statement) it.next();
				editableProperties.add(statement.getPredicate().getURI());
			}

			it = model.listStatements();
			while (it.hasNext()) {
				Statement statement = (Statement) it.next();
				if(!editableProperties.contains(statement.getPredicate().getURI())) {
					log.warn("A non-editable statement has been removed from the rdf sent to KTBS: " + statement);
					it.remove();
				}
			}
		}
		model.write(writer, KtbsUtils.getJenaSyntax(sendMimeType), "");
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

	/*
	 * Temporary fix for the KTBS not accepting property in reserved namespace to be saved.
	 * 
	 */
	private Model getEditable(Model model, String uri, Class<?> cls) {
		String uriWothAspect = uri;

		if(ITrace.class.isAssignableFrom(cls) && !uri.endsWith(KtbsConstants.ABOUT_ASPECT))
			uriWothAspect+=KtbsConstants.ABOUT_ASPECT;


		KtbsResponse response = client.get(uriWothAspect+"?editable");
		if(!response.hasSucceeded()) {
			log.warn("Could not get the editable properties from uri: " + uri);
			return null;
		} else {
			String modelAsString = response.getBodyAsString();
			Model editableModel = ModelFactory.createDefaultModel();
			try {
				String uriWithoutAspect = uriWothAspect.replaceAll(KtbsConstants.ABOUT_ASPECT, "").replaceAll(KtbsConstants.OBSELS_ASPECT, "");

				editableModel.read(new StringReader(modelAsString), uriWothAspect, KtbsUtils.getJenaSyntax(response.getMimeType()));



			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			return editableModel;
		}

	}

	private void connectObselsQueryToTrace(ITrace trace) {
		ProxyFactory fac = (ProxyFactory)proxyFactory;
		String obselsRequest = trace.getUri()+KtbsConstants.OBSELS_ASPECT;
		trace.setObsels(fac.createResourceSetProxy(obselsRequest, IObsel.class));
	}
}
