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
	public static final String KTBS_HASBASE = KTBS_NAMESPACE + "hasBase";
	public static final String KTBS_BASE = KTBS_NAMESPACE + "Base";
	public static final String KTBS_OWNS = KTBS_NAMESPACE + "owns";
	public static final String KTBS_STOREDTRACE = KTBS_NAMESPACE + "StoredTrace";
	public static final String KTBS_COMPUTEDTRACE = KTBS_NAMESPACE + "ComputedTrace";
	public static final String KTBS_TRACEMODEL = KTBS_NAMESPACE + "TraceModel";
	public static final String KTBS_METHOD = KTBS_NAMESPACE + "Method";
	public static final String KTBS_HASBEGIN = KTBS_NAMESPACE + "hasBegin";
	public static final String KTBS_HASEND = KTBS_NAMESPACE + "hasEnd";
	public static final String KTBS_HASBEGIN_DT = KTBS_NAMESPACE + "hasBeginDT";
	public static final String KTBS_HASEND_DT = KTBS_NAMESPACE + "hasEndDT";
	public static final String KTBS_HASSUBJECT = KTBS_NAMESPACE + "hasSubject";
	public static final String KTBS_HASTRACE = KTBS_NAMESPACE + "hasTrace";
	public static final String KTBS_HASMODEL = KTBS_NAMESPACE + "hasModel";
	public static final String KTBS_HASORIGIN = KTBS_NAMESPACE + "hasOrigin";
	public static final String KTBS_DESCRIPTION_OF = KTBS_NAMESPACE + "descriptionOf";
}
