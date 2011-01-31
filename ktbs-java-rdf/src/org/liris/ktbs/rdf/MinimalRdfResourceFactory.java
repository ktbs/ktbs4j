package org.liris.ktbs.rdf;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.liris.ktbs.core.KtbsConstants;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * A factory that creates Jena models that represent KTBS resources in RDF,
 * according to the KTBS specifications.
 * 
 * @author Damien Cram
 *
 */
public class MinimalRdfResourceFactory {
	
	/*
	 * Singleton
	 */
	private static MinimalRdfResourceFactory instance;
	private MinimalRdfResourceFactory() {
		super();
	}
	public static MinimalRdfResourceFactory getInstance() {
		if(instance==null)
			instance = new MinimalRdfResourceFactory();
		return instance;
	}
	
	public Resource createAttributeTypeModel(String attUri, String domainUri) {
		Resource atr = createModelWithType(attUri, KtbsConstants.ATTRIBUTE_TYPE);
		Model model = atr.getModel();
		addPropertyIfNotNull(model, attUri, KtbsConstants.P_HAS_ATTRIBUTE_DOMAIN, domainUri); 
		return atr;
	}

	public Resource createRelationTypeModel(String relUri, String superRelationTypeUri, String domainUri, String rangeUri) {
		Resource rtr = createModelWithType(relUri, KtbsConstants.RELATION_TYPE);
		Model model = rtr.getModel();
		addPropertyIfNotNull(model, relUri, KtbsConstants.P_HAS_RELATION_DOMAIN, domainUri); 
		addPropertyIfNotNull(model, relUri, KtbsConstants.P_HAS_RELATION_RANGE, rangeUri); 
		addPropertyIfNotNull(model, relUri, KtbsConstants.P_HAS_SUPER_RELATION_TYPE, superRelationTypeUri); 
		return rtr;
	}

	public Resource createObselTypeModel(String obsTypeUri, String superObselTypeUri) {
		Resource otr = createModelWithType(obsTypeUri, KtbsConstants.OBSEL_TYPE);
		Model model = otr.getModel();
		addPropertyIfNotNull(model, obsTypeUri, KtbsConstants.P_HAS_SUPER_OBSEL_TYPE, superObselTypeUri); 
		return otr;
	}
	
	private void addPropertyIfNotNull(Model model, String subjectUri, String pName,
			String objectUri) {
		if(objectUri != null)
		model.getResource(subjectUri).addProperty(model.getProperty(pName), model.getResource(objectUri));
		
	}
	public Resource createBaseModel(String rootURI, String baseURI) {
		Resource br = createModelWithType(baseURI, KtbsConstants.BASE);
		Model model = br.getModel();
		model.getResource(rootURI).addProperty(model.getProperty(KtbsConstants.P_HAS_BASE), model.getResource(baseURI));
		return br;
	}
	
	public Resource createMethodModel(String baseURI, String methodURI, String inheritedMethodUri, Map<String,String> parameters) {
		Resource mr = createModelWithType(methodURI, KtbsConstants.METHOD);
		Model model = mr.getModel();
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(methodURI));
		model.getResource(methodURI).addProperty(model.getProperty(KtbsConstants.P_INHERITS), model.getResource(inheritedMethodUri));
		if(parameters != null) {
			for(Entry<String, String> entry:parameters.entrySet())
				model.getResource(methodURI).addLiteral(model.getProperty(KtbsConstants.P_HAS_PARAMETER), entry.getKey()+"="+entry.getValue());
		}
		return mr;
	}
	
	public Resource createTraceModelModel(String baseURI, String modelURI) {
		Resource tmr = createModelWithType(modelURI, KtbsConstants.TRACE_MODEL);
		Model model = tmr.getModel();
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(modelURI));
		return tmr;
	}
	
	public Resource createStoredTraceModel(String baseURI, String traceURI, String modelURI, String origin) {
		Resource str = createModelWithType(traceURI, KtbsConstants.STORED_TRACE);
		Model model = str.getModel();
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(traceURI));
		model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_MODEL), model.getResource(modelURI));
		model.getResource(traceURI).addLiteral(model.getProperty(KtbsConstants.P_HAS_ORIGIN), origin);
		return str;
	}
	
	public Resource createComputedTraceModel(String baseURI, String traceURI, String methodURI, Collection<String> sourceTracesURIs) {
		Resource ctr = createModelWithType(traceURI, KtbsConstants.COMPUTED_TRACE);
		Model model = ctr.getModel();
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(traceURI));
		model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_METHOD), model.getResource(methodURI));
		for(String sourceTraceURI:sourceTracesURIs) 
			model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_SOURCE), model.getResource(sourceTraceURI));
		return ctr;
	}
	
	public Resource createObselModel(String traceURI, String obselURI, String typeURI, String subject, String beginDT, String endDT, BigInteger begin, BigInteger end, Map<String, Object> attributes) {
		
		Resource or = createModelWithType(obselURI, typeURI);
		Model model = or.getModel();
		or.addProperty(model.getProperty(KtbsConstants.P_HAS_TRACE), model.getResource(traceURI));
		if(beginDT != null)
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_BEGIN_DT), model.createTypedLiteral(beginDT, XSDDatatype.XSDdateTime));
		if(endDT != null)
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_END_DT),  model.createTypedLiteral(endDT, XSDDatatype.XSDdateTime));
		if(begin != null)
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_BEGIN), begin);
		if(end != null) {
			Literal endTyped = model.createTypedLiteral(end);
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_END), endTyped);
		}
		if(subject != null)
			or.addLiteral(model.getProperty(KtbsConstants.P_HAS_SUBJECT), subject);

		if(attributes != null) {
			for(Entry<String, Object> entry:attributes.entrySet()) {
				Property attribute = model.getProperty(entry.getKey());
				Collection<RDFNode> values = JenaUtils.asJenaLiteral(model, entry.getValue());
				for(RDFNode value:values)
					or.addProperty(attribute, value);
			}
		}
		return or;
	}
	
	private Resource createModelWithType(String resourceUri, String rdfType) {
		Model model = ModelFactory.createDefaultModel();
		Resource resource;
		if(resourceUri == null ) 
			resource = model.createResource();
		else
			resource = model.createResource(resourceUri);
		resource.addProperty(
				RDF.type, 
				model.getProperty(rdfType));
		return resource;
	}

}
