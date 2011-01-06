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
	public static final String KTBS_NAMESPACE = "http://liris.cnrs.fr/silex/2009/ktbs#";
	public static final String KTBS_KTBSROOT = KTBS_NAMESPACE + "KtbsRoot";
	public static final String P_HAS_BASE = KTBS_NAMESPACE + "hasBase";
	public static final String KTBS_BASE = KTBS_NAMESPACE + "Base";
	public static final String P_OWNS = KTBS_NAMESPACE + "owns";
	public static final String STORED_TRACE = KTBS_NAMESPACE + "StoredTrace";
	public static final String COMPUTED_TRACE = KTBS_NAMESPACE + "ComputedTrace";
	public static final String TRACE_MODEL = KTBS_NAMESPACE + "TraceModel";
	public static final String METHOD = KTBS_NAMESPACE + "Method";
	public static final String P_HAS_BEGIN = KTBS_NAMESPACE + "hasBegin";
	public static final String P_HAS_END = KTBS_NAMESPACE + "hasEnd";
	public static final String P_HAS_BEGIN_DT = KTBS_NAMESPACE + "hasBeginDT";
	public static final String P_HAS_END_DT = KTBS_NAMESPACE + "hasEndDT";
	public static final String P_HAS_SUBJECT = KTBS_NAMESPACE + "hasSubject";
	public static final String P_HAS_TRACE = KTBS_NAMESPACE + "hasTrace";
	public static final String P_HAS_MODEL = KTBS_NAMESPACE + "hasModel";
	public static final String P_HAS_METHOD = KTBS_NAMESPACE + "hasMethod";
	public static final String KTBS_HASORIGIN = KTBS_NAMESPACE + "hasOrigin";
	public static final String KTBS_DESCRIPTION_OF = KTBS_NAMESPACE + "descriptionOf";
	public static final String KTBS_COMPLIES_WITH_MODEL = KTBS_NAMESPACE + "compliesWithModel";
}
