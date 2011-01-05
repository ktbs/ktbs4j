package org.liris.ktbs.rdf.resource;

import java.io.InputStream;

import org.liris.ktbs.core.KtbsRoot;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * A factory that builds KTBS resource that are connected to 
 * an underlying RDF Model.
 * 
 * @author Damien Cram
 *
 */
public class KtbsJenaResourceFactory {

	/*
	 * SINGLETON
	 */
	private static KtbsJenaResourceFactory  instance;
	private KtbsJenaResourceFactory(){}
	public static KtbsJenaResourceFactory getInstance() {
		if(instance == null)
			instance = new KtbsJenaResourceFactory();
		return instance;
	}
	
	public KtbsRoot createKtbsRoot(String uri, InputStream stream, String lang) {
		Model rdfModel = ModelFactory.createDefaultModel();
		rdfModel.read(stream, null, lang);
		return new KtbsJenaRoot(uri, rdfModel);
	}
}
