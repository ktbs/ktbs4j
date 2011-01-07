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
			return clazz.cast(createTraceModel(uri, stream, lang));
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

	public Obsel createObsel(String uri, Model rdfModel) {
		return new KtbsJenaObsel(uri,rdfModel);
	}
	
	public RelationType createRelationType(String relationTypeUri, Model rdfModel) {
		return new KtbsJenaRelationType(relationTypeUri, rdfModel);
	}

	public AttributeType createAttributeType(String attTypeUri, Model rdfModel) {
		return new KtbsJenaAttributeType(attTypeUri, rdfModel);
	}

	public ObselType createObselType(String obsTypeUri, Model rdfModel) {
		return new KtbsJenaObselType(obsTypeUri, rdfModel);
	}

	public <T extends KtbsResource> T createResource(String obsTypeUri, Model rdfModel, Class<T> clazz) {
		if(ObselType.class.equals(clazz))
			return clazz.cast(createObselType(obsTypeUri, rdfModel));
		else if(RelationType.class.equals(clazz))
			return clazz.cast(createRelationType(obsTypeUri, rdfModel));
		else if(AttributeType.class.equals(clazz))
			return clazz.cast(createAttributeType(obsTypeUri, rdfModel));
		else if(Obsel.class.equals(clazz))
			return clazz.cast(createObsel(obsTypeUri, rdfModel));
		else
			throw new UnsupportedOperationException("Cannot create an instance of class \""+clazz.getCanonicalName()+"\"");
	}
	public TraceModel createTraceModel(String uri, InputStream stream,
			String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaTraceModel(uri, rdfModel);
	}
}
