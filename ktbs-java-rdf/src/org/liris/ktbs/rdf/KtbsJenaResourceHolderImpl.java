package org.liris.ktbs.rdf;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsResourceNotFoundException;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.resource.KtbsJenaResource;
import org.liris.ktbs.rdf.resource.KtbsJenaResourceFactory;
import org.liris.ktbs.utils.KtbsUtils;

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
		
		
		// TODO Merge models when this is an obsel
		
		T resource = factory.createResource(uri, stream, lang, clazz);

		putResourceAndChildren(resource);

		return resource;
	}


	private  void putResourceAndChildren(KtbsResource resource) {
		resources.put(resource.getURI(), resource);
		if(TraceModel.class.isAssignableFrom(resource.getClass())) {
			TraceModel tm = (TraceModel) resource;
			//add all the attribute types
			for(AttributeType type : KtbsUtils.toIterable(tm.listAttributeTypes()))
				resources.put(type.getURI(), type);

			//add all the relation types
			for(RelationType type : KtbsUtils.toIterable(tm.listRelationTypes()))
				resources.put(type.getURI(), type);

			//add all the obsel types
			for(ObselType type : KtbsUtils.toIterable(tm.listObselTypes()))
				resources.put(type.getURI(), type);
		} else if (Trace.class.isAssignableFrom(resource.getClass())) {
			Trace trace = (Trace) resource;
			//	add all obsels
			for(Obsel obsel: KtbsUtils.toIterable(trace.listObsels()))
				resources.put(obsel.getURI(), obsel);
		}
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
			putResourceAndChildren(resource);
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
		if (resource instanceof KtbsJenaResource) {
			KtbsJenaResource kjResource = (KtbsJenaResource) resource;
			rdfModel.add(kjResource.getJenaModel());
			resources.put(kjResource.getURI(), kjResource);
		} else
			throw new UnsupportedOperationException("Must be a KTBS jena obsel");
	}


	@Override
	public  <T extends KtbsResource> boolean existsOfType(String uri, Class<T> class1) {
		KtbsResource r = resources.get(uri);
		return (r!=null) && class1.isAssignableFrom(r.getClass());
	}


	@Override
	public <T extends KtbsResource> T getAfterCheck(String uri, Class<T> clazz) {
		if(!exists(uri))
			throw new KtbsResourceNotFoundException(uri);
		else
			return getResource(uri, clazz);
	}
}
