package org.liris.ktbs.dao.rest;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.dao.DaoException;
import org.liris.ktbs.dao.ProxyFactory;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.dao.ResultSet;
import org.liris.ktbs.dao.UserAwareDao;
import org.liris.ktbs.domain.ResourceFactory;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.serial.Deserializer;
import org.liris.ktbs.serial.LinkAxis;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.serial.SerializationMode;
import org.liris.ktbs.serial.Serializer;
import org.liris.ktbs.serial.rdf.Java2Rdf;
import org.liris.ktbs.service.impl.ResourceNotFoundException;
import org.liris.ktbs.utils.KtbsUtils;
import org.liris.ktbs.utils.RelativeURITurtleReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class RestDao implements ResourceDao, UserAwareDao {

	private static final Logger log = LoggerFactory.getLogger(RestDao.class);

	private static String sendMimeType = KtbsConstants.MIME_RDF_XML;
	private static String receiveMimeType = KtbsConstants.MIME_RDF_XML;

	private Map<String, String> etags = new HashMap<String, String>();
	private KtbsRestClient client;

	private Serializer serializer;
	private Deserializer deserializer;
	private String rootUri;
	private KtbsResponse lastResponse;

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

		String absoluteUriWithoutAspect = KtbsUtils.makeChildURI(rootUri, uri, KtbsUtils.isLeafType(cls));

		String requestUri = absoluteUriWithoutAspect;
		if(ITrace.class.isAssignableFrom(cls) && !uri.endsWith(KtbsConstants.ABOUT_ASPECT))
			requestUri+=KtbsConstants.ABOUT_ASPECT;

		log.debug("Retrieving the resource " + uri);
		KtbsResponse response = client.get(requestUri, receiveMimeType);
		this.lastResponse = response;

		if(!response.hasSucceeded())
			return null;
		else {

			String bodyAsString = response.getBodyAsString();


			String mimeType = response.getMimeType();


			if(mimeType.equals(KtbsConstants.MIME_TURTLE)) {
				log.debug("Resolving relative uris for parsing turtle syntax against the base uri " + requestUri);
				bodyAsString = new RelativeURITurtleReader().resolve(bodyAsString, requestUri);
			}

			T resource = deserializer.deserializeResource(
					absoluteUriWithoutAspect,
					new StringReader(bodyAsString), 
					requestUri, 
					mimeType, 
					cls);


			String uriWithoutAspects = KtbsUtils.removeUriAspects(requestUri);
			Class<? extends IKtbsResource> foundResourceType = KtbsUtils.guessResourceType(deserializer.getLastDeserializedModel(), uriWithoutAspects);

			if(foundResourceType == null) {
				/*
				 * Maybe a TraceModel since KTBS returns no rdf:type statement for a ktbs:TraceModel
				 */
				if(!ITraceModel.class.isAssignableFrom(cls)) {
					log.warn("Retrieved ressource {} was found to have no rdf:type statement, excepted type is \"{}\".", new Object[]{requestUri, foundResourceType, cls});
					return null;
				}
			} else if(!cls.isAssignableFrom(foundResourceType)) {
				log.warn("Retrieved ressource {} was found to be of type \"{}\" while the excepted type is \"{}\".", new Object[]{requestUri, foundResourceType, cls});
				return null;
			}


			saveEtag(requestUri, response);

			// This should be connected into the Rdf2Java mapper
			if (resource instanceof ITrace) {
				ITrace trace = (ITrace) resource;
				connectObselsQueryToTrace(trace);
			}

			return resource;
		}
	}

	@Override
	public <T extends IKtbsResource> T createAndGet(T resource) {
		KtbsResponse response = doCreate(resource);
		if(response.hasSucceeded()) {
			return get(response.getHTTPLocation(), (Class<T>)resource.getClass());
		} else {
			if(isUriAlreadyInUse(response)) {
				throw new ResourceAlreadyExistException(resource.getUri());
			} else
				throw new DaoException("Could not create the resource " + resource.getUri() +". Message: " + response.getServerMessage() + " ["+response.getKtbsStatus()+"]");
		}
	}


	@Override
	public String create(IKtbsResource resource) {
		KtbsResponse response = doCreate(resource);

		if(response.hasSucceeded()) {
			String httpLocation = response.getHTTPLocation();
			if(httpLocation == null)
				throw new DaoException("The resource was successfully created but no HTTP location (resource URI) was attached to the response.");
			else
				return httpLocation;
		} else 
			return null;
	}

	private KtbsResponse doCreate(IKtbsResource resource) {
		if(KtbsUtils.isTraceModelElement(resource))
			throw new DaoException("A trace model element cannot be created through the current " +
			"Rest API. Modify and save its parent trace model instead.");
		StringWriter writer = new StringWriter();
		serializer.serializeResource(writer, resource, sendMimeType);

		String uri = resource.getUri();
		log.debug("Creating resource [" + (uri==null?"anonymous":("uri: "+uri)) + ", type: " + resource.getClass().getSimpleName() + "]");
		KtbsResponse response = client.post(resource.getParentUri(), writer.toString(), sendMimeType);
		this.lastResponse = response;

		log.debug("Resource creation " + (response.hasSucceeded()?"succeeded":"failed"));
		return response;
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
		if(!hasETagSupport(resourceToSave.getClass())) {
			log.warn("The resource "+resourceToSave.getUri()+" could not be saved. Resources of type " + resourceToSave.getTypeUri() + " have not support for HTTP Etags in the KTBS.");
			return false;
		}

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

		log.debug("Saving the resource " + updateUri +".");
		KtbsResponse response = client.update(updateUri, writer.toString(), sendMimeType, etag);
		this.lastResponse = response;
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

	private boolean hasETagSupport(Class<? extends IKtbsResource> class1) {
		return !IRoot.class.isAssignableFrom(class1) && !IBase.class.isAssignableFrom(class1);
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

		log.debug("Saving a collection of resources (nb= "+collection.size()+") at uri " + uriToSave);
		KtbsResponse response = client.update(uriToSave, writer.toString(), sendMimeType, etag);
		this.lastResponse = response;
		if(response.hasSucceeded()) {
			return  saveEtag(uriToSave, response);
		} else
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
			KtbsResponse response = client.get(uriToSave, receiveMimeType);

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
		KtbsResponse response = client.delete(uri);
		this.lastResponse = response;
		return response.hasSucceeded();
	}


	@Override
	public <T extends IKtbsResource> ResultSet<T> query(String request,
			Class<T> cls) {


		KtbsResponse response = client.get(request, receiveMimeType);
		this.lastResponse = response;

		if(!response.hasSucceeded())
			return null;
		else {
			if(response.getHTTPETag() != null)
				saveEtag(request, response);

			String bodyAsString = response.getBodyAsString();
			String mimeType = response.getMimeType();

			if(mimeType.equals(KtbsConstants.MIME_TURTLE)) {
				log.debug("Resolving relative uris for parsing turtle syntax against the base uri " + request);
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
		log.debug("Retrieving the editable properties for uri " + uri);

		if(ITrace.class.isAssignableFrom(cls) && !uri.endsWith(KtbsConstants.ABOUT_ASPECT))
			uriWothAspect+=KtbsConstants.ABOUT_ASPECT;


		KtbsResponse response = client.get(uriWothAspect+"?editable", receiveMimeType);
		if(!response.hasSucceeded()) {
			log.debug("Could not get the editable properties from uri: " + uri);
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

	@Override
	public KtbsResponse getLastResponse() {
		return lastResponse;
	}
}
