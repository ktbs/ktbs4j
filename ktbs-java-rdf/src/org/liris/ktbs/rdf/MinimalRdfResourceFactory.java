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
	
	public Model createBaseModel(String rootURI, String baseURI) {
		Model model = createModelWithType(baseURI, KtbsConstants.BASE);
		model.getResource(rootURI).addProperty(model.getProperty(KtbsConstants.P_HAS_BASE), model.getResource(baseURI));
		return model;
	}
	
	public Model createMethodModel(String baseURI, String methodURI, String inheritedMethodUri, Map<String,String> parameters) {
		Model model = createModelWithType(methodURI, KtbsConstants.METHOD);
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(methodURI));
		model.getResource(methodURI).addProperty(model.getProperty(KtbsConstants.P_INHERITS), model.getResource(inheritedMethodUri));
		if(parameters != null) {
			for(Entry<String, String> entry:parameters.entrySet())
				model.getResource(methodURI).addLiteral(model.getProperty(KtbsConstants.P_HAS_PARAMETER), entry.getKey()+"="+entry.getValue());
		}
		return model;
	}
	
	public Model createTraceModelModel(String baseURI, String modelURI) {
		Model model = createModelWithType(modelURI, KtbsConstants.TRACE_MODEL);
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(modelURI));
		return model;
	}
	
	public Model createStoredTraceModel(String baseURI, String traceURI, String modelURI, String origin) {
		Model model = createModelWithType(traceURI, KtbsConstants.STORED_TRACE);
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(traceURI));
		model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_MODEL), model.getResource(modelURI));
		model.getResource(traceURI).addLiteral(model.getProperty(KtbsConstants.P_HAS_ORIGIN), origin);
		return model;
	}
	
	public Model createComputedTraceModel(String baseURI, String traceURI, String methodURI, Collection<String> sourceTracesURIs) {
		Model model = createModelWithType(traceURI, KtbsConstants.COMPUTED_TRACE);
		model.getResource(baseURI).addProperty(model.getProperty(KtbsConstants.P_OWNS), model.getResource(traceURI));
		model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_METHOD), model.getResource(methodURI));
		for(String sourceTraceURI:sourceTracesURIs) 
			model.getResource(traceURI).addProperty(model.getProperty(KtbsConstants.P_HAS_SOURCE), model.getResource(sourceTraceURI));
		return model;
	}
	
	public Model createObselModel(String traceURI, String obselURI, String typeURI, String subject, String beginDT, String endDT, BigInteger begin, BigInteger end, Map<String, Object> attributes) {
		
		Model model = createModelWithType(obselURI, typeURI);
		Resource or = model.getResource(obselURI);
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
		return model;
	}
	
	private Model createModelWithType(String baseURI, String rdfType) {
		Model model = ModelFactory.createDefaultModel();
		model.getResource(baseURI).addProperty(
				RDF.type, 
				model.getProperty(rdfType));
		return model;
	}

}
