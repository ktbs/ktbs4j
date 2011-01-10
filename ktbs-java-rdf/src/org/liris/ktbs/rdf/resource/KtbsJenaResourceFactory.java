package org.liris.ktbs.rdf.resource;

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
import org.liris.ktbs.core.Trace;
import org.liris.ktbs.core.TraceModel;
import org.liris.ktbs.rdf.KtbsJenaResourceHolder;
import org.liris.ktbs.rdf.KtbsResourceInstanciationException;
import org.liris.ktbs.utils.KtbsUtils;

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

	private KtbsJenaResourceHolder holder;

	public	KtbsJenaResourceFactory(KtbsJenaResourceHolder holder) {
		super();
		this.holder = holder;
	}

	public <T extends KtbsResource> T createResource(String uri, InputStream stream, String lang, Class<T> clazz) {
		if(StoredTrace.class.equals(clazz))
			return clazz.cast(createStoredTrace(uri, stream, lang));
		else if(ComputedTrace.class.equals(clazz))
			return clazz.cast(createComputedTrace(uri, stream, lang));
		else if(Obsel.class.equals(clazz))
			throw new UnsupportedOperationException("Cannot create an instance of class \""+clazz.getCanonicalName()+"\" from an input stream.");
		else if(Method.class.equals(clazz))
			return clazz.cast(createMethod(uri, stream, lang));
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



	public <T extends KtbsResource> T createResource(String uri, Class<T> clazz) {
		String rdfType = KtbsUtils.getRDFType(clazz);
		if(rdfType == null && !clazz.equals(Trace.class)) 
			throw new KtbsResourceInstanciationException(clazz, "Cannot create a KTBS resource from the type " + clazz.getCanonicalName()+". No RDF type matching.");

		KtbsResource resource;
		Model model = ModelFactory.createDefaultModel();
		if(clazz.equals(Trace.class))
			return clazz.cast(new KtbsJenaTrace(uri, model, holder));
		else if(clazz.equals(KtbsResource.class)) 
			resource = new KtbsJenaResource(uri, model, holder);
		if(clazz.equals(KtbsRoot.class)) 
			resource = new KtbsJenaRoot(uri, model, holder);
		else if(clazz.equals(Base.class)) 
			resource = new KtbsJenaBase(uri, model, holder);
		else if(clazz.equals(StoredTrace.class)) 
			resource = new KtbsJenaStoredTrace(uri, model, holder);
		else if(clazz.equals(ComputedTrace.class)) 
			resource = new KtbsJenaComputedTrace(uri, model, holder);
		else if(clazz.equals(ObselType.class)) 
			resource = new KtbsJenaObselType(uri, model, holder);
		else if(clazz.equals(AttributeType.class)) 
			resource = new KtbsJenaAttributeType(uri, model, holder);
		else if(clazz.equals(RelationType.class)) 
			resource = new KtbsJenaRelationType(uri, model, holder);
		else if(clazz.equals(TraceModel.class)) 
			resource = new KtbsJenaTraceModel(uri, model, holder);
		else if(clazz.equals(Method.class)) 
			resource = new KtbsJenaMethod(uri, model, holder);
		else
			throw new KtbsResourceInstanciationException(clazz, "Unkown class");

		((KtbsJenaResource)resource).setType(rdfType);

		return clazz.cast(resource);
	}


	private KtbsRoot createKtbsRoot(String uri, InputStream stream, String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaRoot(uri, rdfModel, holder);
	}

	private Model createRdfModel(InputStream stream, String lang) {
		Model rdfModel = ModelFactory.createDefaultModel();
		rdfModel.read(stream, null, lang);
		return rdfModel;
	}

	private Base createBase(String uri, InputStream stream, String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaBase(uri, rdfModel, holder);
	}

	private StoredTrace createStoredTrace(String uri, InputStream stream,
			String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaStoredTrace(uri, rdfModel, holder);
	}

	private ComputedTrace createComputedTrace(String uri, InputStream stream,
			String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaComputedTrace(uri, rdfModel, holder);
	}

	private Obsel createObsel(String uri, Model rdfModel) {
		return new KtbsJenaObsel(uri,rdfModel, holder);
	}

	private RelationType createRelationType(String relationTypeUri, Model rdfModel) {
		return new KtbsJenaRelationType(relationTypeUri, rdfModel, holder);
	}

	private AttributeType createAttributeType(String attTypeUri, Model rdfModel) {
		return new KtbsJenaAttributeType(attTypeUri, rdfModel, holder);
	}

	private ObselType createObselType(String obsTypeUri, Model rdfModel) {
		return new KtbsJenaObselType(obsTypeUri, rdfModel, holder);
	}

	public <T extends KtbsResource> T createResource(String obsTypeUri, Class<T> clazz, Model rdfModel) {
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

	private TraceModel createTraceModel(String uri, InputStream stream,
			String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaTraceModel(uri, rdfModel, holder);
	}
	private Method createMethod(String uri, InputStream stream,
			String lang) {
		Model rdfModel = createRdfModel(stream, lang);
		return new KtbsJenaMethod(uri, rdfModel, holder);
	}
}
