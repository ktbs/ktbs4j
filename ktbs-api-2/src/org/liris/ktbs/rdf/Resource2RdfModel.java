package org.liris.ktbs.rdf;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.api.AttributePair;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.KtbsResource;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.MethodParameter;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.PropertyStatement;
import org.liris.ktbs.core.api.RelationStatement;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.Root;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.api.TransformationResource;
import org.liris.ktbs.core.api.WithDomainResource;
import org.liris.ktbs.core.api.WithRangeResource;
import org.liris.ktbs.serial.SerializationOptions;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

public class Resource2RdfModel {

	private KtbsResource resource;
	private Model model;
	private SerializationOptions options = new SerializationOptions();

	public Resource2RdfModel(KtbsResource resource) {
		super();
		this.resource = resource;
		this.model = ModelFactory.createDefaultModel();
	}

	public Resource2RdfModel(KtbsResource resource, SerializationOptions options) {
		this(resource);
		this.options = options;
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

	private void put(KtbsResource r) {
		/*
		 * Should never be called since there are specialized put/1 methods
		 * for each type of supported resource.
		 */
		throw new IllegalStateException("Unkown resource class: " + r.getClass().getCanonicalName());
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

	private void putResource(String uri, String pName, KtbsResource objectResource) {
		if(objectResource == null)
			return;
		else
			model.getResource(uri).addProperty(
					model.getProperty(pName), 
					model.getResource(objectResource.getUri()));
	}

	private void putGenericResource(KtbsResource r) {
		// Avoid cycles
		if(alreadyProcessed.contains(r.getUri()))
			return;
		alreadyProcessed.add(r.getUri());


		model.getResource(r.getUri()).addProperty(
				RDF.type, 
				model.getResource(KtbsUtils.getRDFType(r)));

		for(String label:KtbsUtils.toIterable(r.listLabels()))
			putLiteral(r.getUri(), RDFS.label.getURI(), label);

		for(PropertyStatement stmt:KtbsUtils.toIterable(r.listProperties()))
			putLiteral(r.getUri(), stmt.getPropertyName(), stmt.getPropertyValue());

	}

	//------------------------------------------------------------------------------
	// SERIALIZATION OPTIONS METHODS
	//------------------------------------------------------------------------------
	private boolean withLinkedResource() {
		return options.isWithLinkedResource();
	}

	private boolean withLinkedResourceURI() {
		return options.isWithLinkedResourceURI();
	}

	private boolean withLinkedResourceType() {
		return options.isWithLinkedResourceType();
	}

	private boolean withContainedResource() {
		return options.isWithContainedResource();
	}

	private boolean withContainedResourceURI() {
		return options.isWithContainedResourceURI();
	}

	private boolean withContainedResourceType() {
		return options.isWithContainedResourceType();
	}

	//------------------------------------------------------------------------------
	// SPECIALIZED METHODS
	//------------------------------------------------------------------------------
	private void put(Root root) {

		putGenericResource(root);

		if(withContainedResource()) {
			for(Base base:KtbsUtils.toIterable(root.listBases()))
				put(base);
		} else if(withContainedResourceURI()) {
			for(Base base:KtbsUtils.toIterable(root.listBases())) {
				putResource(root.getUri(), KtbsConstants.P_HAS_BASE, base);
				if(withContainedResourceType())
					putResource(base.getUri(), RDF.type.getURI(), KtbsConstants.BASE);
			}
		}
	}

	private void put(Base base) {
		putGenericResource(base);

		putLiteral(base.getUri(), KtbsConstants.P_HAS_OWNER, base.getOwner());

		putBaseChildren(base, base.listStoredTraces());
		putBaseChildren(base, base.listComputedTraces());
		putBaseChildren(base, base.listMethods());
		putBaseChildren(base, base.listTraceModels());
	}

	private void putBaseChildren(Base base, Iterator<? extends KtbsResource> it) {
		for(KtbsResource child:KtbsUtils.toIterable(it)) {
			if(withContainedResource())
				put(child);
			else if (withContainedResourceURI()) {
				model.getResource(base.getUri()).addProperty(
						model.getProperty(KtbsConstants.P_OWNS), 
						model.getResource(child.getUri()));
				if (withContainedResourceType()) 
					model.getResource(child.getUri()).addProperty(
							RDF.type, 
							model.getResource(KtbsUtils.getRDFType(child)));
			}
		}
	}

	private void putTraceModelChildren(TraceModel tm, Iterator<? extends KtbsResource> it) {
		for(KtbsResource child:KtbsUtils.toIterable(it)) {
			if(withContainedResource())
				put(child);
			else if (withContainedResourceURI() || withContainedResourceType()) 
					model.getResource(child.getUri()).addProperty(
							RDF.type, 
							model.getResource(KtbsUtils.getRDFType(child)));
		}
	}

	private void put(StoredTrace trace) {
		putGenericResource(trace);
		putTraceResource(trace);

		putLiteral(trace.getUri(), KtbsConstants.P_HAS_SUBJECT, trace.getDefaultSubject());
	}

	private void put(TraceModel traceModel) {
		putGenericResource(traceModel);
		
		putTraceModelChildren(traceModel, traceModel.listAttributeTypes());
		putTraceModelChildren(traceModel, traceModel.listRelationTypes());
		putTraceModelChildren(traceModel, traceModel.listObselTypes());
	}

	private void put(AttributeType attType) {
		putGenericResource(attType);
		putWithDomainResource(attType, KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN);
		putWithStringRangeResource(attType, KtbsConstants.P_HAS_ATTRIBUTE_RANGE);
	}
	
	private void putWithStringRangeResource(WithRangeResource<String> r, String rangePropName) {
		for(String s:KtbsUtils.toIterable(r.listRanges())) 
			model.getResource(((KtbsResource)r).getUri()).addLiteral(
					model.getProperty(rangePropName),
					model.getResource(s));
	}

	private void put(RelationType relType) {
		putGenericResource(relType);
		putWithDomainResource(relType, KtbsConstants.P_HAS_RELATION_DOMAIN);
		putWithRangeResource(relType, KtbsConstants.P_HAS_RELATION_RANGE);
	}

	private void putWithDomainResource(WithDomainResource<? extends KtbsResource> r, String domainPropName) {
		for(KtbsResource domain:KtbsUtils.toIterable(r.listDomains())) {
			if(withLinkedResource())
				put(domain);
			
			model.getResource(((KtbsResource)r).getUri()).addProperty(
					model.getProperty(domainPropName),
					model.getResource(domain.getUri()));
			
			// ignore other options
		}
	}

	private void putWithRangeResource(WithRangeResource<? extends KtbsResource> r , String rangePropName) {
		for(KtbsResource range:KtbsUtils.toIterable(r.listRanges())) {
			if(withLinkedResource())
				put(range);
			
			model.getResource(((KtbsResource)r).getUri()).addProperty(
					model.getProperty(rangePropName),
					model.getResource(range.getUri()));
			
			// ignore other options
		}
	}

	private void put(ComputedTrace trace) {
		putGenericResource(trace);
		putTraceResource(trace);
		putTransformationResource(trace, trace.getUri());

		if(withLinkedResource()) {
			put(trace.getMethod());
			for(Trace sourceTrace:KtbsUtils.toIterable(trace.listSourceTraces())) 
				put(trace);
		}

		if(withLinkedResourceURI()) {
			model.getResource(trace.getUri()).addProperty(
					model.getProperty(KtbsConstants.P_HAS_METHOD),
					model.getResource(trace.getMethod().getUri()));
			for(Trace sourceTrace:KtbsUtils.toIterable(trace.listSourceTraces())) 
				model.getResource(trace.getUri()).addProperty(
						model.getProperty(KtbsConstants.P_HAS_SOURCE),
						model.getResource(sourceTrace.getUri()));
		}
	}

	private void put(Method method) {
		putGenericResource(method);
		putTransformationResource(method, method.getUri());

		putResource(method.getUri(), KtbsConstants.P_INHERITS, method.getInheritedMethod());
		putLiteral(method.getUri(), KtbsConstants.P_HAS_ETAG, method.getETag());
	}

	private void put(Obsel obsel) {
		putGenericResource(obsel);

		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_BEGIN_DT, obsel.getBeginDT());
		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_END_DT, obsel.getEndDT());
		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_BEGIN, obsel.getBegin());
		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_END, obsel.getEnd());
		putLiteral(obsel.getUri(), KtbsConstants.P_HAS_SUBJECT, obsel.getSubject());

		for(AttributePair pair:KtbsUtils.toIterable(obsel.listAttributes())) {
			putLiteral(obsel.getUri(), pair.getAttributeType().getUri(), pair.getValue());
		}

		for(RelationStatement stmt:KtbsUtils.toIterable(obsel.listOutgoingRelations())) {
			if(withLinkedResource()) 
				put(stmt.getToObsel());

			if(withLinkedResourceType())
				model.getResource(stmt.getToObsel().getUri()).addProperty(
						RDF.type,
						model.getResource(stmt.getToObsel().getObselType().getUri())
				);

			if(withLinkedResourceURI())
				model.getResource(obsel.getUri()).addProperty(
						model.getProperty(stmt.getRelation().getUri()),
						model.getResource(stmt.getToObsel().getUri())
				);
		}

		for(RelationStatement stmt:KtbsUtils.toIterable(obsel.listIncomingRelations())) {
			if(withLinkedResource()) 
				put(stmt.getFromObsel());

			if(withLinkedResourceType())
				model.getResource(stmt.getFromObsel().getUri()).addProperty(
						RDF.type,
						model.getResource(stmt.getFromObsel().getObselType().getUri())
				);

			if(withLinkedResourceURI())
				model.getResource(stmt.getFromObsel().getUri()).addProperty(
						model.getProperty(stmt.getRelation().getUri()),
						model.getResource(obsel.getUri())
				);
		}
	}

	private void putTransformationResource(TransformationResource r, String uri) {
		for(MethodParameter param:KtbsUtils.toIterable(r.listMethodParameters())) 
			putLiteral(uri, KtbsConstants.P_HAS_PARAMETER, param.getName()+"="+param.getValue());
	}

	private void putTraceResource(Trace trace) {
		putLiteral(trace.getUri(), KtbsConstants.P_HAS_ORIGIN, trace.getOrigin());
		putLiteral(trace.getUri(), KtbsConstants.P_COMPLIES_WITH_MODEL, trace.getCompliantWithModel());

		if(withLinkedResource()) {
			put(trace.getTraceModel());
			addResourceListToModel(trace.listTransformedTraces());
		} 

		if(withLinkedResourceURI()) {
			model.getResource(trace.getUri()).addProperty(
					model.getProperty(KtbsConstants.P_HAS_MODEL), 
					model.getResource(trace.getTraceModel().getUri()));

			for(KtbsResource child:KtbsUtils.toIterable(trace.listTransformedTraces()))
				model.getResource(trace.getUri()).addProperty(
						model.getProperty(KtbsConstants.P_HAS_SOURCE), 
						model.getResource(child.getUri()));
		}

		if(withLinkedResourceType()) {
			model.getResource(trace.getTraceModel().getUri()).addProperty(
					RDF.type, 
					model.getResource(KtbsConstants.TRACE_MODEL));
			for(KtbsResource child:KtbsUtils.toIterable(trace.listTransformedTraces()))
				model.getResource(child.getUri()).addProperty(
						RDF.type, 
						model.getResource(KtbsUtils.getRDFType(child)));
		}

		if(withContainedResource()) {
			addResourceListToModel(trace.listObsels());
		} else if(withContainedResourceURI()) {
			for(Obsel obsel:KtbsUtils.toIterable(trace.listObsels())) {
				model.getResource(obsel.getUri()).addProperty(
						model.getProperty(KtbsConstants.P_HAS_TRACE), 
						model.getResource(trace.getUri()));

				if(withContainedResourceType()) {
					model.getResource(obsel.getUri()).addProperty(
							RDF.type, 
							model.getResource(obsel.getObselType().getUri()));
				}
			}
		}
	}


	// -------------------------------------------------------------------------
	// SHORTCUT METHOD
	// -------------------------------------------------------------------------
	private void addResourceListToModel(Iterator<? extends KtbsResource> list) {
		for(KtbsResource child:KtbsUtils.toIterable(list))
			put(child);
	}
}
