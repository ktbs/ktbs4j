package org.liris.ktbs.serial.rdf;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.liris.ktbs.client.KtbsConstants;
import org.liris.ktbs.domain.interfaces.IAttributePair;
import org.liris.ktbs.domain.interfaces.IAttributeType;
import org.liris.ktbs.domain.interfaces.IBase;
import org.liris.ktbs.domain.interfaces.IComputedTrace;
import org.liris.ktbs.domain.interfaces.IKtbsResource;
import org.liris.ktbs.domain.interfaces.IMethod;
import org.liris.ktbs.domain.interfaces.IMethodParameter;
import org.liris.ktbs.domain.interfaces.IObsel;
import org.liris.ktbs.domain.interfaces.IObselType;
import org.liris.ktbs.domain.interfaces.IPropertyStatement;
import org.liris.ktbs.domain.interfaces.IRelationStatement;
import org.liris.ktbs.domain.interfaces.IRelationType;
import org.liris.ktbs.domain.interfaces.IRoot;
import org.liris.ktbs.domain.interfaces.IStoredTrace;
import org.liris.ktbs.domain.interfaces.ITrace;
import org.liris.ktbs.domain.interfaces.ITraceModel;
import org.liris.ktbs.domain.interfaces.WithParameters;
import org.liris.ktbs.serial.LinkAxis;
import org.liris.ktbs.serial.SerializationConfig;
import org.liris.ktbs.serial.SerializationMode;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.impl.LiteralImpl;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class Java2Rdf {

	private Model model;
	private SerializationConfig config = new SerializationConfig();

	public void setConfig(SerializationConfig config) {
		this.config = config;
	}

	public Java2Rdf(SerializationConfig config) {
		this.model = ModelFactory.createDefaultModel();
		this.config = config;
	}

	/*
	 * A memory set to avoid cycles when serializing linked resources.
	 *  - trace cycle within ktbs:Base (source trace and transformed traces)
	 *  - obsel cycles within a ktbs:Trace (obsel relations)
	 */
	private Set<Object> alreadyProcessed = new HashSet<Object>();

	public Model getModel(IKtbsResource resource) {
		initModel();
		put(resource);
		return model;
	}

	private Map<Object, Resource> jenaResources = new HashMap<Object, Resource>();

	/*
	 * Get the jena resource associated to the ktsb resource
	 * and creates one if none exists.
	 * 
	 * Supports anonym Ktbs resources (i.e. KTBS resources with null uris)
	 */
	private Resource getJenaResource(IKtbsResource resource) {
		if(resource == null)
			throw new IllegalStateException("Should never be called on a null resource");
		if(!jenaResources.containsKey(resource)) {
			Resource jenaResource = resource.getUri()==null?
					model.createResource():
						model.createResource(resource.getUri());
					jenaResources.put(resource, jenaResource);
		}
		return jenaResources.get(resource);
	}

	private void initModel() {
		alreadyProcessed.clear();
		jenaResources.clear();
		model = ModelFactory.createDefaultModel();
		model.setNsPrefix("ktbs",KtbsConstants.NAMESPACE_KTBS);
		model.setNsPrefix("xsd",XSD.getURI());
		model.setNsPrefix("rdfs",RDFS.getURI());
	}

	public Model getModel(Collection<? extends IKtbsResource> resourceSet) {
		initModel();
		for(IKtbsResource resource:resourceSet)
			put(resource);
		return model;
	}

	private void put(IKtbsResource r) {
		if(IRoot.class.isAssignableFrom(r.getClass())) 
			putRoot((IRoot) r);
		else if(IBase.class.isAssignableFrom(r.getClass())) 
			putBase((IBase) r);
		else if(IStoredTrace.class.isAssignableFrom(r.getClass())) 
			putStoredTrace((IStoredTrace) r);
		else if(IComputedTrace.class.isAssignableFrom(r.getClass())) 
			putComputedTrace((IComputedTrace) r);
		else if(IObsel.class.isAssignableFrom(r.getClass())) 
			putObsel((IObsel) r);
		else if(IObselType.class.isAssignableFrom(r.getClass())) 
			putObselType((IObselType) r);
		else if(IAttributeType.class.isAssignableFrom(r.getClass())) 
			putAttributeType((IAttributeType) r);
		else if(IRelationType.class.isAssignableFrom(r.getClass())) 
			putRelationType((IRelationType) r);
		else if(ITraceModel.class.isAssignableFrom(r.getClass())) 
			putTraceModel((ITraceModel) r);
		else if(IMethod.class.isAssignableFrom(r.getClass())) 
			putMethod((IMethod) r);
		else if(ITrace.class.isAssignableFrom(r.getClass())) 
			putTrace((ITrace) r);
		else 
			throw new IllegalStateException("Should never be invoked since specified method are defined for each resource");
	}



	//------------------------------------------------------------------------------
	// PUT RESOURCES
	//------------------------------------------------------------------------------
	private void putRoot(IRoot root) {
		// Avoid cycles
		if(isAlreadyProcessed(root))
			return;

		putGenericResource(root);
		putChildren(root, KtbsConstants.P_HAS_BASE, root.getBases(), false);

	}

	private void putBase(IBase base) {
		// Avoid cycles
		if(isAlreadyProcessed(base))
			return;

		putGenericResource(base);

		putLiteral(base, KtbsConstants.P_HAS_OWNER, base.getOwner());

		putChildren(base, KtbsConstants.P_CONTAINS, base.getTraceModels(), false);
		putChildren(base, KtbsConstants.P_CONTAINS, base.getStoredTraces(), false);
		putChildren(base, KtbsConstants.P_CONTAINS, base.getComputedTraces(), false);
		putChildren(base, KtbsConstants.P_CONTAINS, base.getMethods(), false);

		putParent(base, KtbsConstants.P_HAS_BASE, false);
	}

	/*
	 * Invole this method with parentChildPName when no parent relation is supposed to be done
	 */
	private void putParent(IKtbsResource child, String parentChildPName, boolean inverse) {
		if(child.getParentUri() == null || parentChildPName == null)
			return;
		SerializationMode mode = config.getMode(LinkAxis.PARENT);
		if(mode == SerializationMode.URI) {
			/*
			 * Only the uri is required. The parent resource may be null 
			 * and the parent uri still required in the trace model.
			 */
			String parentUri = child.getParentUri();
			if(parentUri == null)
				return;
			if(inverse) {
				getJenaResource(child).addProperty(model.getProperty(parentChildPName), model.getResource(parentUri));
			} else {
				model.getResource(parentUri).addProperty(model.getProperty(parentChildPName), getJenaResource(child));
			}
		} else if(mode == SerializationMode.NOTHING) {
			// do nothing
		} else if(mode == SerializationMode.CASCADE) {
			putResource(child.getParentResource(), parentChildPName, child, inverse);
			put(child.getParentResource());
		} else if(mode == SerializationMode.POJO_PROPERTY) {
			throw new UnsupportedOperationException("Not yet supported");
		}
	}

	private void putStoredTrace(IStoredTrace trace) {
		// Avoid cycles
		if(isAlreadyProcessed(trace))
			return;

		putGenericResource(trace);
		putTrace(trace);

		putLiteral(trace, KtbsConstants.P_HAS_SUBJECT, trace.getDefaultSubject());
	}

	private void putComputedTrace(IComputedTrace trace) {
		// Avoid cycles
		if(isAlreadyProcessed(trace))
			return;

		putGenericResource(trace);
		putTrace(trace);
		putTransformationResource(trace);
		putLinkedResourceSetSameType(trace, KtbsConstants.P_HAS_SOURCE, trace.getSourceTraces(), false);
		putLinkedResource(trace, KtbsConstants.P_HAS_METHOD, trace.getMethod(), false);
	}

	private void putMethod(IMethod method) {
		// Avoid cycles
		if(isAlreadyProcessed(method))
			return;

		putGenericResource(method);
		putTransformationResource(method);
		putStringAsResource(method, KtbsConstants.P_INHERITS, method.getInherits());
		putLiteral(method, KtbsConstants.P_HAS_ETAG, method.getEtag());

		putParent(method, KtbsConstants.P_CONTAINS, false);
	}

	private void putStringAsResource(IKtbsResource resource, String pName,
			String objectResourceUri) {
		getJenaResource(resource).addProperty(model.getProperty(pName), model.getResource(objectResourceUri));
	}

	private void putObsel(IObsel obsel) {
		// Avoid cycles
		if(isAlreadyProcessed(obsel))
			return;


		putGenericResource(obsel);

		putXSDDateLiteral(obsel, KtbsConstants.P_HAS_BEGIN_DT, obsel.getBeginDT());
		putXSDDateLiteral(obsel, KtbsConstants.P_HAS_END_DT, obsel.getEndDT());
		putLiteral(obsel, KtbsConstants.P_HAS_BEGIN, obsel.getBegin());
		putLiteral(obsel, KtbsConstants.P_HAS_END, obsel.getEnd());
		putLiteral(obsel, KtbsConstants.P_HAS_SUBJECT, obsel.getSubject());

		// Other option : can be added as same type obsels
		putLinkedResourceSet(obsel, KtbsConstants.P_HAS_SOURCE_OBSEL, obsel.getSourceObsels(), false);

		for(IAttributePair pair:obsel.getAttributePairs()) {
			if(config.getLinkMode() == SerializationMode.NOTHING) {
				// Ok, do nothing
			} else if(config.getLinkMode() == SerializationMode.POJO_PROPERTY) {
				throw new UnsupportedOperationException("Not yet implemented");
			} else if(config.getLinkMode() == SerializationMode.URI) {
				// Ok, do nothing
			} else if(config.getLinkMode() == SerializationMode.URI_AND_TYPE) {
				putRdfType(pair.getAttributeType(), KtbsConstants.ATTRIBUTE_TYPE);
			} else if(config.getLinkMode() == SerializationMode.CASCADE) {
				putAttributeType(pair.getAttributeType());
			}
			String attUri = pair.getAttributeType().getUri();
			putLiteral(obsel, attUri, pair.getValue());
		}

		for(IRelationStatement stmt:obsel.getOutgoingRelations()) {
			putRelation(stmt);
			putLinkedResourceSameType(obsel, stmt.getRelation().getUri(), stmt.getToObsel(), false);
		}

		for(IRelationStatement stmt:obsel.getIncomingRelations()) {
			putRelation(stmt);

			// put the connected obsel 
			// (cannot use putLinkedResourceSameType for that, order is reverse)
			if(config.getLinkMode() == SerializationMode.NOTHING) {
				// Ok, do nothing
			} else if(config.getLinkMode() == SerializationMode.POJO_PROPERTY) {
				throw new UnsupportedOperationException("Not yet implemented");
			} else if(config.getLinkMode() == SerializationMode.URI) {
				// Ok, do nothing (the obsel uri will be put later together with the triple)
			} else if(config.getLinkMode() == SerializationMode.URI_AND_TYPE) {
				putRdfType(stmt.getFromObsel(), stmt.getFromObsel().getObselType().getUri());
			} else if(config.getLinkMode() == SerializationMode.CASCADE) {
				putObsel(stmt.getFromObsel());
			}

			getJenaResource(stmt.getFromObsel()).addProperty(
					model.getProperty(stmt.getRelation().getUri()),
					getJenaResource(obsel));
		}

		putParent(obsel, KtbsConstants.P_HAS_TRACE, true);
	}

	private void putXSDDateLiteral(IKtbsResource resource, String pName, String value) {
		if(value == null)
			return;
		Literal literal;
		try {
			KtbsUtils.parseXsdDate(value);
			literal = model.createTypedLiteral(value, XSDDatatype.XSDdateTime);
		} catch(Exception e) {
			literal = model.createLiteral(value);
		}
		
		getJenaResource(resource).addLiteral(
				model.getProperty(pName), 
				literal);
	}

	private void putRelation(IRelationStatement stmt) {
		if(config.getLinkMode() == SerializationMode.NOTHING) {
			// Ok, do nothing
		} else if(config.getLinkMode() == SerializationMode.POJO_PROPERTY) {
			throw new UnsupportedOperationException("Not yet implemented");
		} else if(config.getLinkMode() == SerializationMode.URI) {
			// Ok, do nothing
		} else if(config.getLinkMode() == SerializationMode.URI_AND_TYPE) {
			putRdfType(stmt.getRelation(), KtbsConstants.RELATION_TYPE);
		} else if(config.getLinkMode() == SerializationMode.CASCADE) {
			putRelationType(stmt.getRelation());
		}
	}

	private void putTransformationResource(WithParameters r) {
		for(IMethodParameter param:r.getMethodParameters()) 
			putLiteral((IKtbsResource)r, KtbsConstants.P_HAS_PARAMETER, param.getName()+"="+param.getValue());
	}

	private void putTraceModel(ITraceModel traceModel) {
		// Avoid cycles
		if(isAlreadyProcessed(traceModel))
			return;

		putGenericResource(traceModel);

		putChildren(traceModel, null, traceModel.getAttributeTypes(), false);
		putChildren(traceModel, null, traceModel.getRelationTypes(), false);
		putChildren(traceModel, null, traceModel.getObselTypes(), false);

		putParent(traceModel, KtbsConstants.P_CONTAINS, false);
	}

	private void putAttributeType(IAttributeType attType) {
		// Avoid cycles
		if(isAlreadyProcessed(attType))
			return;


		putGenericResource(attType);
		putLinkedResourceSet(attType, KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN, attType.getDomains(), false);
		putLiteralSet(attType, KtbsConstants.P_HAS_ATTRIBUTE_RANGE, attType.getRanges());

		putParent(attType, null, false);
	}

	private boolean isAlreadyProcessed(IKtbsResource attType) {
		return alreadyProcessed.contains(attType);
	}

	private void putObselType(IObselType obsType) {
		// Avoid cycles
		if(isAlreadyProcessed(obsType))
			return;

		putGenericResource(obsType);
		putLinkedResourceSetSameType(obsType, KtbsConstants.P_HAS_SUPER_OBSEL_TYPE, obsType.getSuperObselTypes(), false);

		putParent(obsType, null, false);
	}

	private void putRelationType(IRelationType relType) {
		// Avoid cycles
		if(isAlreadyProcessed(relType))
			return;

		putGenericResource(relType);
		putLinkedResourceSet(relType, KtbsConstants.P_HAS_RELATION_DOMAIN, relType.getDomains(), false);
		putLinkedResourceSet(relType, KtbsConstants.P_HAS_RELATION_RANGE, relType.getRanges(), false);

		putParent(relType, null, false);

	}

	private void putTrace(ITrace trace) {
		
		putXSDDateLiteral(trace, KtbsConstants.P_HAS_TRACE_BEGIN_DT, trace.getTraceBeginDT());
		putXSDDateLiteral(trace, KtbsConstants.P_HAS_TRACE_END_DT, trace.getTraceEndDT());
		putLiteral(trace, KtbsConstants.P_HAS_TRACE_BEGIN, trace.getTraceBegin());
		putLiteral(trace, KtbsConstants.P_HAS_TRACE_END, trace.getTraceEnd());
		
		putXSDDateLiteral(trace, KtbsConstants.P_HAS_ORIGIN, trace.getOrigin());
		putLiteral(trace, KtbsConstants.P_COMPLIES_WITH_MODEL, trace.getCompliesWithModel());
		putLinkedResourceSetSameType(trace, KtbsConstants.P_HAS_SOURCE, trace.getTransformedTraces(), true);
		putLinkedResource(trace, KtbsConstants.P_HAS_MODEL, trace.getTraceModel(), false);
		putChildren(trace, KtbsConstants.P_HAS_TRACE, trace.getObsels(), true);

		putParent(trace, KtbsConstants.P_CONTAINS, false);
	}

	private void putGenericResource(IKtbsResource r) {

		alreadyProcessed.add(r);


		String typeUri = r.getTypeUri();
		if(typeUri != null)
			putRdfType(r, typeUri);
		
		putLiteralSet(r, RDFS.label.getURI(), r.getLabels());

		for(IPropertyStatement stmt:r.getProperties())
			putLiteral(r, stmt.getProperty(), stmt.getValue());
	}

	//------------------------------------------------------------------------------
	// HELPER METHODS
	//------------------------------------------------------------------------------
	/*
	 * Put a literal in a model if the value is not null
	 */
	private void putResource(IKtbsResource resource, String pName, IKtbsResource objectResource) {
		putResource(resource, pName, objectResource, false);
	}

	private void putResource(IKtbsResource subjectResource, String pName, IKtbsResource objectResource, boolean inverse) {
		if(pName == null)
			return;

		if(inverse) {
			getJenaResource(objectResource).addProperty(
					model.getProperty(pName), 
					getJenaResource(subjectResource));

		} else {
			getJenaResource(subjectResource).addProperty(
					model.getProperty(pName), 
					getJenaResource(objectResource));
		}
	}

	private void putLiteralSet(IKtbsResource resource, String pName,
			Set<?> literals) {
		for(Object literal:literals)
			putLiteral(resource, pName, literal);
	}

	private <T extends IKtbsResource> void putLinkedResourceSetSameType(IKtbsResource resource, String pName, Set<T> set, boolean inverse) {
		for(T r:set)
			putLinkedResourceSameType(resource, pName, r, inverse);
	}

	private <T extends IKtbsResource> void putLinkedResourceSet(IKtbsResource resource, String pName, Set<T> set, boolean inverse) {
		for(T r:set)
			putLinkedResource(resource, pName, r, inverse);
	}

	private <T extends IKtbsResource> void putLinkedResourceSameType(IKtbsResource resource, String pName, T r, boolean inverse) {
		putLinkedResourceWRTAxis(resource, pName, r, inverse, LinkAxis.LINKED_SAME_TYPE);
	}

	private <T extends IKtbsResource> void putLinkedResource(IKtbsResource resource, String pName, T r, boolean inverse) {
		putLinkedResourceWRTAxis(resource, pName, r, inverse, LinkAxis.LINKED);
	}

	private <T extends IKtbsResource> void putLinkedResourceWRTAxis(IKtbsResource resource, String pName, T r,
			boolean inverse, LinkAxis axis) {
		if(r == null)
			return;

		if(config.getMode(axis) == SerializationMode.NOTHING) {
			return;
		} else if(config.getMode(axis) == SerializationMode.CASCADE) {
			putResource(resource, pName, r, inverse);
			put(r);
		} else if(config.getMode(axis) == SerializationMode.URI) {
			putResource(resource, pName, r, inverse);
		} else if(config.getMode(axis) == SerializationMode.URI_AND_TYPE) {
			putResource(resource, pName, r, inverse);
			putRdfType(r, r.getTypeUri());
		} else if(config.getMode(axis) == SerializationMode.POJO_PROPERTY) 
			throw new UnsupportedOperationException("Not yet supported");
	}

	private <T extends IKtbsResource> void putChildren(IKtbsResource parent, String parentChildProperty, Set<T> children, boolean inverse) {
		if(config.getChildMode() == SerializationMode.NOTHING) {
			return;
		} else if(config.getChildMode() == SerializationMode.CASCADE) {
			for(T child:children) {
				putResource(parent, parentChildProperty, child, inverse);
				put(child);
			}
		} else if(config.getChildMode() == SerializationMode.URI) {
			for(T child:children)
				putResource(parent, parentChildProperty, child, inverse);
		} else if(config.getChildMode() == SerializationMode.URI_AND_TYPE) {
			for(T child:children) {
				putResource(parent, parentChildProperty, child, inverse);
				putRdfType(child, child.getTypeUri());
			}
		} else if(config.getChildMode() == SerializationMode.POJO_PROPERTY) 
			throw new UnsupportedOperationException("Not yet supported");
	}

	/*
	 * Put a literal in a model if it exists
	 */
	private void putLiteral(IKtbsResource resource, String pName, Object value) {
		if(value == null)
			return;
		getJenaResource(resource).addLiteral(
				model.getProperty(pName), 
				value);
	}

	/*
	 * Put a literal in a model if it exists - ensure that strings are converted
	 * to *plain* literals instead of explicitly typing them with xsd:string.
	 */
	private void putLiteral(IKtbsResource resource, String pName, String value) {
		if(value == null)
			return;
		Literal lit = model.createLiteral(value);
		getJenaResource(resource).addLiteral(
				model.getProperty(pName), 
				lit);
	}

	private void putRdfType(IKtbsResource resource, String typeUri) {
		getJenaResource(resource).addProperty(RDF.type, model.getProperty(typeUri));
	}
}
