package org.liris.ktbs.rdf;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.rdf.resource.KtbsJenaResource;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;

import com.hp.hpl.jena.rdf.model.Model;

public class KtbsJenaResourceHolderImpl implements KtbsJenaResourceHolder {

	private KtbsJenaResourceFactory factory;

	private Map<String, KtbsResource> resources;

	public KtbsJenaResourceHolderImpl() {
		resources = new HashMap<String, KtbsResource>();
		factory = new KtbsJenaResourceFactory(this);
	}


	@Override
	public boolean exists(String uri) {
		return resources.containsKey(uri);
	}

	@Override
	public <T extends KtbsResource> T getResource(String uri, Class<T> clazz) {
		if(!exists(uri))
			resources.put(uri, factory.createResource(uri, clazz));
		return clazz.cast(resources.get(uri));
	}

	@Override
	public <T extends KtbsResource> T loadResourceFromStream(String uri,
			InputStream stream, String lang, Class<T> clazz) {
		T resource = factory.createResource(uri, stream, lang, clazz);
		resources.put(uri, resource);
		return resource;
	}

	@Override
	public <T extends KtbsResource> T getResourceAlreadyInModel(String uri,
			Class<T> clazz, Model rdfModel) {
		if(!exists(uri))
			resources.put(uri, factory.createResource(uri, clazz, rdfModel));
		return clazz.cast(resources.get(uri));
	}

	@Override
	public <T extends KtbsResource> T putResource(T resource) {
		if(resource.getURI() != null) {
			resources.put(resource.getURI(), resource);
			return resource;
		}
		else 
			throw new IllegalStateException("uri must not be null");
	}

	@Override
	public void removeResource(String uri) {
		resources.remove(uri);
	}

	@Override
	public void addResourceAsPartOfExistingModel(KtbsResource resource, Model rdfModel) {
		if (rdfModel instanceof KtbsJenaResource) {
			KtbsJenaResource kjResource = (KtbsJenaResource) rdfModel;
			rdfModel.add(kjResource.getJenaModel());
			KtbsResource newResource = factory.createResource(resource.getURI(), resource.getClass(), rdfModel);
			resources.put(newResource.getURI(), newResource);
		} else
			throw new UnsupportedOperationException("Must be a KTBS jena obsel");
	}
}
