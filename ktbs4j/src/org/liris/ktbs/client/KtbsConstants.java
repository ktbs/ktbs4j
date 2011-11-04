package org.liris.ktbs.client;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Constant definitions for the vocabulary defined by the KTBS in the
 * namespace "http://liris.cnrs.fr/silex/2009/ktbs#".
 *  
 * @author Damien Cram
 *
 */
public interface KtbsConstants {

	// Date helper constants
	public static final SimpleDateFormat XSD_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
	public static final TimeZone UTC_ZONE = TimeZone.getTimeZone("UTC");
	
	// Rest aspects defined by the KTBS
	public static final String OBSELS_ASPECT = "@obsels";
	public static final String ABOUT_ASPECT = "@about";
	
	public static final String JENA_TURTLE = "TURTLE";
	public static final String JENA_N3 = "N3";
	public static final String JENA_N3_PP = "N3-PP";
	public static final String JENA_N3_PLAIN = "N3-PLAIN";
	public static final String JENA_N3_TRIPLE = "N3-TRIPLE";
	public static final String JENA_N_TRIPLES = "N-TRIPLE";
	public static final String JENA_RDF_XML = "RDF/XML";
	public static final String JENA_RDF_XML_ABBR = "RDF/XML-ABBREV";
	
	// Mime type supported by the KTBS
	public static final String MIME_RDF_XML = "application/rdf+xml";
	public static final String MIME_NTRIPLES = "text/nt";
	public static final String MIME_N3 = "text/n3";
	public static final String MIME_TURTLE = "text/turtle";
	public static final String MIME_JSON = "application/json";
	
	
	// KTBS vocabulary
	public static final String NAMESPACE_KTBS = "http://liris.cnrs.fr/silex/2009/ktbs#";
	public static final String NAMESPACE_RDFREST = "http://liris.cnrs.fr/silex/2009/rdfrest#";
	public static final String NAMESPACE_RDFS = "http://www.w3.org/2000/01/rdf-schema#";
	public static final String NAMESPACE_XSD = "http://www.w3.org/2001/XMLSchema#";
	public static final String NAMESPACE_D3KODE = "htp://liris.d3kode.org/";

	// KTBS Root concepts and properties
	public static final String ROOT = NAMESPACE_KTBS + "KtbsRoot";
	public static final String P_HAS_BASE = NAMESPACE_KTBS + "hasBase";

	// Base concepts and properties
	public static final String BASE = NAMESPACE_KTBS + "Base";
	public static final String P_CONTAINS = NAMESPACE_KTBS + "contains";
	public static final String STORED_TRACE = NAMESPACE_KTBS + "StoredTrace";
	public static final String COMPUTED_TRACE = NAMESPACE_KTBS + "ComputedTrace";
	public static final String METHOD = NAMESPACE_KTBS + "Method";
	public static final String TRACE_MODEL = NAMESPACE_KTBS + "TraceModel";

	// Trace properties
	public static final String P_HAS_TRACE_BEGIN = NAMESPACE_KTBS + "hasTraceBegin";
	public static final String P_HAS_TRACE_BEGIN_DT = NAMESPACE_KTBS + "hasTraceBeginDT";
	public static final String P_HAS_TRACE_END = NAMESPACE_KTBS + "hasTraceEnd";
	public static final String P_HAS_TRACE_END_DT = NAMESPACE_KTBS + "hasTraceEndDT";
	public static final String P_HAS_ORIGIN = NAMESPACE_KTBS + "hasOrigin";
	public static final String P_DESCRIPTION_OF = NAMESPACE_KTBS + "descriptionOf";
	public static final String P_COMPLIES_WITH_MODEL = NAMESPACE_KTBS + "compliesWithModel";
	public static final String P_HAS_MODEL = NAMESPACE_KTBS + "hasModel";
	public static final String P_HAS_SOURCE = NAMESPACE_KTBS + "hasSource";
	public static final String P_HAS_INTERMEDIATE_SOURCE = NAMESPACE_KTBS + "hasIntermediateSource";

	// Trace model concepts and properties
	public static final String RELATION_TYPE = NAMESPACE_KTBS + "RelationType";
	public static final String ATTRIBUTE_TYPE = NAMESPACE_KTBS + "AttributeType";
	public static final String OBSEL_TYPE = NAMESPACE_KTBS + "ObselType";
	public static final String P_HAS_ATTRIBUTE_DOMAIN = NAMESPACE_KTBS + "hasAttributeDomain";
	public static final String P_HAS_ATTRIBUTE_RANGE = NAMESPACE_KTBS + "hasAttributeRange";
	public static final String P_HAS_RELATION_DOMAIN = NAMESPACE_KTBS + "hasRelationDomain";
	public static final String P_HAS_RELATION_RANGE = NAMESPACE_KTBS + "hasRelationRange";
	public static final String P_HAS_SUPER_RELATION_TYPE = NAMESPACE_KTBS + "hasSuperRelationType";
	public static final String P_HAS_SUPER_OBSEL_TYPE = NAMESPACE_KTBS + "hasSuperObselType";
	public static final String METADATA = NAMESPACE_D3KODE + "metadata#";
	
	// Obsel Properties
	public static final String P_HAS_BEGIN = NAMESPACE_KTBS + "hasBegin";
	public static final String P_HAS_END = NAMESPACE_KTBS + "hasEnd";
	public static final String P_HAS_BEGIN_DT = NAMESPACE_KTBS + "hasBeginDT";
	public static final String P_HAS_END_DT = NAMESPACE_KTBS + "hasEndDT";
	public static final String P_HAS_SUBJECT = NAMESPACE_KTBS + "hasSubject";
	public static final String P_HAS_TRACE = NAMESPACE_KTBS + "hasTrace";
	public static final String P_HAS_METHOD = NAMESPACE_KTBS + "hasMethod";
	public static final String P_HAS_OWNER = NAMESPACE_KTBS + "hasOwner";
	
	public static final String P_HAS_ETAG = NAMESPACE_RDFREST + "hasETag";
	
	// Method Properties
	public static final String P_HAS_PARAMETER = NAMESPACE_KTBS + "hasParameter";
	public static final String P_INHERITS = NAMESPACE_KTBS + "inherits";
	public static final String SCRIPT_PYTHON = NAMESPACE_KTBS + "script-python";
	public static final String P_HAS_SOURCE_OBSEL =  NAMESPACE_KTBS + "hasSourceObsel";
	public static final String FILTER = NAMESPACE_KTBS + "filter";
	public static final String FUSION = NAMESPACE_KTBS + "fusion";
	public static final String SPARQL = NAMESPACE_KTBS + "sparql";
	public static final String EXTERNAL = NAMESPACE_KTBS + "external";;
	public static final String COMMENT = NAMESPACE_RDFS + "comment";
	public static final String SUPER_METHOD = NAMESPACE_KTBS + "supermethod";
	//Method parameters
	public static final String PARAMETER_SUBMETHODS = "submethods";
	public static final String PARAMETER_SPARQL = "sparql";
	public static final String PARAMETER_MODEL = "model";
	public static final String PARAMETER_COMMAND_LINE = "command-line";
	public static final String PARAMETER_FORMAT = "format";
	
	public static final int HTTP_CODE_RESOURCE_NOT_FOUND = 404;
	
	
}
