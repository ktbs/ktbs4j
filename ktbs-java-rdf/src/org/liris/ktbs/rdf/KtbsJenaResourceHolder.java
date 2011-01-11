package org.liris.ktbs.rdf;

import java.io.InputStream;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsResourceHolder;

import com.hp.hpl.jena.rdf.model.Model;

public interface KtbsJenaResourceHolder extends KtbsResourceHolder {
	
	public <T extends KtbsResource> T loadResourceFromStream(String uri, InputStream stream, String lang, Class<T> clazz);
	public <T extends KtbsResource> T getResourceAlreadyInModel(String uri, Class<T> clazz, Model rdfModel);
	public void addResourceAsPartOfExistingModel(KtbsResource resource, Model rdfModel);
	
	
}
