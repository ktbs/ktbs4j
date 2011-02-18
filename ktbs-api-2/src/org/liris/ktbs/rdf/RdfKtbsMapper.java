package org.liris.ktbs.rdf;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.liris.ktbs.core.KtbsConstants;
import org.liris.ktbs.core.api.AttributeType;
import org.liris.ktbs.core.api.Base;
import org.liris.ktbs.core.api.ComputedTrace;
import org.liris.ktbs.core.api.Method;
import org.liris.ktbs.core.api.Obsel;
import org.liris.ktbs.core.api.RelationType;
import org.liris.ktbs.core.api.Root;
import org.liris.ktbs.core.api.StoredTrace;
import org.liris.ktbs.core.api.Trace;
import org.liris.ktbs.core.api.TraceModel;
import org.liris.ktbs.core.api.share.AttributePair;
import org.liris.ktbs.core.api.share.KtbsResource;
import org.liris.ktbs.core.api.share.MethodParameter;
import org.liris.ktbs.core.api.share.PropertyStatement;
import org.liris.ktbs.core.api.share.RelationStatement;
import org.liris.ktbs.core.api.share.TransformationResource;
import org.liris.ktbs.core.api.share.WithDomainResource;
import org.liris.ktbs.core.api.share.WithRangeResource;
import org.liris.ktbs.serial.SerializationOptions;
import org.liris.ktbs.utils.KtbsUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

@SuppressWarnings("unused")
public class RdfKtbsMapper {

	private KtbsResource resource;
	private Model model;
	private SerializationOptions options = new SerializationOptions();

	public RdfKtbsMapper(KtbsResource resource) {
		super();
		this.resource = resource;
		this.model = ModelFactory.createDefaultModel();
	}

	public RdfKtbsMapper(KtbsResource resource, SerializationOptions options) {
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
					model.getResource(objectResource.getURI()));
	}

	private void putGenericResource(KtbsResource r) {
		// Avoid cycles
		if(alreadyProcessed.contains(r.getURI()))
			return;
		alreadyProcessed.add(r.getURI());


		model.getResource(r.getURI()).addProperty(
				RDF.type, 
				model.getResource(KtbsUtils.getRDFType(r)));

		for(String label:KtbsUtils.toIterable(r.listLabels()))
			putLiteral(r.getURI(), RDFS.label.getURI(), label);

		for(PropertyStatement stmt:KtbsUtils.toIterable(r.listProperties()))
			putLiteral(r.getURI(), stmt.getPropertyName(), stmt.getPropertyValue());

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
				putResource(root.getURI(), KtbsConstants.P_HAS_BASE, base);
				if(withContainedResourceType())
					putResource(base.getURI(), RDF.type.getURI(), KtbsConstants.BASE);
			}
		}
	}

	private void put(Base base) {
		putGenericResource(base);

		putLiteral(base.getURI(), KtbsConstants.P_HAS_OWNER, base.getOwner());

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
				model.getResource(base.getURI()).addProperty(
						model.getProperty(KtbsConstants.P_OWNS), 
						model.getResource(child.getURI()));
				if (withContainedResourceType()) 
					model.getResource(child.getURI()).addProperty(
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
					model.getResource(child.getURI()).addProperty(
							RDF.type, 
							model.getResource(KtbsUtils.getRDFType(child)));
		}
	}

	private void put(StoredTrace trace) {
		putGenericResource(trace);
		putTraceResource(trace);

		putLiteral(trace.getURI(), KtbsConstants.P_HAS_SUBJECT, trace.getDefaultSubject());
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
			model.getResource(((KtbsResource)r).getURI()).addLiteral(
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
			
			model.getResource(((KtbsResource)r).getURI()).addProperty(
					model.getProperty(domainPropName),
					model.getResource(domain.getURI()));
			
			// ignore other options
		}
	}

	private void putWithRangeResource(WithRangeResource<? extends KtbsResource> r , String rangePropName) {
		for(KtbsResource range:KtbsUtils.toIterable(r.listRanges())) {
			if(withLinkedResource())
				put(range);
			
			model.getResource(((KtbsResource)r).getURI()).addProperty(
					model.getProperty(rangePropName),
					model.getResource(range.getURI()));
			
			// ignore other options
		}
	}

	private void put(ComputedTrace trace) {
		putGenericResource(trace);
		putTraceResource(trace);
		putTransformationResource(trace, trace.getURI());

		if(withLinkedResource()) {
			put(trace.getMethod());
			for(Trace sourceTrace:KtbsUtils.toIterable(trace.listSourceTraces())) 
				put(trace);
		}

		if(withLinkedResourceURI()) {
			model.getResource(trace.getURI()).addProperty(
					model.getProperty(KtbsConstants.P_HAS_METHOD),
					model.getResource(trace.getMethod().getURI()));
			for(Trace sourceTrace:KtbsUtils.toIterable(trace.listSourceTraces())) 
				model.getResource(trace.getURI()).addProperty(
						model.getProperty(KtbsConstants.P_HAS_SOURCE),
						model.getResource(sourceTrace.getURI()));
		}
	}

	private void put(Method method) {
		putGenericResource(method);
		putTransformationResource(method, method.getURI());

		putResource(method.getURI(), KtbsConstants.P_INHERITS, method.getInheritedMethod());
		putLiteral(method.getURI(), KtbsConstants.P_HAS_ETAG, method.getETag());
	}

	private void put(Obsel obsel) {
		putGenericResource(obsel);

		putLiteral(obsel.getURI(), KtbsConstants.P_HAS_BEGIN_DT, obsel.getBeginDT());
		putLiteral(obsel.getURI(), KtbsConstants.P_HAS_END_DT, obsel.getEndDT());
		putLiteral(obsel.getURI(), KtbsConstants.P_HAS_BEGIN, obsel.getBegin());
		putLiteral(obsel.getURI(), KtbsConstants.P_HAS_END, obsel.getEnd());
		putLiteral(obsel.getURI(), KtbsConstants.P_HAS_SUBJECT, obsel.getSubject());

		for(AttributePair pair:KtbsUtils.toIterable(obsel.listAttributes())) {
			putLiteral(obsel.getURI(), pair.getAttributeType().getURI(), pair.getValue());
		}

		for(RelationStatement stmt:KtbsUtils.toIterable(obsel.listOutgoingRelations())) {
			if(withLinkedResource()) 
				put(stmt.getToObsel());

			if(withLinkedResourceType())
				model.getResource(stmt.getToObsel().getURI()).addProperty(
						RDF.type,
						model.getResource(stmt.getToObsel().getObselType().getURI())
				);

			if(withLinkedResourceURI())
				model.getResource(obsel.getURI()).addProperty(
						model.getProperty(stmt.getRelation().getURI()),
						model.getResource(stmt.getToObsel().getURI())
				);
		}

		for(RelationStatement stmt:KtbsUtils.toIterable(obsel.listIncomingRelations())) {
			if(withLinkedResource()) 
				put(stmt.getFromObsel());

			if(withLinkedResourceType())
				model.getResource(stmt.getFromObsel().getURI()).addProperty(
						RDF.type,
						model.getResource(stmt.getFromObsel().getObselType().getURI())
				);

			if(withLinkedResourceURI())
				model.getResource(stmt.getFromObsel().getURI()).addProperty(
						model.getProperty(stmt.getRelation().getURI()),
						model.getResource(obsel.getURI())
				);
		}
	}

	private void putTransformationResource(TransformationResource r, String uri) {
		for(MethodParameter param:KtbsUtils.toIterable(r.listMethodParameters())) 
			putLiteral(uri, KtbsConstants.P_HAS_PARAMETER, param.getName()+"="+param.getValue());
	}

	private void putTraceResource(Trace trace) {
		putLiteral(trace.getURI(), KtbsConstants.P_HAS_ORIGIN, trace.getOrigin());
		putLiteral(trace.getURI(), KtbsConstants.P_COMPLIES_WITH_MODEL, trace.getCompliantWithModel());

		if(withLinkedResource()) {
			put(trace.getTraceModel());
			addResourceListToModel(trace.listTransformedTraces());
		} 

		if(withLinkedResourceURI()) {
			model.getResource(trace.getURI()).addProperty(
					model.getProperty(KtbsConstants.P_HAS_MODEL), 
					model.getResource(trace.getTraceModel().getURI()));

			for(KtbsResource child:KtbsUtils.toIterable(trace.listTransformedTraces()))
				model.getResource(trace.getURI()).addProperty(
						model.getProperty(KtbsConstants.P_HAS_SOURCE), 
						model.getResource(child.getURI()));
		}

		if(withLinkedResourceType()) {
			model.getResource(trace.getTraceModel().getURI()).addProperty(
					RDF.type, 
					model.getResource(KtbsConstants.TRACE_MODEL));
			for(KtbsResource child:KtbsUtils.toIterable(trace.listTransformedTraces()))
				model.getResource(child.getURI()).addProperty(
						RDF.type, 
						model.getResource(KtbsUtils.getRDFType(child)));
		}

		if(withContainedResource()) {
			addResourceListToModel(trace.listObsels());
		} else if(withContainedResourceURI()) {
			for(Obsel obsel:KtbsUtils.toIterable(trace.listObsels())) {
				model.getResource(obsel.getURI()).addProperty(
						model.getProperty(KtbsConstants.P_HAS_TRACE), 
						model.getResource(trace.getURI()));

				if(withContainedResourceType()) {
					model.getResource(obsel.getURI()).addProperty(
							RDF.type, 
							model.getResource(obsel.getObselType().getURI()));
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
