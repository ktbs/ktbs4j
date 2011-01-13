package org.liris.ktbs.rdf;

import java.io.InputStream;

import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.ResourceRepository;
import org.liris.ktbs.rdf.resource.MultipleResourcesInStreamException;
import org.liris.ktbs.rdf.resource.ResourceLoadException;

import com.hp.hpl.jena.rdf.model.Model;

public interface RDFResourceRepository extends ResourceRepository {
	
	/**
	 * Reads a Ktbs resource from an input stream and register it.
	 * 
	 * <p>
	 * If the resource loaded is an Obsel, the containing trace got by the 
	 * ktbs:hasTrace property is updated with this new obsel.
	 * </p>
	 * <p>
	 * If the resource loaded is an AttributeType, a RelationType, or an ObselType, 
	 * the containing trace model is obtained by resolving the parent URI of the resource.
	 * </p>
	 * 
	 * @param stream
	 * @param lang
	 * @return
	 * @throws MultipleResourcesInStreamException when the content of the stream cannot be interpreted as 
	 * a valid Ktbs resource.
	 */
	public KtbsResource loadResource(InputStream stream, String lang) throws ResourceLoadException;
	
	
	public <T extends KtbsResource> T getResourceAlreadyInModel(String uri, Class<T> clazz, Model rdfModel);
	
	
	public void addResourceAsPartOfExistingModel(KtbsResource resource, Model rdfModel);
	
	
}
