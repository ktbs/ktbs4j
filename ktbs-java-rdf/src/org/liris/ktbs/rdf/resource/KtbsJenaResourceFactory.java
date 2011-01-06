package org.liris.ktbs.rdf.resource;

import java.io.FileInputStream;
import java.io.InputStream;

import org.liris.ktbs.core.AttributeType;
import org.liris.ktbs.core.Base;
import org.liris.ktbs.core.ComputedTrace;
import org.liris.ktbs.core.KtbsResource;
import org.liris.ktbs.core.KtbsRoot;
import org.liris.ktbs.core.Method;
import org.liris.ktbs.core.Obsel;
import org.liris.ktbs.core.ObselType;
import org.liris.ktbs.core.RelationType;
import org.liris.ktbs.core.StoredTrace;
import org.liris.ktbs.core.TraceModel;

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

	public <T extends KtbsResource> T createResource(String uri, InputStream stream, String lang, Class<T> clazz) {
		if(StoredTrace.class.equals(clazz))
			return clazz.cast(createStoredTrace(uri, stream, lang));
		else if(ComputedTrace.class.equals(clazz))
			return clazz.cast(createStoredTrace(uri, stream, lang));
		else if(Obsel.class.equals(clazz))
			throw new UnsupportedOperationException("Cannot create an instance of class \""+clazz.getCanonicalName()+"\" from an input stream.");
		else if(Method.class.equals(clazz))
			throw new UnsupportedOperationException();
		else if(Base.class.equals(clazz))
			return clazz.cast(createBase(uri, stream, lang));
		else if(KtbsRoot.class.equals(clazz))
			return clazz.cast(createKtbsRoot(uri, stream, lang));
		else if(AttributeType.class.equals(clazz))
			throw new UnsupportedOperationException();
		else if(RelationType.class.equals(clazz))
			throw new UnsupportedOperationException();
		else if(ObselType.class.equals(clazz))
			throw new UnsupportedOperationException();
		else if(TraceModel.class.equals(clazz))
			throw new UnsupportedOperationException();
		else
			throw new UnsupportedOperationException("Cannot create an instance of class \""+clazz.getCanonicalName()+"\"");
	}

	public KtbsRoot createKtbsRoot(String uri, InputStream stream, String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaRoot(uri, rdfModel);
	}

	private Model createRdfModel(InputStream stream, String lang) {
		Model rdfModel = ModelFactory.createDefaultModel();
		rdfModel.read(stream, null, lang);
		return rdfModel;
	}

	public Base createBase(String uri, InputStream stream, String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaBase(uri, rdfModel);
	}
	
	public Obsel createObsel(String uri, Model rdfModel) {
		return new KtbsJenaObsel(uri,rdfModel);
	}
	
	public StoredTrace createStoredTrace(String uri, InputStream stream,
			String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaStoredTrace(uri, rdfModel);
	}
	
	public ComputedTrace createComputedTrace(String uri, InputStream stream,
			String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaComputedTrace(uri, rdfModel);
	}
	public Obsel createObsel(String string, FileInputStream fis,
			String jenaSyntaxTurtle) {
		Model rdfModel = createRdfModel(fis, jenaSyntaxTurtle);
		return new KtbsJenaObsel(string, rdfModel);
	}
}
