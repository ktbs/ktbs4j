package org.liris.ktbs.rdf;

import java.util.HashSet;
import java.util.Set;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.domain.interfaces.IAttributePair;
import org.liris.ktbs.core.domain.interfaces.IAttributeType;
import org.liris.ktbs.core.domain.interfaces.IBase;
import org.liris.ktbs.core.domain.interfaces.IComputedTrace;
import org.liris.ktbs.core.domain.interfaces.IKtbsResource;
import org.liris.ktbs.core.domain.interfaces.IMethod;
import org.liris.ktbs.core.domain.interfaces.IMethodParameter;
import org.liris.ktbs.core.domain.interfaces.IObsel;
import org.liris.ktbs.core.domain.interfaces.IObselType;
import org.liris.ktbs.core.domain.interfaces.IPropertyStatement;
import org.liris.ktbs.core.domain.interfaces.IRelationStatement;
import org.liris.ktbs.core.domain.interfaces.IRelationType;
import org.liris.ktbs.core.domain.interfaces.IRoot;
import org.liris.ktbs.core.domain.interfaces.IStoredTrace;
import org.liris.ktbs.core.domain.interfaces.ITrace;
import org.liris.ktbs.core.domain.interfaces.ITraceModel;
import org.liris.ktbs.core.domain.interfaces.WithParameters;
import org.liris.ktbs.serial.LinkAxis;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.serial.SerializationMode;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class Java2Rdf {

	private IKtbsResource resource;
	private Model model;
	private SerializationConfig config = new SerializationConfig();

	public Java2Rdf(IKtbsResource resource) {
		super();
		this.resource = resource;
		this.model = ModelFactory.createDefaultModel();
	}

	public Java2Rdf(IKtbsResource resource, SerializationConfig config) {
		this(resource);
		this.config = config;
	}

	/*
	 * A memory set to avoid cycles when serializing linked resources.
	 *  - trace cycle within ktbs:Base (source trace and transformed traces)
	 *  - obsel cycles within a ktbs:Trace (obsel relations)
	 */
	private Set<String> alreadyProcessed = new HashSet<String>();

	public Model getModel() {
		alreadyProcessed.clear();
		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("ktbs",KtbsConstants.NAMESPACE);
		model.setNsPrefix("xsd",XSD.getURI());
		model.setNsPrefix("rdfs",RDFS.getURI());
		put(resource);
		return model;
	}

	private void put(IKtbsResource r) {
		/*
		 * Should never be called since there are specialized put/1 methods
		 * for each type of supported resource.
		 */
		throw new IllegalStateException("Unkown resource class: " + r.getClass().getCanonicalName());
	}

	

	//------------------------------------------------------------------------------
	// PUT RESOURCES
	//------------------------------------------------------------------------------
	private void put(IRoot root) {
		putGenericResource(root);
		putChildren(root.getUri(), KtbsConstants.P_HAS_BASE, root.getBases(), false);

	}

	private void put(IBase base) {
		putGenericResource(base);

		putLiteral(base.getUri(), KtbsConstants.P_HAS_OWNER, base.getOwner());

		putChildren(base.getUri(), KtbsConstants.P_OWNS, base.getTraceModels(), false);
		putChildren(base.getUri(), KtbsConstants.P_OWNS, base.getStoredTraces(), false);
		putChildren(base.getUri(), KtbsConstants.P_OWNS, base.getComputedTraces(), false);
		putChildren(base.getUri(), KtbsConstants.P_OWNS, base.getMethods(), false);
	}

	private void put(IStoredTrace trace) {
		putGenericResource(trace);
		putTrace(trace);

		putLiteral(trace.getUri(), KtbsConstants.P_HAS_SUBJECT, trace.getDefaultSubject());
	}

	private void put(IComputedTrace trace) {
		putGenericResource(trace);
		putTrace(trace);
		putTransformationResource(trace, trace.getUri());
		putLinkedResourceSetSameType(trace.getUri(), KtbsConstants.P_HAS_SOURCE, trace.getSourceTraces(), false);
		putLinkedResource(trace.getUri(), KtbsConstants.P_HAS_METHOD, trace.getMethod(), false);
	}

	private void put(IMethod method) {
		putGenericResource(method);
		putTransformationResource(method, method.getUri());
		putResource(method.getUri(), KtbsConstants.P_INHERITS, method.getInherits());
		putLiteral(method.getUri(), KtbsConstants.P_HAS_ETAG, method.getEtag());
	}

	private void put(IObsel obsel) {
		putGenericResource(obsel);

		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_BEGIN_DT, obsel.getBeginDT());
		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_END_DT, obsel.getEndDT());
		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_BEGIN, obsel.getBegin());
		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_END, obsel.getEnd());
		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_SUBJECT, obsel.getSubject());

		// Other option : can be added as same type obsels
		putLinkedResourceSet(obsel.getUri(), KtbsConstants.P_HAS_SOURCE_OBSEL, obsel.getSourceObsels(), false);

		for(IAttributePair pair:obsel.getAttributePairs()) 
			putLiteral(obsel.getUri(), pair.getAttributeType().getUri(), pair.getValue());

		for(IRelationStatement stmt:obsel.getOutgoingRelations())
			putLinkedResourceSameType(obsel.getUri(), stmt.getRelation().getUri(), stmt.getToObsel(), false);

		for(IRelationStatement stmt:obsel.getIncomingRelations()) 
			putLinkedResourceSameType(obsel.getUri(), stmt.getRelation().getUri(), stmt.getFromObsel(), true);
	}

	private void putTransformationResource(WithParameters r, String uri) {
		for(IMethodParameter param:r.getMethodParameters()) 
			putLiteral(uri, KtbsConstants.P_HAS_PARAMETER, param.getName()+"="+param.getValue());
	}
	
	private void put(ITraceModel traceModel) {
		putGenericResource(traceModel);

		putChildren(traceModel.getUri(), null, traceModel.getAttributeTypes(), false);
		putChildren(traceModel.getUri(), null, traceModel.getRelationTypes(), false);
		putChildren(traceModel.getUri(), null, traceModel.getObselTypes(), false);
	}

	private void put(IAttributeType attType) {
		putGenericResource(attType);
		putLinkedResourceSet(attType.getUri(), KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN, attType.getDomains(), false);
		putLiteralSet(attType.getUri(), KtbsConstants.P_HAS_ATTRIBUTE_RANGE, attType.getRanges());
	}

	private void put(IObselType obsType) {
		putGenericResource(obsType);
		putLinkedResourceSetSameType(obsType.getUri(), KtbsConstants.P_HAS_SUPER_OBSEL_TYPE, obsType.getSuperObselTypes(), false);
	}
	
	private void put(IRelationType relType) {
		putGenericResource(relType);
		putLinkedResourceSet(relType.getUri(), KtbsConstants.P_HAS_RELATION_DOMAIN, relType.getDomains(), false);
		putLinkedResourceSet(relType.getUri(), KtbsConstants.P_HAS_RELATION_RANGE, relType.getRanges(), false);
	}

	private void putTrace(ITrace trace) {
		putLiteral(trace.getUri(), KtbsConstants.P_HAS_ORIGIN, trace.getOrigin());
		putLiteral(trace.getUri(), KtbsConstants.P_COMPLIES_WITH_MODEL, trace.getCompliesWithModel());
		putLinkedResourceSetSameType(trace.getUri(), KtbsConstants.P_HAS_SOURCE, trace.getTransformedTraces(), true);
		putLinkedResource(trace.getUri(), KtbsConstants.P_HAS_MODEL, trace.getTraceModel(), false);
		putChildren(trace.getUri(), KtbsConstants.P_HAS_TRACE, trace.getObsels(), true);
	}
	
	private void putGenericResource(IKtbsResource r) {
		// Avoid cycles
		if(alreadyProcessed.contains(r.getUri()))
			return;
		alreadyProcessed.add(r.getUri());


		putRdfType(r.getUri(), r.getTypeUri());
		putLiteralSet(r.getUri(), RDFS.label.getURI(), r.getLabels());

		for(IPropertyStatement stmt:r.getProperties())
			putLiteral(r.getUri(), stmt.getProperty(), stmt.getValue());
	}

	//------------------------------------------------------------------------------
	// HELPER METHODS
	//------------------------------------------------------------------------------
	/*
	 * Put a literal in a model if the value is not null
	 */
	private void putResource(String uri, String pName, String objectUri) {
		if(objectUri == null)
			return;
		model.getResource(uri).addProperty(
				model.getProperty(pName), 
				model.getResource(objectUri));
	}

	private void putLiteralSet(String uri, String pName,
			Set<?> literals) {
		for(Object literal:literals)
			putLiteral(uri, pName, literal);
	}

	private <T extends IKtbsResource> void putLinkedResourceSetSameType(String uri, String pName, Set<T> set, boolean inverse) {
		for(T r:set)
			putLinkedResourceSameType(uri, pName, r, inverse);
	}

	private <T extends IKtbsResource> void putLinkedResourceSet(String uri, String pName, Set<T> set, boolean inverse) {
		for(T r:set)
			putLinkedResource(uri, pName, r, inverse);
	}

	private <T extends IKtbsResource> void putLinkedResourceSameType(String uri, String pName, T r, boolean inverse) {
		putLinkedResourceWRTAxis(uri, pName, r, inverse, LinkAxis.LINKED_SAME_TYPE);
	}

	private <T extends IKtbsResource> void putLinkedResource(String uri, String pName, T r, boolean inverse) {
		putLinkedResourceWRTAxis(uri, pName, r, inverse, LinkAxis.LINKED);
	}

	private <T extends IKtbsResource> void putLinkedResourceWRTAxis(String uri, String pName, T r,
			boolean inverse, LinkAxis axis) {
		if(config.getMode(axis) == SerializationMode.NOTHING) {
			return;
		} else if(config.getMode(axis) == SerializationMode.CASCADE) {
			putProperty(uri, pName, r.getUri(), inverse);
			put(r);
		} else if(config.getMode(axis) == SerializationMode.URI) {
			putProperty(uri, pName, r.getUri(), inverse);
		} else if(config.getMode(axis) == SerializationMode.URI_AND_TYPE) {
			putProperty(uri, pName, r.getUri(), inverse);
			putRdfType(r.getUri(), r.getTypeUri());
		} else if(config.getMode(axis) == SerializationMode.POJO_PROPERTY) 
			throw new UnsupportedOperationException("Not yet supported");
	}
	
	private <T extends IKtbsResource> void putChildren(String parentUri, String parentChildProperty, Set<T> children, boolean inverse) {
		if(config.getChildMode() == SerializationMode.NOTHING) {
			return;
		} else if(config.getChildMode() == SerializationMode.CASCADE) {
			for(T child:children) {
				putProperty(parentUri, parentChildProperty, child.getUri(), inverse);
				put(child);
			}
		} else if(config.getChildMode() == SerializationMode.URI) {
			for(T child:children)
				putProperty(parentUri, parentChildProperty, child.getUri(), inverse);
		} else if(config.getChildMode() == SerializationMode.URI_AND_TYPE) {
			for(T child:children) {
				putProperty(parentUri, parentChildProperty, child.getUri(), inverse);
				putRdfType(child.getUri(), child.getTypeUri());
			}
		} else if(config.getChildMode() == SerializationMode.POJO_PROPERTY) 
			throw new UnsupportedOperationException("Not yet supported");
	}
	
	/*
	 * Put a literal in a model if it exists
	 */
	private void putLiteral(String uri, String pName, Object value) {
		if(value == null)
			return;
		model.getResource(uri).addLiteral(
				model.getProperty(pName), 
				value);
	}
	
	private void putRdfType(String subjectUri, String typeUri) {
		putProperty(subjectUri, RDF.type.getURI(), typeUri, false);
	}

	private void putProperty(String subjectUri, String pName,
			String objectUri, boolean inverse) {
		if(pName == null || subjectUri == null || objectUri == null)
			return;
		if(inverse) {
			model.getResource(objectUri).addProperty(
					model.getProperty(pName), 
					model.getResource(subjectUri));
		} else {
			model.getResource(subjectUri).addProperty(
					model.getProperty(pName), 
					model.getResource(objectUri));
		}
	}
}
