package org.liris.ktbs.rdf;

public interface KtbsConstants {

	// Rest aspects defined by the KTBS
	public static final String OBSELS_ASPECT = "@obsels";
	public static final String ABOUT_ASPECT = "@about";
	
	
	// Mime type supported by the KTBS
	public static final String MIME_RDF_XML = "Application/rdf+xml";
	public static final String MIME_NTRIPLES = "text/nt";
	public static final String MIME_N3 = "text/n3";
	public static final String MIME_TURTLE = "text/turtle";
	
	
	// KTBS vocabulary
	public static final String NAMESPACE = "http://liris.cnrs.fr/silex/2009/ktbs#";

	// KTBS Root concepts and properties
	public static final String KTBS_ROOT = NAMESPACE + "KtbsRoot";
	public static final String P_HAS_BASE = NAMESPACE + "hasBase";

	// Base concepts and properties
	public static final String BASE = NAMESPACE + "Base";
	public static final String P_OWNS = NAMESPACE + "owns";
	public static final String STORED_TRACE = NAMESPACE + "StoredTrace";
	public static final String COMPUTED_TRACE = NAMESPACE + "ComputedTrace";
	public static final String METHOD = NAMESPACE + "Method";
	public static final String TRACE_MODEL = NAMESPACE + "TraceModel";

	// Trace properties
	public static final String P_HAS_ORIGIN = NAMESPACE + "hasOrigin";
	public static final String P_DESCRIPTION_OF = NAMESPACE + "descriptionOf";
	public static final String P_COMPLIES_WITH_MODEL = NAMESPACE + "compliesWithModel";
	public static final String P_HAS_MODEL = NAMESPACE + "hasModel";
	public static final String P_HAS_SOURCE = NAMESPACE + "hasSource";

	// Trace model concepts and properties
	public static final String RELATION_TYPE = NAMESPACE + "RelationType";
	public static final String ATTRIBUTE_TYPE = NAMESPACE + "AttributeType";
	public static final String OBSEL_TYPE = NAMESPACE + "ObselType";
	public static final String P_HAS_ATTRIBUTE_DOMAIN = NAMESPACE + "hasAttributeDomain";
	public static final String P_HAS_RELATION_DOMAIN = NAMESPACE + "hasRelationDomain";
	public static final String P_HAS_RELATION_RANGE = NAMESPACE + "hasRelationRange";
	public static final String P_HAS_SUPER_RELATION_TYPE = NAMESPACE + "hasSuperRelationType";
	public static final String P_HAS_SUPER_OBSEL_TYPE = NAMESPACE + "hasSuperObselType";
	
	
	// Obsel Properties
	public static final String P_HAS_BEGIN = NAMESPACE + "hasBegin";
	public static final String P_HAS_END = NAMESPACE + "hasEnd";
	public static final String P_HAS_BEGIN_DT = NAMESPACE + "hasBeginDT";
	public static final String P_HAS_END_DT = NAMESPACE + "hasEndDT";
	public static final String P_HAS_SUBJECT = NAMESPACE + "hasSubject";
	public static final String P_HAS_TRACE = NAMESPACE + "hasTrace";
	public static final String P_HAS_METHOD = NAMESPACE + "hasMethod";
	
	// Method Properties
	public static final String P_HAS_PARAMETER = NAMESPACE + "hasParameter";
	public static final String P_INHERITS = NAMESPACE + "inherits";
	public static final String SCRIPT_PYTHON = NAMESPACE + "script-python";
	public static final String P_HAS_SOURCE_OBSEL =  NAMESPACE + "hasSourceObsel";
	
	
}
