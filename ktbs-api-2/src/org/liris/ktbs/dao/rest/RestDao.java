package org.liris.ktbs.dao.rest;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.dao.DaoException;
import org.liris.ktbs.dao.ProxyFactory;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.dao.ResultSet;
import org.liris.ktbs.dao.UserAwareDao;
import org.liris.ktbs.domain.ResourceFactory;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.serial.Deserializer;
import org.liris.ktbs.serial.LinkAxis;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.serial.SerializationMode;
import org.liris.ktbs.serial.Serializer;
import org.liris.ktbs.serial.rdf.Java2Rdf;
import org.liris.ktbs.service.impl.ResourceNotFoundException;
import org.liris.ktbs.utils.KtbsUtils;
import org.liris.ktbs.utils.RelativeURITurtleReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RestDao implements ResourceDao, UserAwareDao {

	private static final Log log = LogFactory.getLog(RestDao.class);

	private static String sendMimeType = KtbsConstants.MIME_TURTLE;

	private Map<String, String> etags = new HashMap<String, String>();
	private KtbsRestClient client;

	private Serializer serializer;
	private Deserializer deserializer;
	private String rootUri;
	
	public void setRootUri(String rootUri) {
		this.rootUri = rootUri;
	}
	
	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	public void setDeserializer(Deserializer deserializer) {
		this.deserializer = deserializer;
	}

	public RestDao(KtbsRestClient client) {
		super();
		this.client = client;
	}

	public RestDao(Map<String, String> etags, KtbsRestClient client,
			Serializer serializer, Deserializer deserializer,
			ProxyFactory proxyFactory, ResourceFactory pojoFactory) {
		super();
		this.etags = etags;
		this.client = client;
		this.serializer = serializer;
		this.deserializer = deserializer;
		this.proxyFactory = proxyFactory;
		this.pojoFactory = pojoFactory;
	}

	private ProxyFactory proxyFactory;
	private ResourceFactory pojoFactory;

	public void setLinkedResourceFactory(ProxyFactory linkedResourceFactory) {
		this.proxyFactory = linkedResourceFactory;
	}

	public void setResourceFactory(ResourceFactory resourceFactory) {
		this.pojoFactory = resourceFactory;
	}

	public void destroy() {
		client.endSession();
	}

	public void init() {
		client.startSession();
	}

	@Override
	public <T extends IKtbsResource> T get(String uri, Class<T> cls) {
		
		String absoluteUri = KtbsUtils.makeChildURI(rootUri, uri, KtbsUtils.isLeafType(cls));

		String requestUri = absoluteUri;
		if(ITrace.class.isAssignableFrom(cls) && !uri.endsWith(KtbsConstants.ABOUT_ASPECT))
			requestUri+=KtbsConstants.ABOUT_ASPECT;

		log.info("Retrieving the resource " + uri);
		KtbsResponse response = client.get(requestUri);

		if(!response.hasSucceeded())
			return null;
		else {
			saveEtag(requestUri, response);

			String bodyAsString = response.getBodyAsString();
			String mimeType = response.getMimeType();

			if(mimeType.equals(KtbsConstants.MIME_TURTLE)) {
				log.info("Resolving relative uris for parsing turtle syntax against the base uri " + requestUri);
				bodyAsString = new RelativeURITurtleReader().resolve(bodyAsString, requestUri);
			}

			T resource = deserializer.deserializeResource(
					absoluteUri,
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
		if(KtbsUtils.isTraceModelElement(resource))
			throw new DaoException("A trace model element cannot be created through the current " +
			"Rest API. Modify and save its parent trace model instead.");
		StringWriter writer = new StringWriter();
		serializer.serializeResource(writer, resource, sendMimeType);

		log.info("Creating the resource " + resource.getUri());
		KtbsResponse response = client.post(resource.getParentUri(), writer.toString());
		if(response.hasSucceeded()) {
			return get(response.getHTTPLocation(), (Class<T>)resource.getClass());
		} else {
			if(isUriAlreadyInUse(response)) {
				throw new ResourceAlreadyExistException(resource.getUri());
			} else
				throw new DaoException("Could not create the resource " + resource.getUri() +". Message: " + response.getServerMessage() + " ["+response.getKtbsStatus()+"]");
		}
	}


	private boolean isUriAlreadyInUse(KtbsResponse response) {
		return response.getServerMessage() != null 
		&& response.getServerMessage().matches("400 URI Already in Use");
	}

	@Override
	public boolean save(IKtbsResource resource, boolean cascadeChildren) {
		
		SerializationConfig config = new SerializationConfig();
		if(cascadeChildren) {
			if(!KtbsUtils.isTrace(resource) && !KtbsUtils.isTraceModel(resource)) {
				log.warn("Cascading save is not supported for a resource of type " + resource.getTypeUri());
			} else
				config.configure(LinkAxis.CHILD, SerializationMode.CASCADE);
		}

		if(KtbsUtils.isTraceModelElement(resource)) {
			throw new DaoException("Saving an "+resource.getClass()+" is forbidden is the current version" +
			" of the Rest API. Save the parent trace model with children cascading instead.");
		} else if(KtbsUtils.isObsel(resource)) {
			throw new DaoException("Saving an obsel is forbidden is the current version" +
			" of the Rest API. Save the parent trace with children cascading instead.");
		} else if(KtbsUtils.isTraceModel(resource)) {
			if(!cascadeChildren)
				log.warn("Saving a trace model automatically cascade to children " +
				"(obsel types, attribute types, and relation types)");

			SerializationConfig tmConfig = new SerializationConfig(config);
			tmConfig.configure(LinkAxis.CHILD, SerializationMode.CASCADE);
			return save(resource.getUri(), resource, tmConfig);
		} else if(KtbsUtils.isTrace(resource)) {
			ITrace trace = (ITrace)resource;

			String aboutUri = resource.getUri() + KtbsConstants.ABOUT_ASPECT;
			String obselsUri = resource.getUri() + KtbsConstants.OBSELS_ASPECT;

			SerializationConfig aboutConfig = new SerializationConfig(config);
			aboutConfig.configure(LinkAxis.CHILD, SerializationMode.NOTHING);

			boolean aboutSaved = save(aboutUri, trace, aboutConfig);
			if(cascadeChildren) {
				boolean obselsSaved = saveCollection(obselsUri, trace.getObsels());
				return obselsSaved && aboutSaved;
			} else
				return aboutSaved;
		} else 
			return save(resource.getUri(), resource, config);

	}

	private boolean save(String updateUri, IKtbsResource resourceToSave,
			SerializationConfig config) {
		String etag = getEtag(updateUri);


		StringWriter writer = new StringWriter();

		/*
		 * KTBS FIX should use the serializer when KTBS is debugged for update resource: 
		 * 
		 * serializer.serializeResource(writer, resource, sendMimeType, config);
		 */
		temporaryFix(resourceToSave, config, writer);
		/*
		 * END of fix
		 */

		if(etag == null)
			throw new ResourceNotFoundException(updateUri);

		log.info("Saving the resource " + updateUri +".");
		KtbsResponse response = client.update(updateUri, writer.toString(), etag);
		saveEtag(updateUri, response);

		boolean hasSucceeded = response.hasSucceeded();
		if(hasSucceeded)
			return true;
		else {
			if(response.getHttpStatusCode() == KtbsConstants.HTTP_CODE_RESOURCE_NOT_FOUND)
				throw new DaoException("Save failed for the resource "+ updateUri+". Resource does not exists.", true);
		}
		return hasSucceeded;
	}

	@Override
	public boolean save(IKtbsResource resource) {
		return save(resource, false);
	}

	@Override
	public boolean postCollection(String uriToPost, List<? extends IKtbsResource> collection) {
		throw new DaoException("Not yet implemented on any KTBS server");
	}
	
	@Override
	public boolean saveCollection(String uriToSave, Collection<? extends IKtbsResource> collection) {
		String etag = getEtag(uriToSave);
		StringWriter writer = new StringWriter();

		serializer.serializeResourceSet(writer, collection, sendMimeType);
		
		log.info("Saving a collection of resources (nb= "+collection.size()+") at uri " + uriToSave);
		KtbsResponse response = client.update(uriToSave, writer.toString(), etag);
		if(response.hasSucceeded()) 
			return saveEtag(uriToSave, response);
		else
			return false;
	}

	private boolean saveEtag(String uri, KtbsResponse response) {
		String httpeTag = response.getHTTPETag();
		if(httpeTag != null) {
			String cachedEtag = etags.get(uri);
			if(cachedEtag == null || !cachedEtag.equals(httpeTag)) {
				//				String s = KtbsUtils.replaceLast(httpeTag, "\"", "").replaceFirst("\"", "");
				etags.put(uri, httpeTag);
				return true;
			} else
				return false;
		}
		else {
			log.debug("No etag was attached to the resource \""+uri+"\".");
			return false;
		}
	}

	private String getEtag(String uriToSave) {
		String etag = etags.get(uriToSave);
		if(etag == null) {
			// should update the etag
			KtbsResponse response = client.get(uriToSave);

			if(!response.hasSucceeded() || response.getHTTPETag() == null) {
				log.warn("Could not find an etag for the resource \""+uriToSave+"\".");
				return null;
			} else 
				saveEtag(uriToSave, response);
		}

		return etags.get(uriToSave);
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
				saveEtag(request, response);

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

	@Override
	public void setCredentials(String username, String password) {
		client.setCredentials(username, password);
	}
}