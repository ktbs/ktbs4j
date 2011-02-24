package org.liris.ktbs.rest;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.domain.KtbsResource;
import org.liris.ktbs.dao.ResourceDao;
import org.liris.ktbs.serial.RdfResourceSerializer;

public class RestDao implements ResourceDao {
	
	private static final Log log = LogFactory.getLog(RestDao.class);
	
	private static String sendMimeType = KtbsConstants.MIME_TURTLE;
	private static String receiveMimeType = KtbsConstants.MIME_NTRIPLES;
	
	private Map<String, String> etags = new HashMap<String, String>();
	private KtbsRestService service;
	
	private String rootUri;
	
	public RestDao(String rootUri) {
		this.rootUri = rootUri;
	}
	
	public void init(String user, String password) {
		service = new KtbsRestServiceImpl(rootUri);
	}

	@Override
	public KtbsResource get(String uri) {
		KtbsResponse response = service.get(uri);
		if(!response.hasSucceeded())
			return null;
		else {
			if(response.getHTTPETag() != null)
				etags.put(uri, response.getHTTPETag());
			else
				log.warn("No etag was attached to the resource \""+uri+"\".");
			
			KtbsResource resource = new RdfResourceSerializer().deserialize(
					uri,
					new StringReader(response.getBodyAsString()), 
					response.getMimeType());
			return resource;
		}
	}

	@Override
	public boolean create(KtbsResource resource) {
		StringWriter writer = new StringWriter();
		new RdfResourceSerializer().serialize(writer, resource, sendMimeType);
		KtbsResponse response = service.post(resource);
		if(response.hasSucceeded()) {
			if(!response.getHTTPLocation().equals(resource.getUri()))
				log.warn("The resource has been created but in a diffrent uri location than expected.");
				return true;
		} else
			return false;
	}

	@Override
	public boolean save(KtbsResource resource) {
		StringWriter writer = new StringWriter();
		new RdfResourceSerializer().serialize(writer, resource, sendMimeType);
		String etag = etags.get(resource.getUri());
		if(etag == null)
			// should update the etag
			get(resource.getUri());
		
		etag = etags.get(resource.getUri());
		if(etag == null) {
			log.warn("Could not find an etag for the resource \""+resource.getUri()+"\".");
			return false;
		}
		
		KtbsResponse response = service.update(resource, etag);
		return response.hasSucceeded();
	}

	@Override
	public boolean delete(String uri) {
		return service.delete(uri).hasSucceeded();
	}
}
